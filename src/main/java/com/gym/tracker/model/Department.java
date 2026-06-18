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
// //  * Department — e.g. Cardio, Strength, Yoga, Aquatics
// //  *
// //  * Relationships:
// //  *   Department ←── Trainer  (One-to-Many  /  Trainer → Department is Many-to-One)
// //  */
// // @Data
// // @NoArgsConstructor
// // @AllArgsConstructor
// // @Document(collection = "departments")
// // public class Department {

// //     @Id
// //     private String id;

// //     @NotBlank(message = "Department name is required")
// //     @Indexed(unique = true)
// //     private String name;

// //     private String description;

// //     private String location;       // e.g. "Floor 2 - East Wing"

// //     @Email(message = "Invalid contact email")
// //     private String contactEmail;

// //     private boolean active = true;
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
 * Department — e.g. Cardio, Strength, Yoga, Aquatics
 *
 * Relationships:
 *   Department ←── Trainer  (One-to-Many / Trainer → Department is Many-to-One)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "departments")
public class Department {

    @Id
    private String id;

    @NotBlank(message = "Department name is required")
    @Indexed(unique = true)
    private String name;

    private String description;

    private String location;       // e.g. "Floor 2 - East Wing"

    @Email(message = "Invalid contact email")
    private String contactEmail;

    private boolean active = true;
}