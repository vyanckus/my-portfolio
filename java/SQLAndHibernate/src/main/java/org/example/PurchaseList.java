package org.example;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "PurchaseList")
@IdClass(PurchaseListKey.class)
public class PurchaseList {

    @Id
    @Column(name = "student_name")
    private String studentName;

    @Id
    @Column(name = "course_name")
    private String courseName;

    @Column(name = "price")
    private int price;

    @Column(name = "subscription_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionDate;
}
