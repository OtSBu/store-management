package com.shop.store_management.mapper;

import com.shop.store_management.model.dtos.ProductDto;
import com.shop.store_management.model.entities.Product;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ProductMapper implements DtoProductMapper<Product, ProductDto> {

    @Override
    public ProductDto toDto(Product entity) {
        if (entity == null) return null;

        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .category(entity.getCategory())
                .build();
    }

    @Override
    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .category(dto.getCategory())
                .build();
    }

    @Override
    public List<ProductDto> toDtoList(Collection<Product> entities) {
        return DtoProductMapper.super.toDtoList(entities);
    }

    @Override
    public List<Product> toEntityList(Collection<ProductDto> dtos) {
        return DtoProductMapper.super.toEntityList(dtos);
    }
}