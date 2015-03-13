package com.itechart.contacts.controller;

import com.itechart.contacts.dao.PeopleDao;
import com.itechart.contacts.dao.SearchDao;
import com.itechart.contacts.factory.PeopleDaoFactory;
import com.itechart.contacts.factory.SearchDaoFactory;
import com.itechart.contacts.mail.Mail;
import com.itechart.contacts.model.*;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;

@SuppressWarnings("serial")
@MultipartConfig(maxFileSize = 16177215)
public class DispatcherServlet extends HttpServlet {
    private final static int LIMIT_DEFAULT = 10;
    public static final int OFFSET_DEFAULT = 0;
    private final static Logger logger = LogManager.getLogger(DispatcherServlet.class);

    private PeopleDao peopleDao = PeopleDaoFactory.getInstance();
    private SearchDao searchDao = SearchDaoFactory.getInstance();

    @Override
    public void init() throws ServletException {
        super.init();
        MailingJobStarter.scheduleMailingJob();
    }

    private static boolean isEdit(String requestURI) {
        Pattern p = Pattern.compile("^/contacts/edit/[0-9]+$");
        Matcher m = p.matcher(requestURI);
        return m.matches();
    }

    private static boolean isContacts(String requestURI) {
        Pattern p = Pattern.compile("^/contacts/[0-9]+$");
        Matcher m = p.matcher(requestURI);
        return m.matches();
    }

    private void setAttributes(HttpServletRequest request, List<People> peoples, int limit, int offset) {
        int offsetLift;
        int offsetRight;
        if (offset == 0) {
            offsetLift = 0;
        } else {
            offsetLift = offset - 1;
        }
        if ((offset + 1) * limit < peopleDao.getCountElements()) {
            offsetRight = offset + 1;
        } else {
            offsetRight = offset;
        }

        request.setAttribute("offset", offset);
        request.setAttribute("offsetLeft", offsetLift);
        request.setAttribute("offsetRight", offsetRight);
        request.setAttribute("limit", limit);
        request.setAttribute("tablePage", offset + 1);
        request.setAttribute("peoples", peoples);
    }

    private SearchDto getSearchDto(HttpServletRequest request) {
        SearchDto searchDto = new SearchDto();

        if (isNotBlank(request.getParameter("inputFirstName"))) {
            searchDto.setFirstName(trim(request.getParameter("inputFirstName")));
        }
        if (isNotBlank(request.getParameter("inputLastName"))) {
            searchDto.setLastName(trim(request.getParameter("inputLastName")));
        }
        if (isNotBlank(request.getParameter("inputSurName"))) {
            searchDto.setSurName(trim(request.getParameter("inputSurName")));
        }
        if (isNotBlank(request.getParameter("inputDateFrom"))) {
            try {
                Date date = DateUtils.parseDate(request.getParameter("inputDateFrom"), "yyyy-MM-dd");
                searchDto.setDateFrom(date);
            } catch (ParseException e) {
                logger.error(Level.INFO, e);
            }
        }
        if (isNotBlank(request.getParameter("inputDateTo"))) {
            try {
                Date date = DateUtils.parseDate(request.getParameter("inputDateTo"), "yyyy-MM-dd");
                searchDto.setDateTo(date);
            } catch (ParseException e) {
                logger.error(Level.INFO, e);
            }
        }
        searchDto.setSex(Sex.convertToSex(request.getParameter("inputSex")));
        if (isNotBlank(request.getParameter("inputNationality"))) {
            searchDto.setNationality(trim(request.getParameter("inputNationality")));
        }
        searchDto.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(request.getParameter("relationshipStatus")));
        if (isNotBlank(request.getParameter("inputWebSite"))) {
            searchDto.setWebSite(trim(request.getParameter("inputWebSite")));
        }
        if (isNotBlank(request.getParameter("inputEmail"))) {
            searchDto.setEmail(trim(request.getParameter("inputEmail")));
        }
        if (isNotBlank(request.getParameter("inputJob"))) {
            searchDto.setJob(trim(request.getParameter("inputJob")));
        }
        return searchDto;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
//        request.setCharacterEncoding("UTF-8");

        String requestURI = request.getRequestURI();

        if (isContacts(requestURI)) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
            List<People> peoples;
            int limit, offset;
            if (isEmpty(request.getParameter("limit"))) {
                limit = LIMIT_DEFAULT;
                offset = OFFSET_DEFAULT;
                peoples = peopleDao.getAll(limit, offset);
            } else {
                peoples = peopleDao.getAll(Integer.parseInt(request.getParameter("limit")),
                        Integer.parseInt(request.getParameter("offset")));

                offset = Integer.parseInt(request.getParameter("offset"));
                limit = Integer.parseInt(request.getParameter("limit"));
            }

            setAttributes(request, peoples, limit, offset);
            dispatcher.forward(request, response);
        } else {
            doPost(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
//        request.setCharacterEncoding("UTF-8");

        String requestURI = request.getRequestURI();
        RequestDispatcher dispatcher = null;

        if (isEdit(requestURI)) {
            requestURI = requestURI.replaceAll("/contacts/edit/", "");
            dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/addEdit.jsp");
            People people = peopleDao.load(Long.parseLong(requestURI));
            request.setAttribute("people", people);
            request.setAttribute("buttonForm", "Edit");

            if (people.getPhoto() != null) {
                byte[] imageBytes = new byte[people.getPhoto().available()];
                people.getPhoto().read(imageBytes);
                String urlImage = "data:image/jpg;base64," + Base64.encode(imageBytes);
                request.setAttribute("uploadImage", urlImage);
            }
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
                    List<String> emails = searchDao.getSelectEmails(idsSendSplit);

                    request.setAttribute("emails", join(emails, ", "));
                    break;
                case "/contacts/remove":
                    String idsRemove = request.getParameter("idsRemove");
                    String[] idsRemoveSplit = idsRemove.split(",");
                    for (String id : idsRemoveSplit) {
                        peopleDao.delete(Long.parseLong(id));
                    }
                default:
                    if ("Add".equals(request.getParameter("addOrEdit"))) {
                        People people = DispatcherServletHandler.getPeopleFromAddEdit(request);

                        peopleDao.create(people);
                    } else if ("Edit".equals(request.getParameter("addOrEdit"))) {
                        People people = DispatcherServletHandler.getPeopleFromAddEdit(request);

                        peopleDao.update(people);
                    } else if ("search".equals(request.getParameter("search"))) {
                        SearchDto searchDto = getSearchDto(request);
                        Address address = DispatcherServletHandler.getAddress(request);
                        searchDto.setAddress(address);

                        dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                        List<People> peoples = searchDao.getBySearch(searchDto);
                        setAttributes(request, peoples, LIMIT_DEFAULT, OFFSET_DEFAULT);
                        break;
                    } else if (isNotEmpty(request.getParameter("to"))) {
                        String[] emailsSend = split(request.getParameter("to"), ", ");
                        List<People> peoples = searchDao.getByEmail(emailsSend);
                        if ("-None selected-".equals(request.getParameter("templateSelect"))) {
                            Mail.sendMailText(peoples, request.getParameter("subject"), request.getParameter("textareaText"));
                        } else {
                            Mail.sendMail(peoples, request.getParameter("subject"), request.getParameter("templateSelect"));
                        }
                    }
                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                    List<People> peoples = peopleDao.getAll(LIMIT_DEFAULT, OFFSET_DEFAULT);
                    setAttributes(request, peoples, LIMIT_DEFAULT, OFFSET_DEFAULT);

            }
        }
        dispatcher.forward(request, response);
    }
}