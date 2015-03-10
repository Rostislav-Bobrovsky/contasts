package com.itechart.contacts.controller;

import com.itechart.contacts.dao.PeopleDao;
import com.itechart.contacts.dao.SearchDao;
import com.itechart.contacts.factory.PeopleDaoFactory;
import com.itechart.contacts.factory.SearchDaoFactory;
import com.itechart.contacts.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {

    private PeopleDao peopleDao = PeopleDaoFactory.getInstance();
    private SearchDao searchDao = SearchDaoFactory.getInstance();

    public static boolean isEdit(String requestURI) {
        Pattern p = Pattern.compile("^/contacts/edit/[0-9]+$");
        Matcher m = p.matcher(requestURI);
        return m.matches();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        RequestDispatcher dispatcher = null;
         if (isEdit(requestURI)) {
            requestURI = requestURI.replaceAll("/contacts/edit/", "");
            dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/addEdit.jsp");
            People people = peopleDao.load(Long.parseLong(requestURI));
            request.setAttribute("people", people);
            request.setAttribute("buttonForm", "Edit");
        } else {
            switch (requestURI) {
                case "/contacts/add":
                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/addEdit.jsp");
                    request.setAttribute("buttonForm", "Add");
                    break;
                case "/contacts/search":
                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/search.jsp");
                    break;
                case "/contacts/send":
                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/send.jsp");
                    String idsSend = request.getParameter("idsSend");
                    String[] idsSendSplit = idsSend.split(",");
//                    List<String> emails = searchDao.getSelectEmails(idsSendSplit);

                    List<String> emails = new ArrayList<>();
                    emails.add("rostik.life@yandex.by");
                    emails.add("rostik.life@yandex.ru");

                    request.setAttribute("emails", StringUtils.join(emails, ", "));
                    break;
                case "/contacts/remove":
                    String idsRemove = request.getParameter("idsRemove");
                    String[] idsRemoveSplit = idsRemove.split(",");
                    for (String id : idsRemoveSplit) {
                        peopleDao.delete(Long.parseLong(id));
                    }
                default:
                    if ("Add".equals(request.getParameter("addOrEdit"))) {
                        People people = new People();

                        if (StringUtils.isNotBlank(request.getParameter("firstName"))) {
                            people.setFirstName(StringUtils.trim(request.getParameter("firstName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("lastName"))) {
                            people.setLastName(StringUtils.trim(request.getParameter("lastName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("surName"))) {
                            people.setSurName(StringUtils.trim(request.getParameter("surName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputDate"))) {
                            try {
                                Date date = DateUtils.parseDate(StringUtils.trim(request.getParameter("inputDate")), "yyyy-MM-dd");
                                people.setBirthday(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputSex"))) {
                            people.setSex(Sex.convertToSex(request.getParameter("inputSex")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputNationality"))) {
                            people.setNationality(StringUtils.trim(request.getParameter("inputNationality")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("relationshipStatus"))) {
                            people.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(request.getParameter("relationshipStatus")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputWebSite"))) {
                            people.setWebSite(StringUtils.trim(request.getParameter("inputWebSite")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputEmail"))) {
                            people.setEmail(StringUtils.trim(request.getParameter("inputEmail")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputJob"))) {
                            people.setJob(StringUtils.trim(request.getParameter("inputJob")));
                        }

                        List<Phone> phones = new ArrayList<>();
                        if (StringUtils.isNotBlank(request.getParameter("idsAllPhones"))) {
                            String idsPhone[] = (request.getParameter("idsAllPhones")).split("/");
                            for (String anIdsPhone : idsPhone) {
                                Phone phone = new Phone();
                                if (StringUtils.isNotBlank(request.getParameter("inputCountryCode-" + anIdsPhone))) {
                                    phone.setCountryCode(StringUtils.trim(request.getParameter("inputCountryCode-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputOperatorCode-" + anIdsPhone))) {
                                    phone.setOperatorCode(StringUtils.trim(request.getParameter("inputOperatorCode-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputPhoneNumber-" + anIdsPhone))) {
                                    phone.setPhoneNumber(StringUtils.trim(request.getParameter("inputPhoneNumber-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputPhoneType-" + anIdsPhone))) {
                                    phone.setPhoneType(PhoneType.convertToPhoneType(request.getParameter("inputPhoneType-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputPhoneComment-" + anIdsPhone))) {
                                    phone.setComment(StringUtils.trim(request.getParameter("inputPhoneComment-" + anIdsPhone)));
                                }
                                phones.add(phone);
                            }
                        }
                        people.setPhones(phones);

                        Address address = new Address();
                        if (StringUtils.isNotBlank(request.getParameter("inputCountry"))) {
                            address.setCountry(StringUtils.trim(request.getParameter("inputCountry")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputCity"))) {
                            address.setCity(StringUtils.trim(request.getParameter("inputCity")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputStreet"))) {
                            address.setStreet(StringUtils.trim(request.getParameter("inputStreet")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputHouse"))) {
                            address.setHouse(StringUtils.trim(request.getParameter("inputHouse")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputApartment"))) {
                            address.setApartment(StringUtils.trim(request.getParameter("inputApartment")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputIndex"))) {
                            address.setIndex(StringUtils.trim(request.getParameter("inputIndex")));
                        }
                        people.setAddress(address);

                        peopleDao.create(people);

                    } else if ("Edit".equals(request.getParameter("addOrEdit"))) {
                        People people = new People();

                        people.setId(Integer.parseInt(request.getParameter("idPeople")));
                        if (StringUtils.isNotBlank(request.getParameter("firstName"))) {
                            people.setFirstName(StringUtils.trim(request.getParameter("firstName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("lastName"))) {
                            people.setLastName(StringUtils.trim(request.getParameter("lastName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("surName"))) {
                            people.setSurName(StringUtils.trim(request.getParameter("surName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputDate"))) {
                            try {
                                Date date = DateUtils.parseDate(StringUtils.trim(request.getParameter("inputDate")), "yyyy-MM-dd");
                                people.setBirthday(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputSex"))) {
                            people.setSex(Sex.convertToSex(request.getParameter("inputSex")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputNationality"))) {
                            people.setNationality(StringUtils.trim(request.getParameter("inputNationality")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("relationshipStatus"))) {
                            people.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(request.getParameter("relationshipStatus")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputWebSite"))) {
                            people.setWebSite(StringUtils.trim(request.getParameter("inputWebSite")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputEmail"))) {
                            people.setEmail(StringUtils.trim(request.getParameter("inputEmail")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputJob"))) {
                            people.setJob(StringUtils.trim(request.getParameter("inputJob")));
                        }

                        List<Phone> phones = new ArrayList<>();
                        if (StringUtils.isNotBlank(request.getParameter("idsAllPhones"))) {
                            String idsPhone[] = (request.getParameter("idsAllPhones")).split("/");
                            for (String anIdsPhone : idsPhone) {
                                Phone phone = new Phone();
                                phone.setId(anIdsPhone);
                                if (StringUtils.isNotBlank(request.getParameter("inputCountryCode-" + anIdsPhone))) {
                                    phone.setCountryCode(StringUtils.trim(request.getParameter("inputCountryCode-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputOperatorCode-" + anIdsPhone))) {
                                    phone.setOperatorCode(StringUtils.trim(request.getParameter("inputOperatorCode-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputPhoneNumber-" + anIdsPhone))) {
                                    phone.setPhoneNumber(StringUtils.trim(request.getParameter("inputPhoneNumber-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputPhoneType-" + anIdsPhone))) {
                                    phone.setPhoneType(PhoneType.convertToPhoneType(request.getParameter("inputPhoneType-" + anIdsPhone)));
                                }
                                if (StringUtils.isNotBlank(request.getParameter("inputPhoneComment-" + anIdsPhone))) {
                                    phone.setComment(StringUtils.trim(request.getParameter("inputPhoneComment-" + anIdsPhone)));
                                }
                                phones.add(phone);
                            }
                        }
                        people.setPhones(phones);

                        Address address = new Address();
                        if (StringUtils.isNotBlank(request.getParameter("inputCountry"))) {
                            address.setCountry(StringUtils.trim(request.getParameter("inputCountry")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputCity"))) {
                            address.setCity(StringUtils.trim(request.getParameter("inputCity")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputStreet"))) {
                            address.setStreet(StringUtils.trim(request.getParameter("inputStreet")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputHouse"))) {
                            address.setHouse(StringUtils.trim(request.getParameter("inputHouse")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputApartment"))) {
                            address.setApartment(StringUtils.trim(request.getParameter("inputApartment")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputIndex"))) {
                            address.setIndex(StringUtils.trim(request.getParameter("inputIndex")));
                        }
                        people.setAddress(address);

                        peopleDao.update(people);
                    } else if ("search".equals(request.getParameter("search"))) {
                        SearchDto searchDto = new SearchDto();

                        if (StringUtils.isNotBlank(request.getParameter("inputFirstName"))) {
                            searchDto.setFirstName(StringUtils.trim(request.getParameter("inputFirstName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputLastName"))) {
                            searchDto.setLastName(StringUtils.trim(request.getParameter("inputLastName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputSurName"))) {
                            searchDto.setSurName(StringUtils.trim(request.getParameter("inputSurName")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputDateFrom"))) {
                            try {
                                Date date = DateUtils.parseDate(StringUtils.trim(request.getParameter("inputDateFrom")), "yyyy-MM-dd");
                                searchDto.setDateFrom(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputDateTo"))) {
                            try {
                                Date date = DateUtils.parseDate(StringUtils.trim(request.getParameter("inputDateTo")), "yyyy-MM-dd");
                                searchDto.setDateTo(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputSex"))) {
                            searchDto.setSex(Sex.convertToSex(request.getParameter("inputSex")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputNationality"))) {
                            searchDto.setNationality(StringUtils.trim(request.getParameter("inputNationality")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("relationshipStatus"))) {
                            searchDto.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(request.getParameter("relationshipStatus")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputWebSite"))) {
                            searchDto.setWebSite(StringUtils.trim(request.getParameter("inputWebSite")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputEmail"))) {
                            searchDto.setEmail(StringUtils.trim(request.getParameter("inputEmail")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputJob"))) {
                            searchDto.setJob(StringUtils.trim(request.getParameter("inputJob")));
                        }

                        Address address = new Address();
                        if (StringUtils.isNotBlank(request.getParameter("inputCountry"))) {
                            address.setCountry(StringUtils.trim(request.getParameter("inputCountry")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputCity"))) {
                            address.setCity(StringUtils.trim(request.getParameter("inputCity")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputStreet"))) {
                            address.setStreet(StringUtils.trim(request.getParameter("inputStreet")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputHouse"))) {
                            address.setHouse(StringUtils.trim(request.getParameter("inputHouse")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputApartment"))) {
                            address.setApartment(StringUtils.trim(request.getParameter("inputApartment")));
                        }
                        if (StringUtils.isNotBlank(request.getParameter("inputIndex"))) {
                            address.setIndex(StringUtils.trim(request.getParameter("inputIndex")));
                        }
                        searchDto.setAddress(address);

                        dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                        List<People> peoples = searchDao.getBySearch(searchDto);
                        request.setAttribute("peoples", peoples);
                        break;
                    }

                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                    List<People> peoples = peopleDao.getAll();
                    request.setAttribute("peoples", peoples);
            }
        }

        dispatcher.forward(request, response);
    }
}