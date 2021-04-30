package com.example.epickup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.epickup.ui.login.LoginActivity;

public class forgotPassword extends AppCompatActivity {
    AlertDialog.Builder alertBuilder;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        databaseHelper = new DatabaseHelper(this);

        EditText forgotPass = (EditText) findViewById(R.id.forgotPass);
        Button submit = (Button) findViewById(R.id.submitpass);
        alertBuilder = new AlertDialog.Builder(this);
        submit.setOnClickListener(v -> {
            String email = forgotPass.getText().toString();

            boolean check = databaseHelper.isValidEmail(email);
            if (!check) {
                alertBuilder.setMessage("Please enter the valid Email ID !");
                alertBuilder.setPositiveButton("OKAY", (arg0, arg1) -> {
                });

                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            } else {
                alertBuilder.setMessage("Password Reset Email is sent to your Email Id : " + email);
                alertBuilder.setPositiveButton("OKAY", (arg0, arg1) -> {
                    Intent loginIntent = new Intent(forgotPassword.this, LoginActivity.class);
                    startActivity(loginIntent);
                });

                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

    }
}