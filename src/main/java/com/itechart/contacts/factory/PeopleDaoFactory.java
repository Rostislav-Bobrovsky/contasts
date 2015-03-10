package com.itechart.contacts.factory;

import com.itechart.contacts.dao.PeopleDao;
import com.itechart.contacts.dao.PeopleDaoImpl;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public class PeopleDaoFactory {
    private static volatile PeopleDao instance;

    public static PeopleDao getInstance() {
        PeopleDao localInstance = instance;
        if (localInstance == null) {
            synchronized (PeopleDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PeopleDaoImpl();
                }
            }
        }
        return localInstance;
    }
}
