package net.perata.fleet_manager.controller.mapper;

import java.util.List;

public interface EntityMapper<D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    default List<E> toEntity(List<D> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }

    default List<D> toDto(List<E> entityList) {
        return entityList.stream().map(this::toDto).toList();
    }
}
