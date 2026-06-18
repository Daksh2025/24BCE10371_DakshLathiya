package com.gym.tracker.service;

import com.gym.tracker.exception.BusinessRuleException;
import com.gym.tracker.exception.DuplicateResourceException;
import com.gym.tracker.exception.ResourceNotFoundException;
import com.gym.tracker.model.SubscriptionPlan;
import com.gym.tracker.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;

    // ── CREATE ──────────────────────────────────────────────────
    public SubscriptionPlan createPlan(SubscriptionPlan plan) {
        if (planRepository.existsByName(plan.getName())) {
            throw new DuplicateResourceException(
                    "Subscription plan '" + plan.getName() + "' already exists.");
        }
        SubscriptionPlan saved = planRepository.save(plan);
        log.info("Created plan: {} [{}]", saved.getName(), saved.getId());
        return saved;
    }

    // ── READ: All ───────────────────────────────────────────────
    public List<SubscriptionPlan> getAllPlans() {
        return planRepository.findAll();
    }

    // ── READ: By ID ─────────────────────────────────────────────
    public SubscriptionPlan getPlanById(String id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", id));
    }

    // ── UPDATE ──────────────────────────────────────────────────
    public SubscriptionPlan updatePlan(String id, SubscriptionPlan updated) {
        SubscriptionPlan existing = getPlanById(id);

        if (!existing.getName().equals(updated.getName())
                && planRepository.existsByName(updated.getName())) {
            throw new DuplicateResourceException(
                    "Plan name '" + updated.getName() + "' is already taken.");
        }

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setDurationMonths(updated.getDurationMonths());
        existing.setDescription(updated.getDescription());
        existing.setFeatures(updated.getFeatures());

        log.info("Updated plan: {}", id);
        return planRepository.save(existing);
    }

    // ── DELETE ──────────────────────────────────────────────────
    public void deletePlan(String id) {
        SubscriptionPlan plan = getPlanById(id);

        if (!plan.getMemberIds().isEmpty()) {
            throw new BusinessRuleException(
                    "Cannot delete plan '" + plan.getName() + "'. "
                    + plan.getMemberIds().size() + " member(s) are currently subscribed. "
                    + "Migrate them to another plan first.");
        }

        planRepository.delete(plan);
        log.info("Deleted plan: {} [{}]", plan.getName(), id);
    }

    // ── INTERNAL: sync member list (called by MemberService) ───

    /** Add a memberId to the plan's memberIds list (One-to-Many sync). */
    public void addMemberToPlan(String planId, String memberId) {
        SubscriptionPlan plan = getPlanById(planId);
        if (!plan.getMemberIds().contains(memberId)) {
            plan.getMemberIds().add(memberId);
            planRepository.save(plan);
            log.debug("Added member {} to plan {}", memberId, planId);
        }
    }

    /** Remove a memberId from the plan's memberIds list. */
    public void removeMemberFromPlan(String planId, String memberId) {
        SubscriptionPlan plan = getPlanById(planId);
        if (plan.getMemberIds().remove(memberId)) {
            planRepository.save(plan);
            log.debug("Removed member {} from plan {}", memberId, planId);
        }
    }

    /** Return the raw list of member IDs for a given plan. */
    public List<String> getMemberIdsByPlan(String planId) {
        return getPlanById(planId).getMemberIds();
    }
}
