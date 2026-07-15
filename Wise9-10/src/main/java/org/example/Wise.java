package org.example;

public class Wise {
    String content;
    String author;
    int id;
    static int count = 0;

    public Wise(String content, String author) {
        this.content = content;
        this.author = author;
        this.id = ++count;
    }

    public void getWise() {
        System.out.println(id + " / " + author + " / " + content);
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getId() {
        return this.id;
    }

    public void updateWise(String newContent, String newAuthor) {
        this.content = newContent;
        this.author = newAuthor;
    }
}
