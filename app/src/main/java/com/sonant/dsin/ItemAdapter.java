package com.sonant.dsin;

public class ItemAdapter {
    private int image;
    private  String key;
    private String id;
    private String bookurl;
    private String name;
    private String pgno;
    private  String heading;
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPgno() {
        return pgno;
    }

    public String getBookurl() {
        return bookurl;
    }

    public void setBookurl(String bookurl) {
        this.bookurl = bookurl;
    }

    public ItemAdapter() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setPgno(String pgno) {
        this.pgno = pgno;
    }
}