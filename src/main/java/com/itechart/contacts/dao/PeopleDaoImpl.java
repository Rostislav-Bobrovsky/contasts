package com.itechart.contacts.dao;

import com.itechart.contacts.dao.generic.AbstractDao;
import com.itechart.contacts.dao.generic.ConnectionFactory;
import com.itechart.contacts.model.*;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public class PeopleDaoImpl extends AbstractDao<People> implements PeopleDao {
    @Override
    public void create(People people) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO people(first_name, last_name, sur_name, birth_date, sex, nationality, relationship_status, web_site, email, job) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, people.getFirstName());
            preparedStatement.setString(2, people.getLastName());
            preparedStatement.setString(3, people.getSurName());
            if (people.getBirthday() == null) {
                preparedStatement.setTimestamp(4, null);
            } else {
                preparedStatement.setTimestamp(4, new Timestamp(people.getBirthday().getTime()));
            }
            preparedStatement.setString(5, Sex.convertToString(people.getSex()));
            preparedStatement.setString(6, people.getNationality());
            preparedStatement.setString(7, RelationshipStatus.convertToString(people.getRelationshipStatus()));
            preparedStatement.setString(8, people.getWebSite());
            preparedStatement.setString(9, people.getEmail());
            preparedStatement.setString(10, people.getJob());

            preparedStatement.executeUpdate();

            ResultSet tableKeys = preparedStatement.getGeneratedKeys();
            tableKeys.next();
            int idPeople = tableKeys.getInt(1);

            preparedStatement = connection.prepareStatement("INSERT INTO address(people_id, country, city, street, house, apartment, `index`) " +
                    "VALUE (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, idPeople);
            preparedStatement.setString(2, people.getAddress().getCountry());
            preparedStatement.setString(3, people.getAddress().getCity());
            preparedStatement.setString(4, people.getAddress().getStreet());
            preparedStatement.setString(5, people.getAddress().getHouse());
            preparedStatement.setString(6, people.getAddress().getApartment());
            preparedStatement.setString(7, people.getAddress().getIndex());

            preparedStatement.executeUpdate();

            for (int i = 0; i < people.getPhones().size(); ++i) {
                preparedStatement = connection.prepareStatement("INSERT INTO phone(people_id, country_code, operator_code, phone_number, phone_type, comment) " +
                        "VALUE (?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, idPeople);
                preparedStatement.setString(2, people.getPhones().get(i).getCountryCode());
                preparedStatement.setString(3, people.getPhones().get(i).getOperatorCode());
                preparedStatement.setString(4, people.getPhones().get(i).getPhoneNumber());
                preparedStatement.setString(5, PhoneType.convertToString(people.getPhones().get(i).getPhoneType()));
                preparedStatement.setString(6, people.getPhones().get(i).getComment());

                preparedStatement.executeUpdate();
            }
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
    }

    @Override
    public void delete(Long id) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM people WHERE id = ?");
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
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
    }

    @Override
    public People load(Long id) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT country, city, street, house, apartment, `index`, " +
                    "people.id, first_name, last_name, sur_name, birth_date, sex, nationality, relationship_status, web_site, email, job FROM people people " +
                    "LEFT JOIN address ON address.people_id = people.id WHERE people.id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
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
                if (StringUtils.isNotBlank(resultSet.getString("sex"))) {
                    people.setSex(Sex.convertToSex(resultSet.getString("sex")));
                }
                if (StringUtils.isNotBlank(resultSet.getString("nationality"))) {
                    people.setNationality(resultSet.getString("nationality"));
                }
                if (StringUtils.isNotBlank(resultSet.getString("relationship_status"))) {
                    people.setRelationshipStatus(RelationshipStatus.convertToRelationshipStatus(resultSet.getString("relationship_status")));
                }
                if (StringUtils.isNotBlank(resultSet.getString("web_site"))) {
                    people.setWebSite(resultSet.getString("web_site"));
                }
                if (StringUtils.isNotBlank(resultSet.getString("email"))) {
                    people.setEmail(resultSet.getString("email"));
                }
                if (StringUtils.isNotBlank(resultSet.getString("job"))) {
                    people.setJob(resultSet.getString("job"));
                }

                List<Phone> phones = getPhonesByPeople(id);
                people.setPhones(phones);

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
                return people;
            }
            return null;
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

    private List<Phone> getPhonesByPeople(Long peopleId) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT *  FROM phone  WHERE people_id = ?");
            preparedStatement.setLong(1, peopleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Phone> phones = new ArrayList<>();
            while (resultSet.next()) {
                Phone phone = new Phone();
                phone.setId(resultSet.getString("id"));
                if (StringUtils.isNotBlank(resultSet.getString("country_code"))) {
                    phone.setCountryCode(resultSet.getString("country_code"));
                }
                if (StringUtils.isNotBlank(resultSet.getString("operator_code"))) {
                    phone.setOperatorCode(resultSet.getString("operator_code"));
                }
                if (StringUtils.isNotBlank(resultSet.getString("phone_number"))) {
                    phone.setPhoneNumber(resultSet.getString("phone_number"));
                }
                if (StringUtils.isNotBlank(resultSet.getString("phone_type"))) {
                    phone.setPhoneType(PhoneType.convertToPhoneType(resultSet.getString("phone_type")));
                }
                if (StringUtils.isNotBlank(resultSet.getString("comment"))) {
                    phone.setComment(resultSet.getString("comment"));
                }
                phones.add(phone);
            }
            return phones;
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

    private List<Integer> getIdsPhonesByPeople(Long peopleId) {
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
    public People update(People people) {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE people " +
                    "SET first_name = ?, last_name = ?, sur_name = ?, birth_date = ?, " +
                    "sex = ?, nationality = ?, relationship_status = ?, web_site = ?, email = ?, job = ? " +
                    "WHERE id = ?");
            preparedStatement.setString(1, people.getFirstName());
            preparedStatement.setString(2, people.getLastName());
            preparedStatement.setString(3, people.getSurName());
            if (people.getBirthday() == null) {
                preparedStatement.setTimestamp(4, null);
            } else {
                preparedStatement.setTimestamp(4, new Timestamp(people.getBirthday().getTime()));
            }
            preparedStatement.setString(5, Sex.convertToString(people.getSex()));
            preparedStatement.setString(6, people.getNationality());
            preparedStatement.setString(7, RelationshipStatus.convertToString(people.getRelationshipStatus()));
            preparedStatement.setString(8, people.getWebSite());
            preparedStatement.setString(9, people.getEmail());
            preparedStatement.setString(10, people.getJob());
            preparedStatement.setInt(11, people.getId());

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("UPDATE address " +
                    "SET country = ?, city = ?, street = ?, house = ?, apartment = ?, `index` = ? " +
                    "WHERE people_id = ?");
            preparedStatement.setString(1, people.getAddress().getCountry());
            preparedStatement.setString(2, people.getAddress().getCity());
            preparedStatement.setString(3, people.getAddress().getStreet());
            preparedStatement.setString(4, people.getAddress().getHouse());
            preparedStatement.setString(5, people.getAddress().getApartment());
            preparedStatement.setString(6, people.getAddress().getIndex());
            preparedStatement.setInt(7, people.getId());

            preparedStatement.executeUpdate();

            List<Integer> idsExistPhones = getIdsPhonesByPeople(Long.valueOf(people.getId()));

            for (int i = 0; i < people.getPhones().size(); ++i) {
                if (people.getPhones().get(i).getId().contains("new")) {
                    preparedStatement = connection.prepareStatement("INSERT INTO phone(people_id, country_code, operator_code, phone_number, phone_type, comment) " +
                            "VALUE (?, ?, ?, ?, ?, ?)");
                    preparedStatement.setInt(1, people.getId());
                    preparedStatement.setString(2, people.getPhones().get(i).getCountryCode());
                    preparedStatement.setString(3, people.getPhones().get(i).getOperatorCode());
                    preparedStatement.setString(4, people.getPhones().get(i).getPhoneNumber());
                    preparedStatement.setString(5, PhoneType.convertToString(people.getPhones().get(i).getPhoneType()));
                    preparedStatement.setString(6, people.getPhones().get(i).getComment());

                    preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connection.prepareStatement("UPDATE phone " +
                            "SET country_code = ?, operator_code = ?, phone_number = ?, phone_type = ?, comment = ? " +
                            "WHERE id = ?");
                    preparedStatement.setString(1, people.getPhones().get(i).getCountryCode());
                    preparedStatement.setString(2, people.getPhones().get(i).getOperatorCode());
                    preparedStatement.setString(3, people.getPhones().get(i).getPhoneNumber());
                    preparedStatement.setString(4, PhoneType.convertToString(people.getPhones().get(i).getPhoneType()));
                    preparedStatement.setString(5, people.getPhones().get(i).getComment());
                    preparedStatement.setInt(6, Integer.parseInt(people.getPhones().get(i).getId()));

                    preparedStatement.executeUpdate();

                    int index = idsExistPhones.indexOf(Integer.parseInt(people.getPhones().get(i).getId()));
                    idsExistPhones.remove(index);
                }
            }

            for (Integer idPhone : idsExistPhones) {
                preparedStatement = connection.prepareStatement("DELETE FROM phone WHERE id = ?");
                preparedStatement.setLong(1, idPhone);

                preparedStatement.executeUpdate();
            }

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
    public List<People> getAll() {
        Connection connection = ConnectionFactory.openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT country, city, street, house, apartment, `index`," +
                    "people.id, first_name, last_name, sur_name, birth_date, job FROM people people LEFT JOIN address ON address.people_id = people.id");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<People> peoples = new ArrayList<>();
            while (resultSet.next()) {
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
}
