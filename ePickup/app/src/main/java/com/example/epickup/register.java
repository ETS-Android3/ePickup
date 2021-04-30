package com.example.epickup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epickup.data.model.UserModel;
import com.example.epickup.ui.login.LoginActivity;

public class register extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(register.this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final Button userButton = findViewById(R.id.user);
        final Button managerButton = findViewById(R.id.manager);
        final Button registerButton = findViewById(R.id.register);

        final EditText name = findViewById(R.id.name);
        final EditText email = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);


        final EditText restaurantName = findViewById(R.id.restaurantName);
        final EditText restaurantLocation = findViewById(R.id.restaurantLocation);
        final TextView restaurantText = findViewById(R.id.textView3);

        restaurantName.setVisibility(View.GONE);
        restaurantLocation.setVisibility(View.GONE);
        restaurantText.setVisibility(View.GONE);

        userButton.setOnClickListener(v -> {
            restaurantName.setVisibility(View.GONE);
            restaurantLocation.setVisibility(View.GONE);
            restaurantText.setVisibility(View.GONE);
        });

        managerButton.setOnClickListener(v -> {
            restaurantName.setVisibility(View.VISIBLE);
            restaurantLocation.setVisibility(View.VISIBLE);
            restaurantText.setVisibility(View.VISIBLE);
        });


        registerButton.setOnClickListener(v -> {
            boolean check = databaseHelper.isValidEmail(email.getText().toString());
            if (!check) {
                alertBuilder = new AlertDialog.Builder(register.this);
                alertBuilder.setMessage("Please enter the valid Email ID !");
                alertBuilder.setPositiveButton("OKAY", (arg0, arg1) -> {
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            } else {
                UserModel userModel;

                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                int roleId;
                int restId = -1;
                if (radioButton.getText().toString().equals("User")) {
                    roleId = 1;
                } else {
                    roleId = 2;
                    String[] restaurantInfo = {
                            restaurantName.getText().toString(), restaurantLocation.getText().toString()
                    };
                    restId = databaseHelper.registerRestaurant(restaurantInfo);
                }


                userModel = new UserModel(-1, roleId, restId, name.getText().toString(), email.getText().toString(), password.getText().toString(), 0);
                boolean success = databaseHelper.register(userModel);

                alertDialogBuilder.setMessage("Congratulations! You are registered. Please login to continue.");
                alertDialogBuilder.setPositiveButton("OKAY", (arg0, arg1) -> {
                    Intent loginIntent = new Intent(register.this, LoginActivity.class);
                    startActivity(loginIntent);
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }

        });

    }
}