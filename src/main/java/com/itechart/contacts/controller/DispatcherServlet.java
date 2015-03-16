package com.itechart.contacts.controller;

import com.itechart.contacts.dao.PeopleDao;
import com.itechart.contacts.dao.SearchDao;
import com.itechart.contacts.factory.PeopleDaoFactory;
import com.itechart.contacts.factory.SearchDaoFactory;
import com.itechart.contacts.job.MailingJobStarter;
import com.itechart.contacts.mail.Mail;
import com.itechart.contacts.model.Address;
import com.itechart.contacts.model.Attachment;
import com.itechart.contacts.model.People;
import com.itechart.contacts.model.SearchDto;
import com.owtelse.codec.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.text.ParseException;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;

@SuppressWarnings("serial")
@MultipartConfig(maxFileSize = 16177215)
public class DispatcherServlet extends HttpServlet {
    private final static int LIMIT_DEFAULT = 10;
    public static final int OFFSET_DEFAULT = 0;
    private final static Logger logger = LogManager.getLogger(DispatcherServlet.class);

    private PeopleDao peopleDao = PeopleDaoFactory.getInstance();
    private SearchDao searchDao = SearchDaoFactory.getInstance();

    private int limit = LIMIT_DEFAULT;
    private int offset = OFFSET_DEFAULT;

    @Override
    public void init() throws ServletException {
        super.init();
        MailingJobStarter.scheduleMailingJob();
    }

    private void setPaginationParameters(HttpServletRequest request) {
        logger.info("{}:{};", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        try {
            limit = Integer.parseInt(request.getParameter("limit"));
            if (!(limit == 10 || limit == 20)) {
                limit = LIMIT_DEFAULT;
            }
        } catch (NumberFormatException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            limit = LIMIT_DEFAULT;
        }
        try {
            offset = Integer.parseInt(request.getParameter("offset"));
            if (offset * limit >= peopleDao.getCountElements()) {
                offset = OFFSET_DEFAULT;
            }
        } catch (NumberFormatException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            offset = OFFSET_DEFAULT;
        }
    }

    private void setAttributes(HttpServletRequest request) {
        logger.info("{}:{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

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
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.info("{}:{}; parameters: {}, {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), request, response);

        String requestURI = request.getRequestURI();
        RequestDispatcher dispatcher = null;
        List<People> peoples = null;

        setPaginationParameters(request);
        String action = request.getParameter("action");
        if (isNotEmpty(action)) {
            doPost(request, response);
        } else {
            action = "";
            switch (action) {
                default:
                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                    peoples = peopleDao.find(limit, offset);
                    request.setAttribute("peoples", peoples);
                    request.setAttribute("all", peopleDao.getCountElements());
            }
            setAttributes(request);
            dispatcher.forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.info("{}:{}; parameters: {}, {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), request, response);

        String requestURI = request.getRequestURI();
        RequestDispatcher dispatcher = null;
        People people = null;
        List<People> peoples = null;

        setPaginationParameters(request);
        String action = lowerCase(request.getParameter("action"));
        switch (action) {
            case "addnew":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/addEdit.jsp");
                request.setAttribute("buttonForm", "Add");

                break;
            case "add":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                people = DispatcherServletHandler.getPeopleFromAddEdit(request);
                setAttachments(request, people);
                peopleDao.create(people);

                peoples = peopleDao.find(limit, offset);
                request.setAttribute("peoples", peoples);
                break;
            case "editexist":
                try {
                    Long idEdit = Long.parseLong(request.getParameter("id"));
                    people = peopleDao.load(idEdit);

                    if (people == null) {
                        dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/addEdit.jsp");
                        request.setAttribute("buttonForm", "Add");
                    } else {
                        dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/addEdit.jsp");
                        request.setAttribute("people", people);
                        request.setAttribute("buttonForm", "Edit");
                        setPhoto(request, people);
                    }
                } catch (NumberFormatException e) {
                    logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                            Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));

                    dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                    peoples = peopleDao.find(limit, offset);
                    request.setAttribute("peoples", peoples);
                }
                break;
            case "edit":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                people = DispatcherServletHandler.getPeopleFromAddEdit(request);
                setAttachments(request, people);
                removeAttachments(request);
                peopleDao.update(people);

                peoples = peopleDao.find(limit, offset);
                request.setAttribute("peoples", peoples);
                break;
            case "remove":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                String idsRemove = request.getParameter("idsRemove");
                if (idsRemove != null) {
                    String[] idsRemoveSplit = idsRemove.split(",");
                    for (String id : idsRemoveSplit) {
                        peopleDao.delete(Long.parseLong(id));
                    }
                }
                peoples = peopleDao.find(limit, offset);
                request.setAttribute("peoples", peoples);
                break;
            case "send":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/send.jsp");
                String idsSend = request.getParameter("idsSend");
                if (isNotEmpty(idsSend)) {
                    String[] idsSendSplit = idsSend.split(",");
                    List<String> emails = searchDao.getSelectEmails(idsSendSplit);

                    request.setAttribute("emails", join(emails, ", "));
                }
                break;
            case "sended":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                String[] emailsSend = split(request.getParameter("to"), ", ");
                peoples = searchDao.getByEmail(emailsSend);
                if ("-None selected-".equals(request.getParameter("templateSelect"))) {
                    Mail.sendMailText(peoples, request.getParameter("subject"), request.getParameter("textareaText"));
                } else {
                    Mail.sendMail(peoples, request.getParameter("subject"), request.getParameter("templateSelect"));
                }
                peoples = peopleDao.find(limit, offset);
                request.setAttribute("peoples", peoples);
                break;
            case "search":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/search.jsp");
                break;
            case "detected":
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                SearchDto searchDto = DispatcherServletHandler.getSearchDto(request);
                Address address = DispatcherServletHandler.getAddress(request);
                searchDto.setAddress(address);

                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                peoples = searchDao.getBySearch(searchDto);
                request.setAttribute("peoples", peoples);
                break;
            default:
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/contactList.jsp");
                peoples = peopleDao.find(limit, offset);
                request.setAttribute("peoples", peoples);
                request.setAttribute("all", peopleDao.getCountElements());
        }
        setAttributes(request);
        dispatcher.forward(request, response);
    }

    private void setPhoto(HttpServletRequest request, People people) throws IOException {
        if (people.getPhoto() != null) {
            byte[] imageBytes = new byte[people.getPhoto().available()];
            people.getPhoto().read(imageBytes);
            String urlImage = "data:image/jpg;base64," + Base64.encode(imageBytes);
            request.setAttribute("uploadImage", urlImage);
        }
    }

    private void setAttachments(HttpServletRequest request, People people) throws IOException, ServletException {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), people);

        List<Attachment> attachments = new ArrayList<>();

        String linesAllAttachments = request.getParameter("linesAllAttachments");
        if (isNotBlank(linesAllAttachments)) {
            String linesAttachments[] = linesAllAttachments.split("/");

            for (String linesAttachment : linesAttachments) {
                setAttachment(request, attachments, linesAttachment);
            }
        }
        people.setAttachments(attachments);
    }

    private void setAttachment(HttpServletRequest request, List<Attachment> attachments, String linesAttachment) throws IOException, ServletException {
        Attachment attachment = new Attachment();

        attachment.setId(Integer.parseInt(request.getParameter("inputAttachmentId-" + linesAttachment)));
        if (attachment.getId() == -1) {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            Part fileAttachmentPart = request.getPart("inputAttachmentFile-" + linesAttachment);
            if (fileAttachmentPart.getSize() == 0) {
                return;
            }
            try {
                inputStream = fileAttachmentPart.getInputStream();

                String generatedName = String.valueOf((new Date()).getTime());
                String relativeWebPath = "/resources/attachments/" + generatedName;
                String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
                File file = new File(absoluteDiskPath);
                outputStream = new FileOutputStream(file);

                int read = 0;
                byte[] bytes = new byte[inputStream.available()];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                attachment.setGeneratedName(generatedName);
                attachment.setOriginalName(request.getParameter("inputAttachmentName-" + linesAttachment));
                attachment.setType(request.getParameter("inputAttachmentType-" + linesAttachment));
                String attachmentDate = request.getParameter("inputAttachmentDate-" + linesAttachment);
                if (isNotBlank(attachmentDate)) {
                    try {
                        Date date = DateUtils.parseDate(attachmentDate, "yyyy-MM-dd");
                        attachment.setUploadDate(date);
                    } catch (ParseException e) {
                        logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                                Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                    }
                }
                attachment.setComment(request.getParameter("inputAttachmentComment-" + linesAttachment));
            } catch (IOException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                System.out.println(Arrays.toString(e.getStackTrace()));
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                                Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                    }
                }
                if (outputStream != null) {
                    try {
                        // outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                                Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                    }
                }
            }
            attachments.add(attachment);
        } else {
            attachment.setOriginalName(request.getParameter("inputAttachmentName-" + linesAttachment));
            attachment.setComment(request.getParameter("inputAttachmentComment-" + linesAttachment));
            attachments.add(attachment);
        }
    }

    private void removeAttachments(HttpServletRequest request) {
        logger.info("{}:{};", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameRemovesFiles = request.getParameter("removesFiles");
        if (isNotEmpty(nameRemovesFiles)) {
            String[] namesFiles = nameRemovesFiles.split("/");

            for (String namesFile : namesFiles) {
                String relativeWebPath = "/resources/attachments/" + namesFile;
                String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
                File file = new File(absoluteDiskPath);
                file.delete();
            }
        }
    }
}