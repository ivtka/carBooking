package org.jadatix.carbooking.dao;

import org.jadatix.carbooking.model.IdentifierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends IdentifierEntity> {

    Optional<T> get(Long id);

    List<T> getAll();

    Page<T> get(Pageable pageable);

    T create(T t);

    T update(T t);

    void delete(Long id);

}