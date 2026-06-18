package com.gym.tracker.repository;

import com.gym.tracker.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    // Many-to-One reverse: get all members on a plan
    List<Member> findByPlanId(String planId);

    // Get all members assigned to a trainer
    List<Member> findByTrainerId(String trainerId);

    // Filter members by status
    List<Member> findByStatus(Member.MemberStatus status);

    // Core feature: find subscriptions expiring in a date window
    List<Member> findBySubscriptionEndDateBetween(LocalDate startDate, LocalDate endDate);

    // Members whose subscription has already expired
    List<Member> findBySubscriptionEndDateBefore(LocalDate date);
}
