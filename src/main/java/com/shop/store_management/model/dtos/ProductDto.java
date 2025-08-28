package com.shop.store_management.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Min(value = 0, message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @Min(value = 0, message = "Stock quantity must be positive")
    private int stockQuantity;

    @Size(max = 50, message = "Category must be at most 50 characters")
    private String category;

}
