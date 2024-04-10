package org.dmitriy.dao;

import org.dmitriy.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234as";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL ,USERNAME, PASSWORD);  // подключение к базе данных
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() {  // возврат всех пользователей

        List<Person> people = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();  // объект для запросов базе данных
            String SQL = "SELECT * FROM Person"; // строка запроса
            ResultSet resultSet = statement.executeQuery(SQL);  // получение результата запроса

            while (resultSet.next()) {  // чтение резульата запроса
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return people;
    }

    public Person show(int id) {

        Person person = null;

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Person WHERE id=?");
            preparedStatement.setInt(1, id);  // нумерация начинается с 1

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            person = new Person();

            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return person;
    }

    public void save(Person person) {

        try {
            // почти то же самое что и обычный statement, но быстрее и защищеннее и удобнее
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());

            preparedStatement.executeUpdate(); // выполнени запроса

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(int id, Person updatedPerson) {
        Person person = new Person();

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?");
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Person WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
