package com.example.wirtualnabiblioteka;


class Library {
    private int id;
    private String title, name;

    public Library(int id, String title,String name) {
        this.id = id;
        this.title = title;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return name;
    }




}