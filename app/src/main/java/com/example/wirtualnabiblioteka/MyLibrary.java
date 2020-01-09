package com.example.wirtualnabiblioteka;

public class MyLibrary {

    private int id,status, id_copy;
    private String title, name, surname, pseudonym, descr, genre,  language, namePubl,  locationPubl,date1,  date2;

    public MyLibrary(int id, String title,String name, String surname, String pseudonym, String descr,
                     String genre, String language, String namePubl, String locationPubl, int id_copy,
                     int status, String date1, String date2) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.surname=surname;
        this.pseudonym=pseudonym;
        this.descr = descr;
        this.genre = genre;
        this.language=language;
        this.namePubl=namePubl;
        this.locationPubl = locationPubl;
        this.id_copy=id_copy;
        this.status = status;
        this.date1 = date1;
        this.date2=date2;
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
    public String getSurname() {
        return surname;
    }

    public String getPseudonym() {
        return pseudonym;
    }
    public String getDescr() {
        return descr;
    }

    public String getGenre() {
        return genre;
    }
    public String getLanguage() {
        return language;
    }

    public String getNamePubl() {
        return namePubl;
    }
    public String getLocationPubl() {
        return locationPubl;
    }

    public int getCopyId() { return id_copy; }

    public int getStatus() { return status; }

    public String getDate1() {
        return date1;
    }

    public String getDate2() {
        return date2;
    }
}
