package com.itechart.contacts.controller;

import com.itechart.contacts.job.MailingJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;

/**
 * Created by Rostislav on 13-Mar-15.)
 */
public class MailingJobStarter {
    public static void scheduleMailingJob() {
        try {
            JobDetail job = JobBuilder.newJob(MailingJob.class).build();

            CronTriggerImpl cronTrigger = new CronTriggerImpl();
            cronTrigger.setName("mailingTrigger");
            cronTrigger.setCronExpression("0 1 * * * ?");

            SchedulerFactory schFactory = new StdSchedulerFactory();
            Scheduler scheduler = schFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, cronTrigger);
        } catch (ParseException | SchedulerException e) {
            e.printStackTrace();
        }
    }
}
