// // package com.gym.tracker.model;

// // import jakarta.validation.constraints.Email;
// // import jakarta.validation.constraints.NotBlank;
// // import lombok.AllArgsConstructor;
// // import lombok.Data;
// // import lombok.NoArgsConstructor;
// // import org.springframework.data.annotation.Id;
// // import org.springframework.data.mongodb.core.index.Indexed;
// // import org.springframework.data.mongodb.core.mapping.Document;

// // import java.time.LocalDate;

// // /**
// //  * Member — a gym subscriber.
// //  *
// //  * Relationships:
// //  *   Member ──► SubscriptionPlan  (Many-to-One  — planId references SubscriptionPlan._id)
// //  *   Member ──► Trainer           (Many-to-One  — trainerId, optional assignment)
// //  *   SubscriptionPlan ──► Member  (One-to-Many  — maintained via SubscriptionPlan.memberIds)
// //  */
// // @Data
// // @NoArgsConstructor
// // @AllArgsConstructor
// // @Document(collection = "members")
// // public class Member {

// //     @Id
// //     private String id;

// //     @NotBlank(message = "Member name is required")
// //     private String name;

// //     @Email(message = "Invalid email address")
// //     @NotBlank(message = "Email is required")
// //     @Indexed(unique = true)
// //     private String email;

// //     private String phone;

// //     private String address;

// //     private LocalDate dateOfBirth;

// //     private LocalDate joinDate;

// //     private LocalDate subscriptionStartDate;

// //     private LocalDate subscriptionEndDate;

// //     private MemberStatus status = MemberStatus.ACTIVE;

// //     // Emergency contact
// //     private String emergencyContactName;
// //     private String emergencyContactPhone;

// //     // Health notes
// //     private String healthNotes;

// //     // ── MANY-TO-ONE: Member → SubscriptionPlan ─────────────────
// //     // Stores the _id of the SubscriptionPlan this member is on.
// //     private String planId;

// //     // ── MANY-TO-ONE: Member → Trainer (optional) ───────────────
// //     // Stores the _id of the assigned Trainer (can be null).
// //     private String trainerId;

// //     // ── Status Enum ────────────────────────────────────────────
// //     public enum MemberStatus {
// //         ACTIVE,
// //         INACTIVE,
// //         CANCELLED,
// //         EXPIRED,
// //         SUSPENDED
// //     }
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

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Member — a gym subscriber.
 *
 * Relationships:
 *   Member ──► SubscriptionPlan  (Many-to-One — planId references SubscriptionPlan._id)
 *   Member ──► Trainer           (Many-to-One — trainerId, optional assignment)
 *   SubscriptionPlan ──► Member  (One-to-Many — maintained via SubscriptionPlan.memberIds)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "members")
public class Member {

    @Id
    private String id;

    @NotBlank(message = "Member name is required")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    private String email;

    private String phone;

    private String address;

    private LocalDate dateOfBirth;

    private LocalDate joinDate;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    private MemberStatus status = MemberStatus.ACTIVE;

    private String emergencyContactName;
    private String emergencyContactPhone;

    private String healthNotes;

    // ── MANY-TO-ONE: Member → SubscriptionPlan ─────────────────
    private String planId;

    // ── MANY-TO-ONE: Member → Trainer (optional) ───────────────
    private String trainerId;

    // ── Status Enum ────────────────────────────────────────────
    public enum MemberStatus {
        ACTIVE,
        INACTIVE,
        CANCELLED,
        EXPIRED,
        SUSPENDED
    }
}