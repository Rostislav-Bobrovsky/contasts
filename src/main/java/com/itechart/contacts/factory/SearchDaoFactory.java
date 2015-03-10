package com.itechart.contacts.factory;

import com.itechart.contacts.dao.SearchDao;
import com.itechart.contacts.dao.SearchDaoImpl;

public class SearchDaoFactory {
    private static volatile SearchDao instance;

    public static SearchDao getInstance() {
        SearchDao localInstance = instance;
        if (localInstance == null) {
            synchronized (SearchDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SearchDaoImpl();
                }
            }
        }
        return localInstance;
    }
}
