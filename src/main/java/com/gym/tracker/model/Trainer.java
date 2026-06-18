// // package com.gym.tracker.model;

// // import jakarta.validation.constraints.Email;
// // import jakarta.validation.constraints.NotBlank;
// // import lombok.AllArgsConstructor;
// // import lombok.Data;
// // import lombok.NoArgsConstructor;
// // import org.springframework.data.annotation.Id;
// // import org.springframework.data.mongodb.core.index.Indexed;
// // import org.springframework.data.mongodb.core.mapping.Document;

// // /**
// //  * Trainer — a gym staff member who coaches members.
// //  *
// //  * Relationships:
// //  *   Trainer ──► Department  (Many-to-One — departmentId references Department._id)
// //  */
// // @Data
// // @NoArgsConstructor
// // @AllArgsConstructor
// // @Document(collection = "trainers")
// // public class Trainer {

// //     @Id
// //     private String id;

// //     @NotBlank(message = "Trainer name is required")
// //     private String name;

// //     @Email(message = "Invalid email address")
// //     @NotBlank(message = "Email is required")
// //     @Indexed(unique = true)
// //     private String email;

// //     private String phone;

// //     private String specialization;   // e.g. "CrossFit", "Yoga", "Strength"

// //     private String certification;    // e.g. "ACE", "NASM", "CSCS"

// //     private boolean available = true;

// //     // ── MANY-TO-ONE: Trainer → Department ──────────────────────
// //     // Stores the _id of the Department this trainer belongs to.
// //     private String departmentId;
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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Trainer — a gym staff member who coaches members.
 *
 * Relationships:
 *   Trainer ──► Department  (Many-to-One — departmentId references Department._id)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trainers")
public class Trainer {

    @Id
    private String id;

    @NotBlank(message = "Trainer name is required")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    private String email;

    private String phone;

    private String specialization;   // e.g. "CrossFit", "Yoga", "Strength"

    private String certification;    // e.g. "ACE", "NASM", "CSCS"

    private boolean available = true;

    // ── MANY-TO-ONE: Trainer → Department ──────────────────────
    private String departmentId;
}