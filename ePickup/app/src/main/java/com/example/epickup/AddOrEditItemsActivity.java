package com.example.epickup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.epickup.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddOrEditItemsActivity extends AppCompatActivity {
    public String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_items);


        final FloatingActionButton addButton = findViewById(R.id.addItemsButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        builder.setTitle("Add Item:");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                startActivity(getIntent());

            }
        });
        builder.setCancelable(false);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                builder.show();
            }
    });
    }


}