package linhlang.webconfig.service.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<E, M> {

    M toModel(E e);

    List<M> toModel(List<E> e);

    E toEntity(M m);

    List<E> toEntity(List<M> m);

    void update(@MappingTarget E e, M model);
}
