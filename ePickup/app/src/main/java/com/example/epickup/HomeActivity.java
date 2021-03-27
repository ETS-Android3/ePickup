package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.epickup.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        final Button logoutButton = findViewById(R.id.logoutButton);
        final Button profileButton = findViewById(R.id.viewProfileButton);
        final Button addOrEditItemsButton = findViewById(R.id.addOrEditItemsButton);
        final Button viewOrdersButton = findViewById(R.id.viewOrdersButton);
        final Button searchRestaurantButton = findViewById(R.id.searchRestaurantButton);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(goIntent);
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(goIntent);
            }
        });

        addOrEditItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, AddOrEditItemsActivity.class);
                startActivity(goIntent);
            }
        });

        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, ViewOrdersActivity.class);
                startActivity(goIntent);
            }
        });

        searchRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, SearchRestaurantActivity.class);
                startActivity(goIntent);
            }
        });

    }
}