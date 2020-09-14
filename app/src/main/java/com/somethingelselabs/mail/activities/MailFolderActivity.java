package com.somethingelselabs.mail.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.somethingelselabs.mail.R;
import com.somethingelselabs.mail.adapters.MailAdapter;
import com.somethingelselabs.mail.data.MyMail;
import com.somethingelselabs.mail.databinding.ActivityMailBinding;
import com.somethingelselabs.mail.utilities.DateFormatter;

import java.util.ArrayList;

import javax.mail.Message;

import timber.log.Timber;

/**
 * Created by Carson on 8/11/2020.
 */
public class MailFolderActivity  extends AppCompatActivity  {

    ActivityMailBinding binding;
    private ArrayList<MyMail> myMailArray;

    MailAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        binding = com.somethingelselabs.mail.databinding.ActivityMailBinding.inflate(getLayoutInflater());
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

            //Obtain Messages
                    myMailArray = HomeActivity.getLoadedMail();

            String description = String.valueOf(Html.fromHtml(myMailArray.get(1).getBody(), Html.FROM_HTML_MODE_COMPACT));
            description.trim();

            description = DateFormatter.remove_parenthesis(description, "{}");
            Timber.e(description);


            //setup recyclerview
            recyclerView = binding.mailRecyclerview;
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            adapter = new MailAdapter(myMailArray, this);
            recyclerView.setAdapter(adapter);

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
