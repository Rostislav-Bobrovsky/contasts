package com.itechart.contacts.factory;

import com.itechart.contacts.dao.SearchDao;
import com.itechart.contacts.dao.SearchDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchDaoFactory {
    private final static Logger logger = LogManager.getLogger(SearchDaoFactory.class);

    private static volatile SearchDao instance;

    public static SearchDao getInstance() {
        logger.info("{}:{}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

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
