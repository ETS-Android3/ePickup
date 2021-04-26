package com.example.epickup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.epickup.data.model.UserModel;
import com.example.epickup.ui.login.LoginActivity;
import com.example.epickup.DatabaseHelper;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SharedPreferences sp;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(HomeActivity.this);
        sp = this.getSharedPreferences("sp", MODE_PRIVATE);

        final Button logoutButton = findViewById(R.id.logoutButton);
        final Button profileButton = findViewById(R.id.viewProfileButton);
        final Button addOrEditItemsButton = findViewById(R.id.addOrEditItemsButton);
        final Button viewOrdersButton = findViewById(R.id.viewOrdersButton);
        final Button ordersReceivedButton = findViewById(R.id.ordersReceivedButton);
        final Button searchRestaurantButton = findViewById(R.id.searchRestaurantButton);

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        if(uM.getRoleId()==1){
            addOrEditItemsButton.setVisibility(View.GONE);
            ordersReceivedButton.setVisibility(View.GONE);
            searchRestaurantButton.setVisibility(View.VISIBLE);
            viewOrdersButton.setVisibility(View.VISIBLE);
        } else {
            addOrEditItemsButton.setVisibility(View.VISIBLE);
            ordersReceivedButton.setVisibility(View.VISIBLE);
            searchRestaurantButton.setVisibility(View.GONE);
            viewOrdersButton.setVisibility(View.GONE);
        }


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DatabaseHelper databaseHelper = new DatabaseHelper(HomeActivity.this);
                boolean logoutSuccess = databaseHelper.logout(true);
                if(logoutSuccess) {
                    Intent goIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(goIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Logout Failed.",Toast.LENGTH_LONG).show();
                }
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

        ordersReceivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, OrdersReceivedActivity.class);
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


    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}