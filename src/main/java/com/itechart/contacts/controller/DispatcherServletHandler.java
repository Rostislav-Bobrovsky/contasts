package com.itechart.contacts.controller;

import com.itechart.contacts.model.*;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Created by Rostislav on 13-Mar-15.)
 */
public class DispatcherServletHandler {
    private DispatcherServletHandler() {
    }

    public static Address getAddress(HttpServletRequest request) {
        Address address = new Address();
        if (isNotBlank(request.getParameter("inputCountry"))) {
            address.setCountry(trim(request.getParameter("inputCountry")));
        }
        if (isNotBlank(request.getParameter("inputCity"))) {
            address.setCity(trim(request.getParameter("inputCity")));
        }
        if (isNotBlank(request.getParameter("inputStreet"))) {
            address.setStreet(trim(request.getParameter("inputStreet")));
        }
        if (isNotBlank(request.getParameter("inputHouse"))) {
            address.setHouse(trim(request.getParameter("inputHouse")));
        }
        if (isNotBlank(request.getParameter("inputApartment"))) {
            address.setApartment(trim(request.getParameter("inputApartment")));
        }
        if (isNotBlank(request.getParameter("inputIndex"))) {
            address.setIndex(trim(request.getParameter("inputIndex")));
        }
        return address;
    }

    public static List<Phone> getPhones(HttpServletRequest request) {
        List<Phone> phones = new ArrayList<>();
        if (isNotBlank(request.getParameter("idsAllPhones"))) {
            String idsPhone[] = (request.getParameter("idsAllPhones")).split("/");
            for (String anIdsPhone : idsPhone) {
                Phone phone = new Phone();
                phone.setId(anIdsPhone);
                if (isNotBlank(request.getParameter("inputCountryCode-" + anIdsPhone))) {
                    phone.setCountryCode(trim(request.getParameter("inputCountryCode-" + anIdsPhone)));
                }
                if (isNotBlank(request.getParameter("inputOperatorCode-" + anIdsPhone))) {
                    phone.setOperatorCode(trim(request.getParameter("inputOperatorCode-" + anIdsPhone)));
                }
                if (isNotBlank(request.getParameter("inputPhoneNumber-" + anIdsPhone))) {
                    phone.setPhoneNumber(trim(request.getParameter("inputPhoneNumber-" + anIdsPhone)));
                }
                if (isNotBlank(request.getParameter("inputPhoneType-" + anIdsPhone))) {
                    phone.setPhoneType(PhoneType.convertToPhoneType(request.getParameter("inputPhoneType-" + anIdsPhone)));
                }
                if (isNotBlank(request.getParameter("inputPhoneComment-" + anIdsPhone))) {
                    phone.setComment(trim(request.getParameter("inputPhoneComment-" + anIdsPhone)));
                }
                phones.add(phone);
            }
        }
        return phones;
    }

    public static People getPeopleFromAddEdit(HttpServletRequest request) throws IOException, ServletException {
        People people = new People();

        if (isNotBlank(request.getParameter("idPeople"))) {
            people.setId(Integer.parseInt(request.getParameter("idPeople")));
        }
        if (isNotBlank(request.getParameter("firstName"))) {
            people.setFirstName(trim(request.getParameter("firstName")));
        }
        if (isNotBlank(request.getParameter("lastName"))) {
            people.setLastName(trim(request.getParameter("lastName")));
        }
        if (isNotBlank(request.getParameter("surName"))) {
            people.setSurName(trim(request.getParameter("surName")));
        }
        if (isNotBlank(request.getParameter("inputDate"))) {
            try {
                Date date = DateUtils.parseDate(request.getParameter("inputDate"), "yyyy-MM-dd");
                people.setBirthday(date);
            } catch (ParseException e) {
//                logger.error(Level.INFO, e);
            }
        }
        if (isNotBlank(request.getParameter("inputSex"))) {
            people.setSex(Sex.convertToSex(request.getParameter("inputSex")));
        }
        if (isNotBlank(request.getParameter("inputNationality"))) {
            people.setNationality(trim(request.getParameter("inputNationality")));
        }
        if (isNotBlank(request.getParameter("relationshipStatus"))) {
            people.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(request.getParameter("relationshipStatus")));
        }
        if (isNotBlank(request.getParameter("inputWebSite"))) {
            people.setWebSite(trim(request.getParameter("inputWebSite")));
        }
        if (isNotBlank(request.getParameter("inputEmail"))) {
            people.setEmail(trim(request.getParameter("inputEmail")));
        }
        if (isNotBlank(request.getParameter("inputJob"))) {
            people.setJob(trim(request.getParameter("inputJob")));
        }
        if ("true".equals(request.getParameter("isUploadImage"))) {
            Part filePart = request.getPart("inputPhoto");
            if (filePart.getSize() != 0) {
                people.setPhoto(filePart.getInputStream());
            }
        }

        List<Phone> phones = getPhones(request);
        people.setPhones(phones);

        Address address = getAddress(request);
        people.setAddress(address);

        return people;
    }
}
