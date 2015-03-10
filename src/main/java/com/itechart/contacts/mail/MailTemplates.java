package com.itechart.contacts.mail;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostislav on 09-Mar-15.)
 */
public class MailTemplates {
    public static List<String> loadFromFile() {
        STGroup group = new STGroupFile("template.stg");

        List<String> templates = new ArrayList<>();
        for (TemplateEnum template : TemplateEnum.values()) {
            ST birthday = group.getInstanceOf(template.getDisplayName());
            birthday.add("name", "$Name");
            templates.add(birthday.render());
        }

        return templates;
    }

    public static String loadFromFile(String templateName) {
        STGroup group = new STGroupFile("template.stg");

        ST birthday = group.getInstanceOf(templateName);
        birthday.add("name", "$Name");

        return birthday.render();
    }
}
