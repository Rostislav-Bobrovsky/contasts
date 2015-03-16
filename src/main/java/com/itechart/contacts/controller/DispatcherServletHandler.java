package com.itechart.contacts.controller;

import com.itechart.contacts.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Created by Rostislav on 13-Mar-15.)
 */
public class DispatcherServletHandler {
    private final static Logger logger = LogManager.getLogger(DispatcherServletHandler.class);

    private DispatcherServletHandler() {
    }

    public static boolean validateCountryCode(String countryCode) {
        Pattern p = Pattern.compile("^([+]?[0-9]+)?$");
        Matcher m = p.matcher(countryCode);
        return m.matches();
    }

    public static boolean validateOperatorCode(String operatorCode) {
        Pattern p = Pattern.compile("^([0-9]+)?$");
        Matcher m = p.matcher(operatorCode);
        return m.matches();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^([0-9])+$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static Address getAddress(HttpServletRequest request) {
        logger.info("{}:{};", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        Address address = new Address();
        String country = request.getParameter("inputCountry");
        if (isNotBlank(country)) {
            address.setCountry(trim(country));
        }
        String city = request.getParameter("inputCity");
        if (isNotBlank(city)) {
            address.setCity(trim(city));
        }
        String street = request.getParameter("inputStreet");
        if (isNotBlank(street)) {
            address.setStreet(trim(street));
        }
        String house = request.getParameter("inputHouse");
        if (isNotBlank(house)) {
            address.setHouse(trim(house));
        }
        String apartment = request.getParameter("inputApartment");
        if (isNotBlank(apartment)) {
            address.setApartment(trim(apartment));
        }
        String index = request.getParameter("inputIndex");
        if (isNotBlank(index)) {
            address.setIndex(trim(index));
        }
        return address;
    }

    public static List<Phone> getPhones(HttpServletRequest request) {
        logger.info("{}:{};", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        List<Phone> phones = new ArrayList<>();
        String idsAllPhones = request.getParameter("idsAllPhones");
        if (isNotBlank(idsAllPhones)) {
            String idsPhone[] = idsAllPhones.split("/");
            for (String anIdsPhone : idsPhone) {
                Phone phone = new Phone();
                phone.setId(anIdsPhone);
                String countryCode = request.getParameter("inputCountryCode-" + anIdsPhone);
                if (validateCountryCode(countryCode)) {
                    phone.setCountryCode(trim(countryCode));
                }
                String operatorCode = request.getParameter("inputOperatorCode-" + anIdsPhone);
                if (validateOperatorCode(operatorCode)) {
                    phone.setOperatorCode(trim(operatorCode));
                }
                String phoneNumber = request.getParameter("inputPhoneNumber-" + anIdsPhone);
                if (validatePhoneNumber(phoneNumber)) {
                    phone.setPhoneNumber(trim(phoneNumber));
                }
                String phoneType = request.getParameter("inputPhoneType-" + anIdsPhone);
                if (isNotBlank(phoneType)) {
                    phone.setPhoneType(PhoneType.convertToPhoneType(phoneType));
                }
                String phoneComment = request.getParameter("inputPhoneComment-" + anIdsPhone);
                if (isNotBlank(phoneComment)) {
                    phone.setComment(trim(phoneComment));
                }
                phones.add(phone);
            }
        }
        return phones;
    }

    public static People getPeopleFromAddEdit(HttpServletRequest request) throws IOException, ServletException {
        logger.info("{}:{};", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        People people = new People();

        String idPeople = request.getParameter("idPeople");
        if (isNotBlank(idPeople)) {
            people.setId(Integer.parseInt(idPeople));
        }
        String firstName = request.getParameter("firstName");
        if (isNotBlank(firstName)) {
            people.setFirstName(trim(firstName));
        }
        String lastName = request.getParameter("lastName");
        if (isNotBlank(lastName)) {
            people.setLastName(trim(lastName));
        }
        String secondName = request.getParameter("secondName");
        if (isNotBlank(secondName)) {
            people.setSecondName(trim(secondName));
        }
        String birthday = request.getParameter("inputDate");
        if (isNotBlank(birthday)) {
            try {
                Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");
                people.setBirthday(date);
            } catch (ParseException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        String sex = request.getParameter("inputSex");
        if (isNotBlank(sex)) {
            people.setSex(Sex.convertToSex(sex));
        }
        String nationality = request.getParameter("inputNationality");
        if (isNotBlank(nationality)) {
            people.setNationality(trim(nationality));
        }
        String relationshipStatus = request.getParameter("relationshipStatus");
        if (isNotBlank(relationshipStatus)) {
            people.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(relationshipStatus));
        }
        String webSite = request.getParameter("inputWebSite");
        if (isNotBlank(webSite)) {
            people.setWebSite(trim(webSite));
        }
        String email = request.getParameter("inputEmail");
        if (isNotBlank(email)) {
                people.setEmail(trim(email));
        }
        String job = request.getParameter("inputJob");
        if (isNotBlank(job)) {
            people.setJob(trim(job));
        }
        Part filePhotoPart = request.getPart("inputPhoto");
        if (filePhotoPart.getSize() != 0) {
            if (StringUtils.contains(filePhotoPart.getContentType(), "image")) {
                people.setPhoto(filePhotoPart.getInputStream());
            }
        }

        List<Phone> phones = getPhones(request);
        people.setPhones(phones);

        Address address = getAddress(request);
        people.setAddress(address);

        return people;
    }

    public static SearchDto getSearchDto(HttpServletRequest request) {
        logger.info("{}:{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        SearchDto searchDto = new SearchDto();

        String firstName = request.getParameter("inputFirstName");
        if (isNotBlank(firstName)) {
            searchDto.setFirstName(trim(firstName));
        }
        String lastName = request.getParameter("inputLastName");
        if (isNotBlank(lastName)) {
            searchDto.setLastName(trim(lastName));
        }
        String secondName = request.getParameter("inputSecondName");
        if (isNotBlank(secondName)) {
            searchDto.setSecondName(trim(secondName));
        }
        String dateFrom = request.getParameter("inputDateFrom");
        if (isNotBlank(dateFrom)) {
            try {
                Date date = DateUtils.parseDate(dateFrom, "yyyy-MM-dd");
                searchDto.setDateFrom(date);
            } catch (ParseException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        String dateTo = request.getParameter("inputDateTo");
        if (isNotBlank(dateTo)) {
            try {
                Date date = DateUtils.parseDate(dateTo, "yyyy-MM-dd");
                searchDto.setDateTo(date);
            } catch (ParseException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        searchDto.setSex(Sex.convertToSex(request.getParameter("inputSex")));
        String nationality = request.getParameter("inputNationality");
        if (isNotBlank(nationality)) {
            searchDto.setNationality(trim(nationality));
        }
        searchDto.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(request.getParameter("relationshipStatus")));
        String webSite = request.getParameter("inputWebSite");
        if (isNotBlank(webSite)) {
            searchDto.setWebSite(trim(webSite));
        }
        String email = request.getParameter("inputEmail");
        if (isNotBlank(email)) {
            searchDto.setEmail(trim(email));
        }
        String job = request.getParameter("inputJob");
        if (isNotBlank(job)) {
            searchDto.setJob(trim(job));
        }
        return searchDto;
    }
}
