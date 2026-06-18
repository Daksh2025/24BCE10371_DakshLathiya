package com.gym.tracker.repository;

import com.gym.tracker.model.SubscriptionPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, String> {

    Optional<SubscriptionPlan> findByName(String name);

    boolean existsByName(String name);

    // Find plans that contain a specific member (for reverse lookup)
    List<SubscriptionPlan> findByMemberIdsContaining(String memberId);
}
