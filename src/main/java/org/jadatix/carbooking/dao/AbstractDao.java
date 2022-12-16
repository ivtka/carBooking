package org.jadatix.carbooking.dao;

import org.jadatix.carbooking.exception.AccessDeniedException;
import org.jadatix.carbooking.exception.NotFoundException;
import org.jadatix.carbooking.model.IdentifierEntity;
import org.jadatix.carbooking.repository.SpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.jadatix.carbooking.service.AuthenticateUserService.isManager;

@Component
abstract class AbstractDao<T extends IdentifierEntity> implements Dao<T> {

    @Override
    public Optional<T> get(Long id) {
        return getRepository().findOne(getEqualSpecification("id", id).and(getForReadOnly()));
    }

    @Override
    public Page<T> get(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    public List<T> getAll() {
        return getRepository().findAll(getForReadOnly());
    }

    @Override
    public T create(T t) {
        if (isManager()) {
            return getRepository().save(t);
        }
        throw new AccessDeniedException();
    }

    @Override
    public T update(T t) {
        Optional<T> found = getRepository()
                .findOne(getEqualSpecification("id", t.getId()).and(getForModify(t.getId())));
        if (found.isEmpty()) {
            throw new NotFoundException("Not found with id " + t.getId());
        }
        return getRepository().save(t);
    }

    @Override
    public void delete(Long id) {
        Optional<T> found = getRepository().findOne(getEqualSpecification("id", id).and(getForModify(id)));
        getRepository().delete(found.orElseThrow(() -> new NotFoundException("Not found with id " + id)));
    }

    protected abstract SpecificationRepository<T> getRepository();

    protected Specification<T> getEqualSpecification(String fieldName, Object value) {
        return (root, query, cb) -> cb.equal(root.get(fieldName), value);
    }

    protected abstract Specification<T> getForReadOnly();

    protected abstract Specification<T> getForModify(Long id);

}
