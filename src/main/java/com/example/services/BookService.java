package com.example.services;

import com.example.models.Book;
import com.example.repositories.BookRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiFunction;

@Service
@Transactional(readOnly = true)
public class BookService implements BookRepository{
    private final JdbcTemplate jdbcTemplate;

    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Book> findAllByOrderByTitleDesc(){
        return jdbcTemplate.query("SELECT * FROM Book ORDER BY title DESC", new BeanPropertyRowMapper<>(Book.class));
    }

    @Transactional
    public int saveBook(Book book){
        jdbcTemplate.update("INSERT INTO Book VALUES(?, ?, ?, ?)",
                book.getId(), book.getTitle(), book.getAuthor(), book.getDescription());
        return book.getId();
    }

    public List<Book> getBooksGroupByAuthor(){
        return jdbcTemplate.query("SELECT * FROM Book ORDER BY author", new BeanPropertyRowMapper<>(Book.class));
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
