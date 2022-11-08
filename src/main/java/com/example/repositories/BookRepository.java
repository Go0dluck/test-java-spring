package com.example.repositories;

import com.example.models.Book;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BookRepository {
    List<Book> findAllByOrderByTitleDesc();
    int saveBook(Book book);
    List<Book> getBooksGroupByAuthor();
    LinkedHashMap<String, Long> searchCharInTitle(char title);
}
