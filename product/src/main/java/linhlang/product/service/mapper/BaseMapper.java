package linhlang.product.service.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<E, R> {

    R toModel(E e);

    List<R> toModel(List<E> e);

    E toEntity(R r);

    List<E> toEntity(List<R> r);

    void update(@MappingTarget E e, R model);
}
