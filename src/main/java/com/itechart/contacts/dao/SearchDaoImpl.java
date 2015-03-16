package com.itechart.contacts.dao;

import com.itechart.contacts.dao.generic.ConnectionFactory;
import com.itechart.contacts.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Rostislav on 09-Mar-15.)
 */
public class SearchDaoImpl implements SearchDao {
    private final static Logger logger = LogManager.getLogger(SearchDaoImpl.class);

    @Override
    public List<People> getBySearch(SearchDto searchDto) {
        logger.info("{}:{}; parameters: {}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), searchDto.toString());

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "SELECT country, city, street, house, apartment, `index`, " +
                    "people.id, first_name, last_name, second_name, birth_date, job FROM people people " +
                    "LEFT JOIN address ON address.people_id = people.id WHERE ";
            if (searchDto.getFirstName() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.first_name LIKE ? AND ");
            }
            if (searchDto.getLastName() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.last_name LIKE ? AND ");
            }
            if (searchDto.getSecondName() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.second_name LIKE ? AND ");
            }
            if (searchDto.getDateFrom() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.birth_date >= ? AND ");
            }
            if (searchDto.getDateTo() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.birth_date < ? + INTERVAL 1 DAY AND ");
            }
            if (searchDto.getSex() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.sex = ? AND ");
            }
            if (searchDto.getNationality() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.nationality LIKE ? AND ");
            }
            if (searchDto.getRelationshipStatus() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.relationship_status = ? AND ");
            }
            if (searchDto.getWebSite() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.web_site LIKE ? AND ");
            }
            if (searchDto.getEmail() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.email LIKE ? AND ");
            }
            if (searchDto.getJob() != null) {
                sql = StringUtils.appendIfMissing(sql, "people.job LIKE ? AND ");
            }
            if (searchDto.getAddress().getCountry() != null) {
                sql = StringUtils.appendIfMissing(sql, "address.country LIKE ? AND ");
            }
            if (searchDto.getAddress().getCity() != null) {
                sql = StringUtils.appendIfMissing(sql, "address.city LIKE ? AND ");
            }
            if (searchDto.getAddress().getStreet() != null) {
                sql = StringUtils.appendIfMissing(sql, "address.street LIKE ? AND ");
            }
            if (searchDto.getAddress().getHouse() != null) {
                sql = StringUtils.appendIfMissing(sql, "address.house LIKE ? AND ");
            }
            if (searchDto.getAddress().getApartment() != null) {
                sql = StringUtils.appendIfMissing(sql, "address.apartment LIKE ? AND ");
            }
            if (searchDto.getAddress().getIndex() != null) {
                sql = StringUtils.appendIfMissing(sql, "address.`index` LIKE ? AND ");
            }

            sql = StringUtils.removeEnd(sql, " AND ");
            System.out.println(sql);
            preparedStatement = connection.prepareStatement(sql);

            int i = 1;
            if (searchDto.getFirstName() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getFirstName() + "%");
            }
            if (searchDto.getLastName() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getLastName() + "%");
            }
            if (searchDto.getSecondName() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getSecondName() + "%");
            }
            if (searchDto.getDateFrom() != null) {
                preparedStatement.setTimestamp(i++, new Timestamp(searchDto.getDateFrom().getTime()));
            }
            if (searchDto.getDateTo() != null) {
                preparedStatement.setTimestamp(i++, new Timestamp(searchDto.getDateTo().getTime()));
            }
            if (searchDto.getSex() != null) {
                preparedStatement.setString(i++, Sex.convertToString(searchDto.getSex()));
            }
            if (searchDto.getNationality() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getNationality() + "%");
            }
            if (searchDto.getRelationshipStatus() != null) {
                preparedStatement.setString(i++, RelationshipStatus.convertToString(searchDto.getRelationshipStatus()));
            }
            if (searchDto.getWebSite() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getWebSite() + "%");
            }
            if (searchDto.getEmail() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getEmail() + "%");
            }
            if (searchDto.getJob() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getJob() + "%");
            }
            if (searchDto.getAddress().getCountry() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getAddress().getCountry() + "%");
            }
            if (searchDto.getAddress().getCity() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getAddress().getCity() + "%");
            }
            if (searchDto.getAddress().getStreet() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getAddress().getStreet() + "%");
            }
            if (searchDto.getAddress().getHouse() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getAddress().getHouse() + "%");
            }
            if (searchDto.getAddress().getApartment() != null) {
                preparedStatement.setString(i++, "%" + searchDto.getAddress().getApartment() + "%");
            }
            if (searchDto.getAddress().getIndex() != null) {
                preparedStatement.setString(i, "%" + searchDto.getAddress().getIndex() + "%");
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            List<People> peoples = new ArrayList<>();
            if (resultSet.first()) {
                do {
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
                } while (resultSet.next());
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
    public List<String> getSelectEmails(String[] ids) {
        logger.info("{}:{}; parameters: {}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), Arrays.toString(ids));

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT email FROM people WHERE people.id IN (");
            for (String id : ids) {
                sqlBuilder.append("?,");
            }
            String sql = String.valueOf(sqlBuilder);
            sql = StringUtils.appendIfMissing(StringUtils.removeEnd(sql, ","), ")");
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < ids.length; ++i) {
                preparedStatement.setString(i + 1, ids[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> emails = new ArrayList<>();
            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
            return emails;
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
    public List<People> getByEmail(String[] emails) {
        logger.info("{}:{}; parameters: {}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), Arrays.toString(emails));

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT first_name, email FROM people WHERE people.email IN (");
            for (String email : emails) {
                sqlBuilder.append("?,");
            }
            String sql = String.valueOf(sqlBuilder);
            sql = StringUtils.appendIfMissing(StringUtils.removeEnd(sql, ","), ")");
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < emails.length; ++i) {
                preparedStatement.setString(i + 1, emails[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            List<People> peoples = new ArrayList<>();
            while (resultSet.next()) {
                People people = new People();
                people.setFirstName(resultSet.getString("first_name"));
                people.setEmail(resultSet.getString("email"));
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
    public List<People> getByBirthday(java.util.Date date) {
        logger.info("{}:{}; parameters: {}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), date.toString());

        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT first_name, email, birth_date FROM people WHERE people.birth_date != \"NULL\"");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<People> peoples = new ArrayList<>();
            while (resultSet.next()) {
                People people = new People();
                people.setFirstName(resultSet.getString("first_name"));
                people.setEmail(resultSet.getString("email"));
                people.setBirthday(resultSet.getDate("birth_date"));
                peoples.add(people);
            }

            List<People> resultList = new ArrayList<>();
            for (People people : peoples) {
                Calendar calendarBrithday = Calendar.getInstance();
                calendarBrithday.setTime(people.getBirthday());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(date);

                int dateBr = calendarBrithday.get(Calendar.DATE);
                int dateDt = calendarDate.get(Calendar.DATE);

                if (dateBr == dateDt) {
                    int monthBr = calendarBrithday.get(Calendar.MONTH);
                    int monthDt = calendarDate.get(Calendar.MONTH);

                    if (monthBr == monthDt) {
                        resultList.add(people);
                    }
                }
            }
            return resultList;
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
