package com.shop.store_management.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import com.shop.store_management.model.dtos.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shop.store_management.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("It was requested full product list");
        List<ProductDto> products = productService.getAllProducts();
        log.debug("Response contains {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.info("It was requested product details for ID {}", id);
        ProductDto productDto = productService.getProductById(id);
        log.debug("Product details returned for ID {}: {}", id, productDto);
        return ResponseEntity.ok(productDto);

    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        log.info("It was submitted new product: {}", productDto.getName());
        ProductDto createdProd = productService.createProduct(productDto);
        log.debug("New product saved with ID: {}", createdProd.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProd);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
        log.info("It was requested update for product ID {}", id);
        try {
            ProductDto updatedProd = productService.updateProduct(id, product);
            log.debug("Product update result for ID {}: {}", id, updatedProd);
            return ResponseEntity.ok(updatedProd);
        } catch (EntityNotFoundException ex) {
            log.warn("Product update failed - no product found with ID {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/change-price")
    public ResponseEntity<ProductDto> changePrice(@PathVariable Long id,
                                               @RequestParam BigDecimal newPrice) {
        log.info("Updating price for product ID {} to {} ", id, newPrice);
        ProductDto updatedProd = productService.changePrice(id, newPrice);
        log.debug("Price updated in database for product ID {}: {}", id, updatedProd.getPrice());
        return ResponseEntity.ok(updatedProd);
    }

    @PutMapping("/{id}/change-stock")
    public ResponseEntity<ProductDto> changeStock(@PathVariable Long id,
                                                  @RequestParam Integer newStockQuantity) {
        log.info("It was requested stock update for product ID {} to {}", id, newStockQuantity);
        ProductDto updatedProd = productService.changeStockQuantity(id, newStockQuantity);
        log.debug("Stock update result for product ID {}: {}", id, updatedProd);
        return  ResponseEntity.ok(updatedProd);
    }

    @PutMapping("/{id}/change-category")
    public ResponseEntity<ProductDto> changeCategory(@PathVariable Long id,
                                                  @RequestParam String newCategory) {
        log.info("Client requested category change for product ID {} to '{}'", id, newCategory);
        ProductDto updatedProd = productService.changeCategory(id, newCategory);
        log.debug("Category update result for product ID {}: {}", id, updatedProd);
        return  ResponseEntity.ok(updatedProd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("It was requested deletion of product with ID {}", id);
        productService.deleteProduct(id);
        log.debug("Deletion confirmed for product ID {}", id);
        return ResponseEntity.noContent().build();
    }

}
