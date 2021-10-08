package com.sonant.dsin;

import android.util.Log;

public class SubjectAdapter {

    private String heading, subtopic, id, chapterNumber, pgno,keyname , url,poss,chapterpdfurl,chaptername;

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    private String position;

    public String getChapterpdfurl() {
        return chapterpdfurl;
    }

    public void setChapterpdfurl(String chapterpdfurl) {
        this.chapterpdfurl = chapterpdfurl;
    }

    private  boolean isSelected = false;


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPoss() {
        return poss;
    }

    public void setPoss(String poss) {
        this.poss = poss;
    }

    public String getHeading() {
        return heading;

    }

    public String getUrl() {
        return url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPgno() {
        return pgno;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public void setPgno(String pgno) {
        this.pgno = pgno;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public SubjectAdapter() {
    }


    public SubjectAdapter(String id, String heading) {
        this.id = id;
        this.heading = heading;
    }
}