package com.shop.store_management.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface DtoProductMapper<Entity, Dto> {

    Dto toDto(Entity entity);

    Entity toEntity(Dto dto);

    default List<Dto> toDtoList(Collection<Entity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<Entity> toEntityList(Collection<Dto> entities) {
        return entities.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
