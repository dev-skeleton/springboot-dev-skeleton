package com.example.skeleton.service;

import com.example.skeleton.exception.ClientSideException;
import com.example.skeleton.exception.ServerSideException;
import com.example.skeleton.model.entity.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Transactional
public class CrudSvc<E extends AbstractEntity<E>, I> {

    private final JpaRepository<E, I> repo;
    private final JpaSpecificationExecutor<E> jpaSpecificationExecutor;

    public CrudSvc(JpaRepository<E, I> repo, JpaSpecificationExecutor<E> specificationExecutor) {
        this.repo = repo;
        this.jpaSpecificationExecutor = specificationExecutor;
    }

    public void Create(E entity) {
        this.repo.saveAndFlush(entity);
    }

    public void Merge(E entity) {
        if (this.Exist(entity)) {
            this.Update(this.MustGet(entity).Merge(entity));
        } else {
            this.Create(entity);
        }
    }

    public E MustGet(E entity) {
        var found = this.jpaSpecificationExecutor.findAll(entity.uniqueExample());
        if (found.size() != 1) {
            throw ServerSideException.InternalError("data integrity convention corrupted");
        }
        return found.get(0);
    }

    public E GetByID(I id) {
        try {
            return repo.getOne(id);
        } catch (EntityNotFoundException e) {
            throw ClientSideException.NotFound(e.getMessage()).CausedBy(e);
        } catch (Exception e) {
            throw ServerSideException.InternalError(e.getMessage()).CausedBy(e);
        }
    }

    public E TryGetByID(I id) {
        try {
            return repo.getOne(id);
        } catch (EntityNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw ServerSideException.InternalError(e.getMessage()).CausedBy(e);
        }
    }

    public long Count(E entity) {
        return this.jpaSpecificationExecutor.count(entity.countExample());
    }

    public boolean Exist(E entity) {
        return this.jpaSpecificationExecutor.count(entity.uniqueExample()) == 1;
    }

    public E Refresh(E entity) {
        return MustGet(entity);
    }

    public List<E> List() {
        return this.repo.findAll();
    }

    public List<E> List(Specification<E> spec) {
        return this.jpaSpecificationExecutor.findAll(spec);
    }


    public void DeleteById(I id) {
        this.repo.deleteById(id);
    }

    public void Update(E entity) {
        this.repo.save(entity);
    }

    public List<E> ListAll() {
        return this.repo.findAll();
    }
}
