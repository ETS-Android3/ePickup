package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.epickup.ui.login.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchRestaurantActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText searchRestaurantString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);
        databaseHelper = new DatabaseHelper(SearchRestaurantActivity.this);

        final RadioGroup radioGroup = findViewById(R.id.searchRestaurantRadioGroup);
        final Button searchButton = findViewById(R.id.searchButton);
        searchRestaurantString = findViewById(R.id.searchRestaurantString);
        final ListView searchRestaurantListView = (ListView) findViewById(R.id.searchRestaurantListView);

        searchButton.setOnClickListener(v->{
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            String searchParam = "";
            if(radioButton.getText().toString().equals("Restaurant")){
                searchParam = "Name";
            } else {
                searchParam = "Location";
            }
            reloadList(searchParam);
        });

        searchRestaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject clickedItem = (JSONObject) parent.getItemAtPosition(position);
                Intent goIntent = new Intent(SearchRestaurantActivity.this, ViewRestaurantActivity.class);
                goIntent.putExtra("restaurant",clickedItem.toString());
                startActivity(goIntent);
            }
        });

    }


    public void reloadList(String searchParam){
        if(!searchRestaurantString.getText().toString().isEmpty()) {
            final ListView searchRestaurantListView = (ListView) findViewById(R.id.searchRestaurantListView);
            ArrayList<JSONObject> searchResults = (ArrayList<JSONObject>) databaseHelper.searchRestaurantList(searchRestaurantString.getText().toString(),searchParam);
            searchRestaurantAdapter searchArrayAdapter = new searchRestaurantAdapter(this, searchResults);
            searchRestaurantListView.setAdapter(searchArrayAdapter);
        }
        else{

        }


    }
}