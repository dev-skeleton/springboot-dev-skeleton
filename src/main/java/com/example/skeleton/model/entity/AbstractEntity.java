package com.example.skeleton.model.entity;

import org.springframework.data.jpa.domain.Specification;

public interface AbstractEntity<T> {
    Specification<T> countExample();

    Specification<T> uniqueExample();

    T Merge(T that);
}

