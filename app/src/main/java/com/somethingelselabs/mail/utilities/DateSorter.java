package com.somethingelselabs.mail.utilities;

import com.somethingelselabs.mail.data.MyMail;

import java.util.Comparator;

import javax.mail.Message;
import javax.mail.MessagingException;

import timber.log.Timber;

/**
 * Created by Carson on 8/17/2020.
 */
public class DateSorter implements Comparator<MyMail>
{

    @Override
    public int compare(MyMail m1, MyMail m2) {
        //Timber.e("Comparing " + m1.getReceivedDate() + " --- " + m2.getReceivedDate());
        return m1.getReceivedDate().compareTo(m2.getReceivedDate());

    }
}
