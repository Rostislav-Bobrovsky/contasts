package com.itechart.contacts.factory;

import com.itechart.contacts.dao.PeopleDao;
import com.itechart.contacts.dao.PeopleDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public class PeopleDaoFactory {
    private final static Logger logger = LogManager.getLogger(PeopleDaoFactory.class);

    private static volatile PeopleDao instance;

    public static PeopleDao getInstance() {
        logger.info("{}:{}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

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
