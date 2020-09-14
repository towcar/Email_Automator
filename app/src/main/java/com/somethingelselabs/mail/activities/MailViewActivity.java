package com.somethingelselabs.mail.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.somethingelselabs.mail.BuildConfig;
import com.somethingelselabs.mail.R;
import com.somethingelselabs.mail.data.MyMail;
import com.somethingelselabs.mail.databinding.ActivityHomeBinding;
import com.somethingelselabs.mail.databinding.ActivityViewMailBinding;

import javax.mail.event.ConnectionListener;

import timber.log.Timber;

/**
 * Created by Carson on 8/12/2020.
 */

public class MailViewActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityViewMailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mail);

        binding = ActivityViewMailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        setup();
    }

    public void setup(){

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //get data
        MyMail mail = (MyMail) getIntent().getSerializableExtra("mail");
        binding.textTitle.setText(mail.getTitle());
        binding.textSender.setText(mail.getSender());
        String email = Html.fromHtml(mail.getBody(), Html.FROM_HTML_MODE_COMPACT).toString();

        for (int i = 0; i < email.length(); i++){
            char c = email.charAt(i);
            if (i + 1 < email.length()){
                char de = email.charAt(i + 1);
                //Process char
                if (c == '>' && de == '>'){
                    email = email.substring(0, i - 1) + "/n" + email.substring(i);
                    i += 2;
                }

            }

        }

        binding.textBody.setText(email);

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
