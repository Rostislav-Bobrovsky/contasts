package com.itechart.contacts.dao.generic;

import java.util.List;

public interface GenericDao<T, PK> {
    void create(T t);

    void delete(PK id);

    T load(PK id);

    T update(T t);

    List<T> getAll(int limit, int offset);
}
