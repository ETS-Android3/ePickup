package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.epickup.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        final Button logoutButton = findViewById(R.id.logoutButton);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(goIntent);
            }
        });

    }
}