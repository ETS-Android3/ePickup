package com.example.epickup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epickup.ui.login.LoginActivity;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final Button userButton = findViewById(R.id.user);
        final Button managerButton = findViewById(R.id.manager);
        final Button registerButton = findViewById(R.id.register);

        final EditText restaurantName = findViewById(R.id.restaurantName);
        final EditText restaurantLocation = findViewById(R.id.restaurantLocation);
        final TextView restaurantText = findViewById(R.id.textView3);

        restaurantName.setVisibility(View.GONE);
        restaurantLocation.setVisibility(View.GONE);
        restaurantText.setVisibility(View.GONE);

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                restaurantName.setVisibility(View.GONE);
                restaurantLocation.setVisibility(View.GONE);
                restaurantText.setVisibility(View.GONE);
            }
        });

        managerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                restaurantName.setVisibility(View.VISIBLE);
                restaurantLocation.setVisibility(View.VISIBLE);
                restaurantText.setVisibility(View.VISIBLE);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                alertDialogBuilder.setMessage("Congratulations! You are registered. Please login to continue.");
                alertDialogBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Intent loginIntent = new Intent(register.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

    }
}