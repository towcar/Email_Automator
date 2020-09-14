package com.somethingelselabs.mail.adapters;

/**
 * Created by Carson on 8/11/2020.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.somethingelselabs.mail.activities.MailViewActivity;
import com.somethingelselabs.mail.data.MyMail;
import com.somethingelselabs.mail.databinding.ItemLayoutMailBinding;
import com.somethingelselabs.mail.utilities.DateFormatter;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Create by Carson Skjerdal of Something Else Labs on 1/14/2019.
 */
public class MailAdapter extends RecyclerView.Adapter<MailAdapter.ItemHolder> implements View.OnClickListener {

    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;


    private ArrayList<MyMail> itemList;
    private String itemName;
    private int itemPosition;
    private MailAdapter.ItemHolder itemHolder;
    private Activity myActivity;


    public MailAdapter(ArrayList<MyMail> list, Activity myActivity) {
        itemList = list;
        this.myActivity = myActivity;
    }

    @Override
    public void onClick(View v) {


    }


    /* ViewHolder for each item */
    class ItemHolder extends RecyclerView.ViewHolder {


        private ItemLayoutMailBinding binding;

        ItemHolder(ItemLayoutMailBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

    }


    @NonNull
    @Override
    public MailAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MailAdapter.ItemHolder(ItemLayoutMailBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }


    @Override
    public void onBindViewHolder(final MailAdapter.ItemHolder holder, int position) {
        //holder.setIsRecyclable(false);
        final MyMail item = (MyMail) itemList.get(position);

        if (item.getTitle().length() > 31) {
            holder.binding.textTitle.setText(item.getTitle().substring(0, 31) + "...");
        } else {
            holder.binding.textTitle.setText(item.getTitle().toUpperCase());
        }
        String senderChar = String.valueOf(item.getSender().charAt(0));
        if (senderChar.equals('"')){
            senderChar = String.valueOf(item.getSender().charAt(1));
        }

        if (senderChar.equals('"')){
            senderChar = String.valueOf(item.getSender().charAt(2));
        }
        holder.binding.textIcon.setText(senderChar);
        holder.binding.textIcon.setBackgroundTintList( getRandomizedColor(position));

        //String description = String.valueOf(Html.fromHtml(item.getBody().substring(0, 45), Html.FROM_HTML_MODE_COMPACT));
        String fullBody = String.valueOf(Html.fromHtml(item.getBody(), Html.FROM_HTML_MODE_COMPACT));
        //description.trim();
        fullBody = DateFormatter.remove_parenthesis(fullBody, "{}");
        fullBody = DateFormatter.remove_parenthesis(fullBody, "[]");

        fullBody = fullBody.trim();
        fullBody = fullBody.replace(System.getProperty("line.separator"), " ");
        if (fullBody.length() > 99) {
            fullBody = fullBody.substring(0, 100);
        }


        holder.binding.textBody.setText(fullBody);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create fragment for viewing internal files
                Intent intent = new Intent(holder.itemView.getContext(), MailViewActivity.class);


                Bundle b = new Bundle();

                intent.putExtra("mail", item);
                intent.putExtras(b);

                Timber.e("start Activity called twice");
                //begin activity
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public ColorStateList getRandomizedColor(int position){

        if (position > 9){
            position -= 10;
        }
        switch(position){

            case 0:{
                return ColorStateList.valueOf(Color.parseColor("#bada55"));

            }
            case 1:{
                return ColorStateList.valueOf(Color.parseColor("#7fe5f0"));

            }
            case 2:{
                return ColorStateList.valueOf(Color.parseColor("#ff0000"));

            }
            case 3:{
                return ColorStateList.valueOf(Color.parseColor("#ff80ed"));

            }
            case 4:{
                return ColorStateList.valueOf(Color.parseColor("#701894"));

            }
            case 5:{
                return ColorStateList.valueOf(Color.parseColor("#cbcba9"));

            }
            case 6:{
                return ColorStateList.valueOf(Color.parseColor("#420420"));

            }
            case 7:{
                return ColorStateList.valueOf(Color.parseColor("#133337"));

            }
            case 8:{
                return ColorStateList.valueOf(Color.parseColor("#065535"));

            }
            case 9:{
                return ColorStateList.valueOf(Color.parseColor("#800000"));

            }

        }

        return null;
    }


}

