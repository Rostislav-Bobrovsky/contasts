package com.itechart.contacts.dao;

import com.itechart.contacts.dao.generic.ConnectionFactory;
import com.itechart.contacts.model.*;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostislav on 09-Mar-15.)
 */
public class SearchDaoImpl implements SearchDao {

    @Override
    public List<People> getBySearch(SearchDto searchDto) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            StringBuilder sql = new StringBuilder("SELECT country, city, street, house, apartment, `index`, " +
                    "people.id, first_name, last_name, sur_name, birth_date, job FROM people people " +
                    "LEFT JOIN address ON address.people_id = people.id WHERE ");
            if (searchDto.getFirstName() != null) {
                sql.append("people.first_name LIKE '%").append(searchDto.getFirstName()).append("%' AND ");
            }
            if (searchDto.getLastName() != null) {
                sql.append("people.last_name LIKE '%").append(searchDto.getLastName()).append("%' AND ");
            }
            if (searchDto.getSurName() != null) {
                sql.append("people.sur_name LIKE '%").append(searchDto.getSurName()).append("%' AND ");
            }
            if (searchDto.getDateFrom() != null) {
                sql.append("people.birth_date >= '").append(new Timestamp(searchDto.getDateFrom().getTime())).append("' AND ");
            }
            if (searchDto.getDateTo() != null) {
                sql.append("people.birth_date < '").append(new Timestamp(searchDto.getDateTo().getTime())).append("' + INTERVAL 1 DAY AND ");
            }
            if (searchDto.getSex() != null) {
                sql.append("people.sex LIKE '%").append(Sex.convertToString(searchDto.getSex())).append("%' AND ");
            }
            if (searchDto.getNationality() != null) {
                sql.append("people.nationality LIKE '%").append(searchDto.getNationality()).append("%' AND ");
            }
            if (searchDto.getRelationshipStatus() != null) {
                sql.append("people.relationship_status LIKE '%").append(RelationshipStatus.convertToString(searchDto.getRelationshipStatus())).append("%' AND ");
            }
            if (searchDto.getWebSite() != null) {
                sql.append("people.web_site LIKE '%").append(searchDto.getWebSite()).append("%' AND ");
            }
            if (searchDto.getEmail() != null) {
                sql.append("people.email LIKE '%").append(searchDto.getEmail()).append("%' AND ");
            }
            if (searchDto.getJob() != null) {
                sql.append("people.job LIKE '%").append(searchDto.getJob()).append("%' AND ");
            }
            if (searchDto.getAddress().getCountry() != null) {
                sql.append("address.country LIKE '%").append(searchDto.getAddress().getCountry()).append("%' AND ");
            }
            if (searchDto.getAddress().getCity() != null) {
                sql.append("address.city LIKE '%").append(searchDto.getAddress().getCity()).append("%' AND ");
            }
            if (searchDto.getAddress().getStreet() != null) {
                sql.append("address.street LIKE '%").append(searchDto.getAddress().getStreet()).append("%' AND ");
            }
            if (searchDto.getAddress().getHouse() != null) {
                sql.append("address.house LIKE '%").append(searchDto.getAddress().getHouse()).append("%' AND ");
            }
            if (searchDto.getAddress().getApartment() != null) {
                sql.append("address.apartment LIKE '%").append(searchDto.getAddress().getApartment()).append("%' AND ");
            }
            if (searchDto.getAddress().getIndex() != null) {
                sql.append("address.`index` LIKE '%").append(searchDto.getAddress().getIndex()).append("%' AND ");
            }

            String sqlRequest = String.valueOf(sql);
            sqlRequest = sqlRequest.substring(0, sqlRequest.length() - 4);
            preparedStatement = connection.prepareStatement(sqlRequest);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<People> peoples = new ArrayList<>();
            if (resultSet.first()) {
                do {
                    People people = new People();
                    people.setId(resultSet.getInt("id"));
                    if (StringUtils.isNotBlank(resultSet.getString("first_name"))) {
                        people.setFirstName(resultSet.getString("first_name"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("last_name"))) {
                        people.setLastName(resultSet.getString("last_name"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("sur_name"))) {
                        people.setSurName(resultSet.getString("sur_name"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("birth_date"))) {
                        people.setBirthday(resultSet.getDate("birth_date"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("job"))) {
                        people.setJob(resultSet.getString("job"));
                    }

                    Address address = new Address();
                    if (StringUtils.isNotBlank(resultSet.getString("country"))) {
                        address.setCountry(resultSet.getString("country"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("city"))) {
                        address.setCity(resultSet.getString("city"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("street"))) {
                        address.setStreet(resultSet.getString("street"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("house"))) {
                        address.setHouse(resultSet.getString("house"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("apartment"))) {
                        address.setApartment(resultSet.getString("apartment"));
                    }
                    if (StringUtils.isNotBlank(resultSet.getString("index"))) {
                        address.setIndex(resultSet.getString("index"));
                    }
                    people.setAddress(address);

                    peoples.add(people);
                } while (resultSet.next());
            }
            return peoples;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<String> getSelectEmails(String[] ids) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            StringBuilder sql = new StringBuilder("SELECT email FROM people WHERE people.id IN (");
            for (String id : ids) {
                sql.append(id).append(",");
            }
            String sqlRequest = String.valueOf(sql);
            sqlRequest = sqlRequest.substring(0, sqlRequest.length() - 1) + ")";
            preparedStatement = connection.prepareStatement(sqlRequest);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> emails = new ArrayList<>();
            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
            return emails;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
