package com.example.controllers;

import com.example.models.Book;
import com.example.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/title-sorted-in-reverse")
    public ResponseEntity<List<Book>> getBooksTitleSortedInReverse() {
        return new ResponseEntity<>(bookService.findAllByOrderByTitleDesc(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Integer>> addBook(@RequestBody Book book){
        return new ResponseEntity<>(Collections.singletonMap("id", bookService.saveBook(book)), HttpStatus.OK);
    }

    @GetMapping("/group-by-author")
    public ResponseEntity<List<Book>> getBooksGroupByAuthor() {
        return new ResponseEntity<>(bookService.getBooksGroupByAuthor(), HttpStatus.OK);
    }

    @GetMapping("/search-char-in-title")
    public ResponseEntity<LinkedHashMap<String, Long>> searchCharInTitle(@RequestParam("char") char title){
        return new ResponseEntity<>(bookService.searchCharInTitle(title), HttpStatus.OK);
    }
}
