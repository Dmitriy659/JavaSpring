package org.dmitriy.dao;

import org.dmitriy.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {  // возврат всех пользователей
        // получение списка пользователей из бд. Можно использовать готовый rowMapper, который при создании
        // объекта будет помещать значение в поле с таким же названием как столбец таблицы бд. Также нужно указать класс
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        // получение одного человека или null, если таких нет
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[] {id}, new PersonMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person VALUES(1, ?, ?, ?)", person.getName(), person.getAge(),
                person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        // в метода update не обязательно использовать массив Object
        jdbcTemplate.update("INSERT INTO person VALUES (1, ?, ?, ?) WHERE id=?", updatedPerson.getName(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }
}
