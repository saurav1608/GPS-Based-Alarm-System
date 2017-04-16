package com.example.mahe.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuPage extends AppCompatActivity {

    ImageButton addgeo,addalrm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        addgeo=(ImageButton)findViewById(R.id.addgeo);
        addalrm=(ImageButton)findViewById(R.id.addAlarm);
        addgeo();
        addAlarm();
    }
    public  void addgeo()
    {
        addgeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x=new Intent(MenuPage.this,MapsActivity.class);
                startActivity(x);
            }
        });
    }
    public  void addAlarm()
    {
        addalrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x=new Intent(MenuPage.this,AddAlarm.class);
                startActivity(x);
            }
        });
    }
}
