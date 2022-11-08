package com.example.models;

import org.springframework.stereotype.Component;

@Component
public class Book {
    private int id;
    private String title;
    private String author;
    private String description;

    public long getCountChar(char str){
        return title.toLowerCase().chars().filter(c -> c == Character.toLowerCase(str)).count();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
