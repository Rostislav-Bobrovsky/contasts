package com.itechart.contacts.mail;

/**
 * Created by Rostislav on 10-Mar-15.)
 */
public enum TemplateEnum {
    BIRTHDAY("Birthday"),
    JOB("Job");

    private String displayName;
    private String template;

    TemplateEnum(String displayName) {
        this.displayName = displayName;
        this.template = MailTemplates.loadFromFile(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTemplate() {
        return template;
    }
}
