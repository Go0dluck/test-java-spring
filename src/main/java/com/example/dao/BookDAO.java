package com.example.dao;

import com.example.models.Book;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.function.BiFunction;

@Component
@Transactional(readOnly = true)
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Book> findAllByOrderByTitleDesc(){
        return jdbcTemplate.query("SELECT * FROM Book ORDER BY title DESC", new BeanPropertyRowMapper<>(Book.class));
    }

    @Transactional
    public int saveBook(Book book){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement("INSERT INTO Book(title, author, description) VALUES(?, ?, ?) RETURNING id", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getDescription());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<Book> getBooksGroupByAuthor(){
        return jdbcTemplate.query("SELECT * FROM Book GROUP BY author, id, title, description", new BeanPropertyRowMapper<>(Book.class));
    }

    public LinkedHashMap<String, Long> searchCharInTitle(char title){
        BiFunction<Long, Long, Long> bFunc = (oldValue, newValue)-> oldValue + newValue;
        List<Book> books = jdbcTemplate.query("SELECT * FROM Book WHERE title ILIKE ?",
                new BeanPropertyRowMapper<>(Book.class),
                "%"+title+"%");
        Map<String, Long> unSortedMap = new HashMap<>();
        LinkedHashMap<String, Long> reverseSortedMap = new LinkedHashMap<>();

        books.forEach(
                x -> unSortedMap.merge(x.getAuthor(), x.getCountChar(title), bFunc));
        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        return reverseSortedMap;
    }
}
