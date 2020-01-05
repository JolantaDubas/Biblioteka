package com.example.wirtualnabiblioteka;

public class URL {
    private static final String ROOT_URL = "http://192.168.88.249/biblioteka/v1/Api.php?apicall=";

   // public static final String URL_CREATE_HERO = ROOT_URL + "createhero";
    public static final String URL_READ_BOOKS = ROOT_URL + "getbooks";
   // public static final String URL_UPDATE_HERO = ROOT_URL + "updatehero";
   // public static final String URL_DELETE_HERO = ROOT_URL + "deletehero&id=";
    public static final String URL_READ_BOOK = ROOT_URL + "getbook&id=";
}