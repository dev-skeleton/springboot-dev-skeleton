package com.example.skeleton.model.entity;

import com.example.skeleton.exception.ServerSideException;
import com.example.skeleton.util.Reflect;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotNull;
import java.util.List;

public abstract class EntityUtil {

    static public <E> Specification<E> Equal(E e, String attr) {
        try {
            if (e == null) {
                return null;
            }
            var f = Reflect.GetFieldValue(e, attr);
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attr), f);
        } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
            throw ServerSideException.InternalError(noSuchFieldException.getMessage()).CausedBy(noSuchFieldException);
        }
    }


    static public <E> Specification<E> Equal(E e, String attr, Object val) {
        if (e == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attr), val);
    }


    static public <E> Specification<E> NotEqual(E e, String attr) {
        try {
            if (e == null) {
                return null;
            }
            var f = Reflect.GetFieldValue(e, attr);
            return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(attr), f);
        } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
            throw ServerSideException.InternalError(noSuchFieldException.getMessage()).CausedBy(noSuchFieldException);
        }
    }

    static public <E> Specification<E> NotEqual(E e, String attr, Object val) {
        if (e == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(attr), val);
    }


    static public <E, V extends Comparable<V>> Specification<E> Between(E e, String attr, @NotNull V start, @NotNull V end) {
        if (e == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attr), start, end);
    }

    static public <E> Specification<E> And(List<Specification<E>> specifications) {
        if (specifications == null || specifications.size() == 0) {
            return null;
        }
        var spec = Specification.where(specifications.get(0));
        if (specifications.size() == 1) {
            return spec;
        }
        for (int i = 1; i < specifications.size(); i++) {
            spec = spec.and(specifications.get(i));
        }
        return spec;
    }
}
