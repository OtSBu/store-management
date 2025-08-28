package com.shop.store_management.repository;

import com.shop.store_management.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {
}
