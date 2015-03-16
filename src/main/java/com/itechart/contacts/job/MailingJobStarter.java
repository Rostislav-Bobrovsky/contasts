package com.itechart.contacts.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by Rostislav on 13-Mar-15.)
 */
public class MailingJobStarter {
    private final static Logger logger = LogManager.getLogger(MailingJobStarter.class);

    public static void scheduleMailingJob() {
        try {
            JobDetail job = JobBuilder.newJob(MailingJob.class).build();

            CronTriggerImpl cronTrigger = new CronTriggerImpl();
            cronTrigger.setName("mailingTrigger");
            cronTrigger.setCronExpression("00 00 9 * * ?");

            SchedulerFactory schFactory = new StdSchedulerFactory();
            Scheduler scheduler = schFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, cronTrigger);
        } catch (ParseException | SchedulerException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }
}
