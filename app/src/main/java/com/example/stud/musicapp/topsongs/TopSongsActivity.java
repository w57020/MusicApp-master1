package com.example.stud.musicapp.topsongs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.stud.musicapp.R;

public class TopSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_songs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public boolean onSupportNavigateUp(){



        onBackPressed();
        return true;
    }



}
