package com.somethingelselabs.mail.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.somethingelselabs.mail.BuildConfig;
import com.somethingelselabs.mail.R;
import com.somethingelselabs.mail.data.MyMail;
import com.somethingelselabs.mail.databinding.ActivityAutomateBinding;
import com.somethingelselabs.mail.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;

import timber.log.Timber;

/**
 * Created by Carson on 8/18/2020.
 */
public class AutomateActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAutomateBinding binding;
    private static Message[] messages;
    private static ArrayList<MyMail> myMailArray = new ArrayList<>();

    ProgressBar text;
    TextView text2;
    TextView countText;
    TextView automateText;
    int status = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate);

        binding = ActivityAutomateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        setup();
    }

    public void setup() {

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        text = binding.progressHorizontal;
        text2 = binding.value123;
        countText = binding.emailCountText;
        automateText = binding.automatingText;

        myMailArray = HomeActivity.getLoadedMail();
        messages = HomeActivity.getMessagesStored();

        startAutomation();

    }

    private void startAutomation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (status < myMailArray.size()) {

                    //remove this and put status +1 after processing
                    status += 1;

                    if (automateText.getText().equals("Automating Emails")) {
                        automateText.setText("Automating Emails.");
                    } else if (automateText.getText().equals("Automating Emails.")) {
                        automateText.setText("Automating Emails..");
                    } else if (automateText.getText().equals("Automating Emails..")) {
                        automateText.setText("Automating Emails...");
                    } else if (automateText.getText().equals("Automating Emails...")) {
                        automateText.setText("Automating Emails");
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            double percent = ((double)status / (double)myMailArray.size()) * 100;
                            Timber.e("percet: %s", percent);
                            int percentSmall = (int) percent;
                            text.setProgress(percentSmall);
                            text2.setText(String.format(Locale.CANADA, "%.2f", percent));
                            countText.setText( status + " / " + myMailArray.size());

                            if (status == myMailArray.size()) {
                                text2.setText("Complete");
                            }
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void onClick(View view) {

    }


}
