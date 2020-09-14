package com.somethingelselabs.mail.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.somethingelselabs.mail.BuildConfig;
import com.somethingelselabs.mail.R;
import com.somethingelselabs.mail.data.MyMail;
import com.somethingelselabs.mail.databinding.ActivityHomeBinding;
import com.somethingelselabs.mail.utilities.DateSorter;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.SortTerm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements ConnectionListener, View.OnClickListener {

    private ActivityHomeBinding binding;
    private static Message[] messages;
    private static ArrayList<MyMail> myMailArray = new ArrayList<>();
    static boolean textIsHtml = false;
    static TextView mailLoadedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.e("test line");
        new getMail().execute();

        setup();
    }

    public void setup(){

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        //set on click listeners
        binding.automateButton.setOnClickListener(this);
        binding.mailButton.setOnClickListener(this);
        binding.otherButton.setOnClickListener(this);
        binding.settingButton.setOnClickListener(this);

        mailLoadedText = binding.mailLoadedText;
        mailLoadedText.setText("Loaded: 0");

    }

    public static Message[] getMessagesStored() {
        return messages;
    }


    //we'll make this screen similar to google home's screen
    //with the cetner button just some simple automate function for looping through the emails
/*
    @OnClick(R.id.automate_button)
    void submitButton(View view) {
        Timber.e("Submit Button Clicked");
        Toast.makeText(this, "Automate Mail", Toast.LENGTH_SHORT).show();
        receiveEmail("carson.skjerdal@somethingelselabs.com","NcvWXtLHBlvJ");
    }*/

    public static void receiveEmail(String user, String password) {
        Timber.e("receiveEmail called");
        try {
            //1) get the session object
            Properties properties = new Properties();
            //obtain imaps
            properties.setProperty("mail.store.protocol", "imaps");
            properties.setProperty("mail.smtp.port", "993");
            properties.setProperty("mail.imaps.auth", "true");
            properties.setProperty("mail.imaps.ssl.enable", "true");
            //properties.setProperty("mail.imaps.ssl.trust", "somethingelselabs.com");

/*
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(  "carson.skjerdal@somethingelselabs.com","NcvWXtLHBlvJ");
                }
            });
            session.setDebug(true);*/

            Session session = Session.getInstance(properties);
            session.setDebug(true);

            //2) create the imaps store object and connect with the imaps server
            //https://stackoverflow.com/questions/43014085/error-javax-mail-authenticationfailedexception-failed-to-connect

            IMAPStore emailStore = (IMAPStore) session.getStore("imaps");
            //emailStore.addConnectionListener(this);
            //emailStore.connect();

            //emailStore.connect(user, password);
            emailStore.connect("somethingelselabs.com", user, password);


            //3) create the folder object and open it
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //4) retrieve the messages from the folder in an array and print it
            SortTerm[] sortTerms = new SortTerm[1];
            sortTerms[0] = SortTerm.ARRIVAL;
            //messages = emailFolder.getMessages();
            messages = ((IMAPFolder)emailFolder).getSortedMessages(sortTerms);

            int mLength = 100;
            if (messages.length < 100) {
                mLength = messages.length;
            }

            for (int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];
                String body = getText(message);

                MyMail mail = new MyMail(message.getSubject(), message.getFrom()[0].toString(), body, message.getReceivedDate());
                myMailArray.add(mail);

                String mailLoadedCount = "Loaded: " + myMailArray.size();
                mailLoadedText.setText(mailLoadedCount);
            }
            /*
            for (int i = 0; i < mLength; i++) {
                Message message = messages[i];
                //Timber.e("---------------------------------");
                //Timber.e("Email Number " + (i + 1));
                //Timber.e("Subject: " + message.getSubject());
                //Timber.e("From: " + message.getFrom()[0]);
                //Timber.e("Text: " + message.getContent().toString());
                //public MyMail(String title,  String sender,  String body) {
                String body = getText(message);

                MyMail mail = new MyMail(message.getSubject(), message.getFrom()[0].toString(), body, message.getReceivedDate());
                myMailArray.add(mail);
            }*/

            myMailArray.sort(new DateSorter());

            Timber.e("Complete");

            //5) close the store and folder objects
            //emailFolder.close(false);
            //emailStore.close();


        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            Timber.e(e);
        } catch (MessagingException e) {
            e.printStackTrace();
            Timber.e(e);
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e);
        }
    }

    @Override
    public void opened(ConnectionEvent e) {
        Timber.e("connection opened");
    }

    @Override
    public void disconnected(ConnectionEvent e) {
        Timber.e("connection disconnected");

    }

    @Override
    public void closed(ConnectionEvent e) {
        Timber.e("connection closed");

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.automate_button:{
                //handle loop operations
                Intent intent = new Intent(this, AutomateActivity.class);


                Bundle b = new Bundle();

                //intent.putExtra("mail", myMailArray);
                intent.putExtras(b);

                //begin activity
                startActivity(intent);
                break;
            }

            case R.id.mail_button:{
                //list mail
                //create fragment for viewing internal files
                Intent intent = new Intent(this, MailFolderActivity.class);


                Bundle b = new Bundle();

                //intent.putExtra("mail", myMailArray);
                intent.putExtras(b);

                //begin activity
                startActivity(intent);
                break;
            }

            case R.id.other_button:{
                //other

                break;
            }

            case R.id.setting_button:{
                //settings

                break;
            }
        }
    }


    private static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            try {
                String s = (String) p.getContent();
                textIsHtml = p.isMimeType("text/html");
                return s;
            } catch (IOException | MessagingException | ClassCastException e) {
                e.printStackTrace();
            }
            return "";
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

    public static ArrayList<MyMail> getLoadedMail() {
        return myMailArray;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.option1:


                new getMail().execute();
                return  true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class getMail extends AsyncTask<String, Integer, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        HomeActivity.receiveEmail("carson.skjerdal@somethingelselabs.com", "NcvWXtLHBlvJ");
        return null;
    }

    protected void onProgressUpdate() {
        //called when the background task makes any progress
    }

    protected void onPreExecute() {
        //called before doInBackground() is started
    }

    protected void onPostExecute() {
        //called after doInBackground() has finished
    }

}
