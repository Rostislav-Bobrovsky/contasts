package com.itechart.contacts.job;

import com.itechart.contacts.dao.SearchDao;
import com.itechart.contacts.factory.SearchDaoFactory;
import com.itechart.contacts.mail.Mail;
import com.itechart.contacts.model.People;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by Rostislav on 13-Mar-15.)
 */
public class MailingJob implements Job {
    private final static Logger logger = LogManager.getLogger(MailingJob.class);

    private SearchDao searchDao = SearchDaoFactory.getInstance();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("{}:{}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        Date date = new java.util.Date();
        List<People> peoples = searchDao.getByBirthday(date);
        Mail.sendMail(peoples, "Birthday", "Birthday");
    }
}


