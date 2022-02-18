package com.example.statusmaster.Data;

public class FileClass {
    String Pathname;
    boolean isChek;

    public boolean isChek() {
        return isChek;
    }

    public void setChek(boolean chek) {
        isChek = chek;
    }

    public FileClass(String pathname) {
        Pathname = pathname;
    }

    public String getPathname() {
        return Pathname;
    }
}