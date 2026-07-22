package org.example;

public class Wise {
    private int id;
    private String content;
    private String author;

    public Wise(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
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
