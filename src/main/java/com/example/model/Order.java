package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private User user (User, not null)
    status (Status (enum), not null)
    total (BigDecimal, not null)
    orderDate (LocalDateTime, not null)
    shippingAddress (String, not null)
    orderItems (Set<OrderItem>)

}
