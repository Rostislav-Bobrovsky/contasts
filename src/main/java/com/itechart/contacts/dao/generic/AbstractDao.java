package com.itechart.contacts.dao.generic;

import java.util.List;

public abstract class AbstractDao<T> implements GenericDao<T, Long> {
    public abstract void create(T t);

    public abstract void delete(Long id);

    public abstract T load(Long id);

    public abstract T update(T t);

    public abstract List<T> getAll();
}
