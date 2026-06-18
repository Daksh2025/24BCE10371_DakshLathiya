// // package com.gym.tracker.model;

// // import jakarta.validation.constraints.NotBlank;
// // import jakarta.validation.constraints.NotNull;
// // import jakarta.validation.constraints.Positive;
// // import lombok.AllArgsConstructor;
// // import lombok.Data;
// // import lombok.NoArgsConstructor;
// // import org.springframework.data.annotation.Id;
// // import org.springframework.data.mongodb.core.index.Indexed;
// // import org.springframework.data.mongodb.core.mapping.Document;

// // import java.util.ArrayList;
// // import java.util.List;

// // /**
// //  * SubscriptionPlan — Monthly / Annual / VIP / etc.
// //  *
// //  * Relationships:
// //  *   Plan ──► Members  (One-to-Many  — memberIds list stores member IDs)
// //  *   Member ──► Plan   (Many-to-One  — Member.planId points here)
// //  */
// // @Data
// // @NoArgsConstructor
// // @AllArgsConstructor
// // @Document(collection = "subscription_plans")
// // public class SubscriptionPlan {

// //     @Id
// //     private String id;

// //     @NotBlank(message = "Plan name is required")
// //     @Indexed(unique = true)
// //     private String name;             // Monthly | Annual | VIP

// //     @NotNull(message = "Price is required")
// //     @Positive(message = "Price must be positive")
// //     private Double price;            // USD

// //     @NotNull(message = "Duration (months) is required")
// //     @Positive(message = "Duration must be positive")
// //     private Integer durationMonths;  // 1, 12, etc.

// //     private String description;

// //     private List<String> features = new ArrayList<>();
// //     //  e.g. ["Unlimited classes", "Pool access", "Personal trainer session"]

// //     // ── ONE-TO-MANY: Plan → Members ────────────────────────────
// //     // Each time a member subscribes, their ID is added here.
// //     // This list is kept in sync by MemberService.
// //     private List<String> memberIds = new ArrayList<>();
// // }



// import lombok.Data;

// @Data  // generates getters, setters, equals, hashCode, toString
// public class SubscriptionPlan {
//     private Long id;
//     private String name;
//     private Double price;
//     private Integer durationMonths;
//     private String description;
//     private List<String> features;
//     private List<Long> memberIds;
// }


package com.gym.tracker.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SubscriptionPlan — Monthly / Annual / VIP / etc.
 *
 * Relationships:
 *   Plan ──► Members  (One-to-Many — memberIds list stores member IDs)
 *   Member ──► Plan   (Many-to-One — Member.planId points here)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscription_plans")
public class SubscriptionPlan {

    @Id
    private String id;

    @NotBlank(message = "Plan name is required")
    @Indexed(unique = true)
    private String name;             // Monthly | Annual | VIP

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;            // USD

    @NotNull(message = "Duration (months) is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMonths;  // 1, 12, etc.

    private String description;

    private List<String> features = new ArrayList<>();

    // ── ONE-TO-MANY: Plan → Members ────────────────────────────
    private List<String> memberIds = new ArrayList<>();
}