/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gumtreescraper.model;

import java.util.Date;

/**
 *
 * @author duonghung1269
 */
public class Gumtree {
    private static final String DELIMITER = ",";
    private String url = "";
    private String name = "";
    private String phone = "";
    private String type = "";
    private Date scrapedDate = new Date();
    private String notes = "";

        
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getScrapedDate() {
        return scrapedDate;
    }

    public void setScrapedDate(Date scrapedDate) {
        this.scrapedDate = scrapedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(url)
          .append(DELIMITER)
          .append(name)
          .append(DELIMITER)
                .append(phone).append(DELIMITER)
                .append(type).append(DELIMITER)
                .append(scrapedDate).append(DELIMITER)
                .append(notes);
        return sb.toString();
    }
    
    
}
