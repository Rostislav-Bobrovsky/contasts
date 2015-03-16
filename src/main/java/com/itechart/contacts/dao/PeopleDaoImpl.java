package com.itechart.contacts.dao;

import com.itechart.contacts.dao.generic.AbstractDao;
import com.itechart.contacts.dao.generic.ConnectionFactory;
import com.itechart.contacts.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public class PeopleDaoImpl extends AbstractDao<People> implements PeopleDao {
    private final static Logger logger = LogManager.getLogger(PeopleDaoImpl.class);

    private void preparedStatementSetPeopleAttributes(People people, PreparedStatement preparedStatement) throws SQLException {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), people);

        preparedStatement.setString(1, people.getFirstName());
        preparedStatement.setString(2, people.getLastName());
        preparedStatement.setString(3, people.getSecondName());
        if (people.getBirthday() != null) {
            java.util.Date birthday = getDateWithoutTime(people.getBirthday());
            preparedStatement.setTimestamp(4, new Timestamp(birthday.getTime()));
        } else {
            preparedStatement.setTimestamp(4, null);
        }
        preparedStatement.setString(5, Sex.convertToString(people.getSex()));
        preparedStatement.setString(6, people.getNationality());
        preparedStatement.setString(7, RelationshipStatus.convertToString(people.getRelationshipStatus()));
        preparedStatement.setString(8, people.getWebSite());
        preparedStatement.setString(9, people.getEmail());
        preparedStatement.setString(10, people.getJob());
    }

    @Override
    public void create(People people) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), people);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO people(first_name, last_name, second_name, birth_date, sex, nationality, relationship_status, web_site, email, job, photo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatementSetPeopleAttributes(people, preparedStatement);
            if (people.getPhoto() != null) {
                preparedStatement.setBlob(11, people.getPhoto());
            } else {
                preparedStatement.setNull(11, Types.BLOB);
            }
            preparedStatement.executeUpdate();

            ResultSet tableKeys = preparedStatement.getGeneratedKeys();
            tableKeys.next();
            int idPeople = tableKeys.getInt(1);

            preparedStatement = connection.prepareStatement("INSERT INTO address(people_id, country, city, street, house, apartment, `index`) " +
                    "VALUE (?, ?, ?, ?, ?, ?, ?)");
            Address address = people.getAddress();
            preparedStatement.setInt(1, idPeople);
            preparedStatement.setString(2, address.getCountry());
            preparedStatement.setString(3, address.getCity());
            preparedStatement.setString(4, address.getStreet());
            preparedStatement.setString(5, address.getHouse());
            preparedStatement.setString(6, address.getApartment());
            preparedStatement.setString(7, address.getIndex());
            preparedStatement.executeUpdate();

            for (Phone phone : people.getPhones()) {
                preparedStatement = connection.prepareStatement("INSERT INTO phone(people_id, country_code, operator_code, phone_number, phone_type, comment) " +
                        "VALUE (?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, idPeople);
                preparedStatement.setString(2, phone.getCountryCode());
                preparedStatement.setString(3, phone.getOperatorCode());
                preparedStatement.setString(4, phone.getPhoneNumber());
                preparedStatement.setString(5, PhoneType.convertToString(phone.getPhoneType()));
                preparedStatement.setString(6, phone.getComment());
                preparedStatement.executeUpdate();
            }

            for (Attachment attachment : people.getAttachments()) {
                preparedStatement = connection.prepareStatement("INSERT INTO attachment(people_id, generated_name, original_name, type, upload_date, comment) " +
                        "VALUE (?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, idPeople);
                preparedStatement.setString(2, attachment.getGeneratedName());
                preparedStatement.setString(3, attachment.getOriginalName());
                preparedStatement.setString(4, attachment.getType());
                if (attachment.getUploadDate() != null) {
                    java.util.Date uploadDate = getDateWithoutTime(attachment.getUploadDate());
                    preparedStatement.setTimestamp(5, new Timestamp(uploadDate.getTime()));
                } else {
                    preparedStatement.setTimestamp(5, null);
                }
                preparedStatement.setString(6, attachment.getComment());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private java.util.Date getDateWithoutTime(java.util.Date date) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();
        return date;
    }

    @Override
    public void delete(Long id) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), id);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM people WHERE id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
    }

    @Override
    public People load(Long id) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), id);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT country, city, street, house, apartment, `index`, " +
                    "people.id, first_name, last_name, second_name, birth_date, sex, nationality, relationship_status, web_site, email, job, photo FROM people people " +
                    "LEFT JOIN address ON address.people_id = people.id WHERE people.id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                People people = new People();
                people.setId(resultSet.getInt("id"));
                String firstName = resultSet.getString("first_name");
                if (StringUtils.isNotBlank(firstName)) {
                    people.setFirstName(firstName);
                }
                String lastName = resultSet.getString("last_name");
                if (StringUtils.isNotBlank(lastName)) {
                    people.setLastName(lastName);
                }
                String secondName = resultSet.getString("second_name");
                if (StringUtils.isNotBlank(secondName)) {
                    people.setSecondName(secondName);
                }
                Date birthDate = resultSet.getDate("birth_date");
                if (birthDate != null) {
                    people.setBirthday(birthDate);
                }
                String sex = resultSet.getString("sex");
                if (StringUtils.isNotBlank(sex)) {
                    people.setSex(Sex.convertToSex(sex));
                }
                String nationality = resultSet.getString("nationality");
                if (StringUtils.isNotBlank(nationality)) {
                    people.setNationality(nationality);
                }
                String relationshipStatus = resultSet.getString("relationship_status");
                if (StringUtils.isNotBlank(relationshipStatus)) {
                    people.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(relationshipStatus));
                }
                String webSite = resultSet.getString("web_site");
                if (StringUtils.isNotBlank(webSite)) {
                    people.setWebSite(webSite);
                }
                String email = resultSet.getString("email");
                if (StringUtils.isNotBlank(email)) {
                    people.setEmail(email);
                }
                String job = resultSet.getString("job");
                if (StringUtils.isNotBlank(job)) {
                    people.setJob(job);
                }
                InputStream photo = resultSet.getBinaryStream("photo");
                if (photo != null) {
                    people.setPhoto(photo);
                }

                Address address = new Address();
                String country = resultSet.getString("country");
                if (StringUtils.isNotBlank(country)) {
                    address.setCountry(country);
                }
                String city = resultSet.getString("city");
                if (StringUtils.isNotBlank(city)) {
                    address.setCity(city);
                }
                String street = resultSet.getString("street");
                if (StringUtils.isNotBlank(street)) {
                    address.setStreet(street);
                }
                String house = resultSet.getString("house");
                if (StringUtils.isNotBlank(house)) {
                    address.setHouse(house);
                }
                String apartment = resultSet.getString("apartment");
                if (StringUtils.isNotBlank(apartment)) {
                    address.setApartment(apartment);
                }
                String index = resultSet.getString("index");
                if (StringUtils.isNotBlank(index)) {
                    address.setIndex(index);
                }
                people.setAddress(address);

                List<Phone> phones = getPhonesByPeople(id);
                people.setPhones(phones);

                List<Attachment> attachments = getAttachmentsByPeople(id);
                people.setAttachments(attachments);

                return people;
            }
            return null;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    private List<Phone> getPhonesByPeople(Long peopleId) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), peopleId);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM phone WHERE people_id = ?");
            preparedStatement.setLong(1, peopleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Phone> phones = new ArrayList<>();
            while (resultSet.next()) {
                Phone phone = new Phone();
                phone.setId(resultSet.getString("id"));
                String countryCode = resultSet.getString("country_code");
                if (StringUtils.isNotBlank(countryCode)) {
                    phone.setCountryCode(countryCode);
                }
                String operatorCode = resultSet.getString("operator_code");
                if (StringUtils.isNotBlank(operatorCode)) {
                    phone.setOperatorCode(operatorCode);
                }
                String phoneNumber = resultSet.getString("phone_number");
                if (StringUtils.isNotBlank(phoneNumber)) {
                    phone.setPhoneNumber(phoneNumber);
                }
                String phoneType = resultSet.getString("phone_type");
                if (StringUtils.isNotBlank(phoneType)) {
                    phone.setPhoneType(PhoneType.convertToPhoneType(phoneType));
                }
                String comment = resultSet.getString("comment");
                if (StringUtils.isNotBlank(comment)) {
                    phone.setComment(comment);
                }
                phones.add(phone);
            }

            return phones;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    private List<Attachment> getAttachmentsByPeople(Long peopleId) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), peopleId);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM attachment WHERE people_id = ?");
            preparedStatement.setLong(1, peopleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Attachment> attachments = new ArrayList<>();
            while (resultSet.next()) {
                Attachment attachment = new Attachment();
                attachment.setId(resultSet.getInt("id"));
                String generatedName = resultSet.getString("generated_name");
                if (StringUtils.isNotBlank(generatedName)) {
                    attachment.setGeneratedName(generatedName);
                }
                String originalName = resultSet.getString("original_name");
                if (StringUtils.isNotBlank(originalName)) {
                    attachment.setOriginalName(originalName);
                }
                String type = resultSet.getString("type");
                if (StringUtils.isNotBlank(type)) {
                    attachment.setType(type);
                }
                Date uploadDate = resultSet.getDate("upload_date");
                if (uploadDate != null) {
                    attachment.setUploadDate(uploadDate);
                }
                String comment = resultSet.getString("comment");
                if (StringUtils.isNotBlank(comment)) {
                    attachment.setComment(comment);
                }
                attachments.add(attachment);
            }

            return attachments;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    private List<Integer> getIdsPhonesByPeople(Long peopleId) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), peopleId);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT id  FROM phone WHERE people_id = ?");
            preparedStatement.setLong(1, peopleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
            return ids;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    private List<Integer> getIdsAttachmentsByPeople(Long peopleId) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), peopleId);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM attachment WHERE people_id = ?");
            preparedStatement.setLong(1, peopleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
            return ids;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    @Override
    public People update(People people) {
        logger.info("{}:{}; parameters: {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), people.toString());

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "UPDATE people SET first_name = ?, last_name = ?, second_name = ?, birth_date = ?, " +
                    "sex = ?, nationality = ?, relationship_status = ?, web_site = ?, email = ?, job = ?";
            if (people.getPhoto() != null) {
                sql = StringUtils.appendIfMissing(sql, ", photo = ? WHERE id = ?");
            } else {
                sql = StringUtils.appendIfMissing(sql, " WHERE id = ?");
            }
            preparedStatement = connection.prepareStatement(sql);
            preparedStatementSetPeopleAttributes(people, preparedStatement);
            if (people.getPhoto() != null) {
                preparedStatement.setBlob(11, people.getPhoto());
                preparedStatement.setInt(12, people.getId());
            } else {
                preparedStatement.setInt(11, people.getId());
            }
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("UPDATE address " +
                    "SET country = ?, city = ?, street = ?, house = ?, apartment = ?, `index` = ? " +
                    "WHERE people_id = ?");
            Address address = people.getAddress();
            preparedStatement.setString(1, address.getCountry());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getStreet());
            preparedStatement.setString(4, address.getHouse());
            preparedStatement.setString(5, address.getApartment());
            preparedStatement.setString(6, address.getIndex());
            preparedStatement.setInt(7, people.getId());

            preparedStatement.executeUpdate();

            List<Integer> idsExistPhones = getIdsPhonesByPeople(Long.valueOf(people.getId()));

            List<Phone> phones = people.getPhones();
            for (Phone phone : phones) {
                if (phone.getId().contains("new")) {
                    preparedStatement = connection.prepareStatement("INSERT INTO phone(people_id, country_code, operator_code, phone_number, phone_type, comment) " +
                            "VALUE (?, ?, ?, ?, ?, ?)");
                    preparedStatement.setInt(1, people.getId());
                    preparedStatement.setString(2, phone.getCountryCode());
                    preparedStatement.setString(3, phone.getOperatorCode());
                    preparedStatement.setString(4, phone.getPhoneNumber());
                    preparedStatement.setString(5, PhoneType.convertToString(phone.getPhoneType()));
                    preparedStatement.setString(6, phone.getComment());
                    preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connection.prepareStatement("UPDATE phone " +
                            "SET country_code = ?, operator_code = ?, phone_number = ?, phone_type = ?, comment = ? " +
                            "WHERE id = ?");
                    preparedStatement.setString(1, phone.getCountryCode());
                    preparedStatement.setString(2, phone.getOperatorCode());
                    preparedStatement.setString(3, phone.getPhoneNumber());
                    preparedStatement.setString(4, PhoneType.convertToString(phone.getPhoneType()));
                    preparedStatement.setString(5, phone.getComment());
                    preparedStatement.setInt(6, Integer.parseInt(phone.getId()));
                    preparedStatement.executeUpdate();

                    int index = idsExistPhones.indexOf(Integer.parseInt(phone.getId()));
                    if (index != -1) {
                        idsExistPhones.remove(index);
                    }
                }
            }

            for (Integer idPhone : idsExistPhones) {
                preparedStatement = connection.prepareStatement("DELETE FROM phone WHERE id = ?");
                preparedStatement.setLong(1, idPhone);
                preparedStatement.executeUpdate();
            }

            List<Integer> idsExistAttachments = getIdsAttachmentsByPeople(Long.valueOf(people.getId()));

            List<Attachment> attachments = people.getAttachments();
            for (Attachment attachment : attachments) {
                if (attachment.getId() == -1) {
                    preparedStatement = connection.prepareStatement("INSERT INTO attachment(people_id, generated_name, original_name, type, upload_date, comment) " +
                            "VALUE (?, ?, ?, ?, ?, ?)");
                    preparedStatement.setInt(1, people.getId());
                    preparedStatement.setString(2, attachment.getGeneratedName());
                    preparedStatement.setString(3, attachment.getOriginalName());
                    preparedStatement.setString(4, attachment.getType());
                    if (attachment.getUploadDate() != null) {
                        java.util.Date uploadDate = getDateWithoutTime(attachment.getUploadDate());
                        preparedStatement.setTimestamp(5, new Timestamp(uploadDate.getTime()));
                    } else {
                        preparedStatement.setTimestamp(5, null);
                    }
                    preparedStatement.setString(6, attachment.getComment());
                    preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connection.prepareStatement("UPDATE attachment " +
                            "SET original_name = ?, comment = ? WHERE id = ?");
                    preparedStatement.setString(1, attachment.getOriginalName());
                    preparedStatement.setString(2, attachment.getComment());
                    preparedStatement.setInt(3, attachment.getId());
                    preparedStatement.executeUpdate();

                    int index = idsExistAttachments.indexOf(attachment.getId());
                    if (index != -1) {
                        idsExistAttachments.remove(index);
                    }
                }
            }

            for (Integer idAttachment : idsExistAttachments) {
                preparedStatement = connection.prepareStatement("DELETE FROM attachment WHERE id = ?");
                preparedStatement.setLong(1, idAttachment);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    @Override
    public List<People> find(int limit, int offset) {
        logger.info("{}:{}; parameters: {}, {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), limit, offset);

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT country, city, street, house, apartment, `index`," +
                    "people.id, first_name, last_name, second_name, birth_date, job FROM people people LEFT JOIN address ON address.people_id = people.id " +
                    "LIMIT ?, ?");
            preparedStatement.setInt(1, limit * offset);
            preparedStatement.setInt(2, limit);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<People> peoples = new ArrayList<>();
            while (resultSet.next()) {
                People people = new People();
                people.setId(resultSet.getInt("id"));
                String firstName = resultSet.getString("first_name");
                if (StringUtils.isNotBlank(firstName)) {
                    people.setFirstName(firstName);
                }
                String lastName = resultSet.getString("last_name");
                if (StringUtils.isNotBlank(lastName)) {
                    people.setLastName(lastName);
                }
                String secondName = resultSet.getString("second_name");
                if (StringUtils.isNotBlank(secondName)) {
                    people.setSecondName(secondName);
                }
                Date birthDate = resultSet.getDate("birth_date");
                if (birthDate != null) {
                    people.setBirthday(birthDate);
                }
                String job = resultSet.getString("job");
                if (StringUtils.isNotBlank(job)) {
                    people.setJob(job);
                }

                Address address = new Address();
                String country = resultSet.getString("country");
                if (StringUtils.isNotBlank(country)) {
                    address.setCountry(country);
                }
                String city = resultSet.getString("city");
                if (StringUtils.isNotBlank(city)) {
                    address.setCity(city);
                }
                String street = resultSet.getString("street");
                if (StringUtils.isNotBlank(street)) {
                    address.setStreet(street);
                }
                String house = resultSet.getString("house");
                if (StringUtils.isNotBlank(house)) {
                    address.setHouse(house);
                }
                String apartment = resultSet.getString("apartment");
                if (StringUtils.isNotBlank(apartment)) {
                    address.setApartment(apartment);
                }
                String index = resultSet.getString("index");
                if (StringUtils.isNotBlank(index)) {
                    address.setIndex(index);
                }
                people.setAddress(address);

                peoples.add(people);
            }
            return peoples;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }

    @Override
    public Integer getCountElements() {
        logger.info("{}:{};", Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM people;");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
        return null;
    }
}
