package com.shop.store_management.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",nullable = false, updatable = false)
    private Long id;

    @Column(name = "product_name",nullable = false, length = 45)
    @NotBlank(message = "Name of the product is mandatory")
    private String name;

    @Column(name = "price", nullable = false)
    @Min(value=0, message = "The price has to be positive")
    private BigDecimal price;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "stock_quantity", nullable = false)
    @Min(value = 0, message = "Stock quantity must be positive")
    private int stockQuantity;

    @Column(name = "category", nullable = false, length = 60)
    private String category;

}
