package org.jadatix.carbooking.service;

import org.jadatix.carbooking.model.IdentifierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

interface EntityService<T extends IdentifierEntity> {

    List<T> getAll();

    T get(Long id);

    Page<T> get(Pageable pageable);

    T create(T t);

    void delete(Long id);

    T update(T t);

}