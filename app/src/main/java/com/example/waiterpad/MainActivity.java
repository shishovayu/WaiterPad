package com.example.waiterpad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button menuButton;
    Button ordersButton;
    Button editMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuButton = (Button) findViewById(R.id.menuButton);
        ordersButton = (Button) findViewById(R.id.ordersButton);
        editMenu = (Button) findViewById(R.id.editMenu);


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("Menu"));
            }
        });

        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("Orders"));
            }
        });

        editMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("TestClass"));
            }
        });

    }


    DBHelper dbHelper;

}
