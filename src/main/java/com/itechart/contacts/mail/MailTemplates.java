package com.itechart.contacts.mail;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Created by Rostislav on 09-Mar-15.)
 */
public class MailTemplates {
    public static String loadFromFile(String templateName) {
        STGroup group = new STGroupFile("template.stg");

        ST textTemplate = group.getInstanceOf(templateName);
        textTemplate.add("name", "$Name$");

        return textTemplate.render();
    }

    public static String loadFromFile(String templateName, String name) {
        STGroup group = new STGroupFile("template.stg");

        ST textTemplate = group.getInstanceOf(templateName);
        textTemplate.add("name", name);

        return textTemplate.render();
    }
}
