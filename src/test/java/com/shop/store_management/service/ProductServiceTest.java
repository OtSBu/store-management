package com.shop.store_management.service;

import com.shop.store_management.exception.ResourceNotFoundException;
import com.shop.store_management.exception.UnauthorizedAccessException;
import com.shop.store_management.mapper.ProductMapper;
import com.shop.store_management.model.dtos.ProductDto;
import com.shop.store_management.model.entities.Product;
import com.shop.store_management.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setStockQuantity(10);
        product.setCategory("Electronics");

        productDto = new ProductDto();
        productDto.setName("Laptop");
        productDto.setPrice(BigDecimal.valueOf(1000));
        productDto.setStockQuantity(10);
        productDto.setCategory("Electronics");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testUser", null, List.of())
        );
    }

    @Test
    void testCreateProduct_success() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.createProduct(productDto);

        assertEquals("Laptop", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    void testChangePrice_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(productMapper.toDto(any())).thenReturn(productDto);

        ProductDto result = productService.changePrice(1L, BigDecimal.valueOf(1200));

        assertEquals("Laptop", result.getName());
        assertEquals(BigDecimal.valueOf(1200), product.getPrice());
        verify(productRepository).save(product);
    }

    @Test
    void testChangePrice_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.changePrice(99L, BigDecimal.valueOf(1200)));
    }

    @Test
    void testGetAllProducts_success() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDtoList(any())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void testGetProductById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(1L);

        assertEquals("Laptop", result.getName());
    }

    @Test
    void testGetProductById_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void testUpdateProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(productDto);

        ProductDto result = productService.updateProduct(1L, productDto);

        assertEquals("Laptop", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProduct_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(99L, productDto));
    }

    @Test
    void testChangeStockQuantity_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(productDto);

        ProductDto result = productService.changeStockQuantity(1L, 50);

        assertEquals("Laptop", result.getName());
        assertEquals(50, product.getStockQuantity());
    }

    @Test
    void testChangeStockQuantity_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.changeStockQuantity(99L, 50));
    }

    @Test
    void testChangeCategory_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(productDto);

        ProductDto result = productService.changeCategory(1L, "New Category");

        assertEquals("Laptop", result.getName());
        assertEquals("New Category", product.getCategory());
    }

    @Test
    void testChangeCategory_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.changeCategory(99L, "New Category"));
    }

    @Test
    void testDeleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProduct_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(99L));
    }

    @Test
    void testCreateProduct_unauthenticated() {
        SecurityContextHolder.clearContext();

        assertThrows(UnauthorizedAccessException.class, () -> productService.createProduct(productDto));
    }
}