package com.gym.tracker.service;

import com.gym.tracker.exception.DuplicateResourceException;
import com.gym.tracker.exception.ResourceNotFoundException;
import com.gym.tracker.model.Member;
import com.gym.tracker.model.SubscriptionPlan;
import com.gym.tracker.repository.MemberRepository;
import com.gym.tracker.repository.SubscriptionPlanRepository;
import com.gym.tracker.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository           memberRepository;
    private final SubscriptionPlanRepository planRepository;
    private final TrainerRepository          trainerRepository;
    private final SubscriptionPlanService    planService;

    // ── CREATE ──────────────────────────────────────────────────
    public Member createMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new DuplicateResourceException(
                    "Member with email '" + member.getEmail() + "' already exists.");
        }

        // Set join date if missing
        if (member.getJoinDate() == null) {
            member.setJoinDate(LocalDate.now());
        }

        // Validate & auto-calculate subscription window
        if (member.getPlanId() != null && !member.getPlanId().isBlank()) {
            SubscriptionPlan plan = planRepository.findById(member.getPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", member.getPlanId()));

            if (member.getSubscriptionStartDate() == null) {
                member.setSubscriptionStartDate(LocalDate.now());
            }
            if (member.getSubscriptionEndDate() == null) {
                member.setSubscriptionEndDate(
                        member.getSubscriptionStartDate().plusMonths(plan.getDurationMonths()));
            }
        }

        // Validate trainer if provided
        if (member.getTrainerId() != null && !member.getTrainerId().isBlank()) {
            trainerRepository.findById(member.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer", member.getTrainerId()));
        }

        Member saved = memberRepository.save(member);

        // ── Sync One-to-Many: add this member to plan's memberIds ──
        if (saved.getPlanId() != null && !saved.getPlanId().isBlank()) {
            planService.addMemberToPlan(saved.getPlanId(), saved.getId());
        }

        log.info("Created member: {} [{}]", saved.getName(), saved.getId());
        return saved;
    }

    // ── READ: All ───────────────────────────────────────────────
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // ── READ: By ID ─────────────────────────────────────────────
    public Member getMemberById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
    }

    // ── READ: By Plan (Many-to-One reverse) ─────────────────────
    public List<Member> getMembersByPlan(String planId) {
        if (!planRepository.existsById(planId)) {
            throw new ResourceNotFoundException("SubscriptionPlan", planId);
        }
        return memberRepository.findByPlanId(planId);
    }

    // ── READ: By Trainer ────────────────────────────────────────
    public List<Member> getMembersByTrainer(String trainerId) {
        if (!trainerRepository.existsById(trainerId)) {
            throw new ResourceNotFoundException("Trainer", trainerId);
        }
        return memberRepository.findByTrainerId(trainerId);
    }

    // ── READ: By Status ─────────────────────────────────────────
    public List<Member> getMembersByStatus(Member.MemberStatus status) {
        return memberRepository.findByStatus(status);
    }

    // ── READ: Expiring THIS Month ───────────────────────────────
    public List<Member> getExpiringThisMonth() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end   = start.plusMonths(1).minusDays(1);
        log.debug("Checking expirations between {} and {}", start, end);
        return memberRepository.findBySubscriptionEndDateBetween(start, end);
    }

    // ── READ: Already Expired ───────────────────────────────────
    public List<Member> getExpiredMembers() {
        return memberRepository.findBySubscriptionEndDateBefore(LocalDate.now());
    }

    // ── UPDATE: Contact / personal info ─────────────────────────
    public Member updateMember(String id, Member updated) {
        Member existing = getMemberById(id);

        if (!existing.getEmail().equals(updated.getEmail())
                && memberRepository.existsByEmail(updated.getEmail())) {
            throw new DuplicateResourceException(
                    "Email '" + updated.getEmail() + "' is already registered.");
        }

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setEmergencyContactName(updated.getEmergencyContactName());
        existing.setEmergencyContactPhone(updated.getEmergencyContactPhone());
        existing.setHealthNotes(updated.getHealthNotes());
        existing.setStatus(updated.getStatus());

        // Allow trainer re-assignment
        if (updated.getTrainerId() != null && !updated.getTrainerId().isBlank()) {
            trainerRepository.findById(updated.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer", updated.getTrainerId()));
            existing.setTrainerId(updated.getTrainerId());
        }

        log.info("Updated member: {}", id);
        return memberRepository.save(existing);
    }

    // ── UPDATE: Upgrade / change subscription plan ──────────────
    public Member upgradePlan(String memberId, String newPlanId) {
        Member member = getMemberById(memberId);
        SubscriptionPlan newPlan = planRepository.findById(newPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", newPlanId));

        String oldPlanId = member.getPlanId();

        // Remove from old plan's memberIds
        if (oldPlanId != null && !oldPlanId.isBlank()) {
            planService.removeMemberFromPlan(oldPlanId, memberId);
        }

        // Apply new plan
        member.setPlanId(newPlanId);
        member.setSubscriptionStartDate(LocalDate.now());
        member.setSubscriptionEndDate(LocalDate.now().plusMonths(newPlan.getDurationMonths()));
        member.setStatus(Member.MemberStatus.ACTIVE);

        Member saved = memberRepository.save(member);

        // Add to new plan's memberIds
        planService.addMemberToPlan(newPlanId, memberId);

        log.info("Member {} upgraded from plan {} to {}", memberId, oldPlanId, newPlanId);
        return saved;
    }

    // ── DELETE ──────────────────────────────────────────────────
    public void deleteMember(String id) {
        Member member = getMemberById(id);

        // Remove from plan's memberIds before deleting
        if (member.getPlanId() != null && !member.getPlanId().isBlank()) {
            planService.removeMemberFromPlan(member.getPlanId(), id);
        }

        memberRepository.delete(member);
        log.info("Deleted member: {} [{}]", member.getName(), id);
    }
}
