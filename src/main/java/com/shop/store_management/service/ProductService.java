package com.shop.store_management.service;

import com.shop.store_management.exception.ResourceNotFoundException;
import com.shop.store_management.exception.UnauthorizedAccessException;
import com.shop.store_management.mapper.ProductMapper;
import com.shop.store_management.model.dtos.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import com.shop.store_management.model.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.shop.store_management.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ProductService {


    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDto createProduct(ProductDto productDto) {
        String username = getLoggedUsername();

        log.info("User '{}' is creating product: {}", username, productDto.getName());
        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        log.debug("Product saved with ID: {}", saved.getId());
        return productMapper.toDto(saved);

    }

    public ProductDto changePrice(Long productId, BigDecimal newPrice) {
        log.info("Changing price for product ID {} to {}", productId, newPrice);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for id" + productId));
        log.warn("Product not found for ID {}", productId);
        product.setPrice(newPrice);
        Product updated = productRepository.save(product);
        log.debug("Price updated for product ID {}: {}", productId, updated.getPrice());
        return productMapper.toDto(updated);
    }

    public List<ProductDto> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.debug("Found {} products", products.size());
        return productMapper.toDtoList(products);
    }

    public ProductDto getProductById(Long id) {
        log.info("Attempting to retrieve product with ID {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for ID {}", id);
                    return new ResourceNotFoundException("Product not found for id " + id);
                });

        log.debug("Product retrieved successfully: {}", product);
        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long id, ProductDto updatedProductDto) {
        String username = getLoggedUsername();

        log.info("User '{}' is attempting to update product with ID {}", username, id);

        Product existedProd = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for ID {}", id);
                    throw new EntityNotFoundException("Product not found");
                });

        log.debug("Existing product before update: {}", existedProd);

        existedProd.setName(updatedProductDto.getName());
        existedProd.setDescription(updatedProductDto.getDescription());
        existedProd.setPrice(updatedProductDto.getPrice());
        existedProd.setStockQuantity(updatedProductDto.getStockQuantity());
        existedProd.setCategory(updatedProductDto.getCategory());

        Product saved = productRepository.save(existedProd);
        log.debug("Product updated: {}", saved);
        return productMapper.toDto(saved);

    }

    public ProductDto changeStockQuantity(Long id, int newQuantity) {
        log.info("Attempting to change stock quantity for product ID {} to {}", id, newQuantity);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for ID {}", id);
                    return new ResourceNotFoundException("Product not found for id " + id);
                });

        int oldQuantity = product.getStockQuantity();
        product.setStockQuantity(newQuantity);
        Product updated = productRepository.save(product);

        log.debug("Stock quantity changed for product ID {}: {} → {}", id, oldQuantity, newQuantity);
        return productMapper.toDto(updated);
    }

    public ProductDto changeCategory(Long id, String newCategory) {
        log.info("Attempting to change category for product ID {} to '{}'", id, newCategory);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for ID {}", id);
                    return new ResourceNotFoundException("Product not found for id " + id);
                });

        String oldCategory = product.getCategory();
        product.setCategory(newCategory);
        Product updated = productRepository.save(product);

        log.debug("Category changed for product ID {}: '{}' → '{}'", id, oldCategory, newCategory);
        return productMapper.toDto(updated);
    }


    public void deleteProduct(Long id) {
        String username = getLoggedUsername();

        log.info("User '{}' is attempting to delete product with ID {}", username, id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for id " + id));

        productRepository.delete(product);
        log.debug("Product with ID {} deleted by user '{}'", id, username);
    }

    private String getLoggedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return principal.toString();
    }
}
