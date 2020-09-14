package com.somethingelselabs.mail.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Carson on 8/11/2020.
 */
public class MyMail implements Serializable {

    private String title;
    private String body;
    private String sender;
    private Date dateRecieved;

    public MyMail(String title, String sender, String body, Date receivedDate) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.dateRecieved = receivedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public Date getReceivedDate() {
        return dateRecieved;
    }
}
