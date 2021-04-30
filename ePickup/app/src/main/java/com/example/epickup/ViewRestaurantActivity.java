package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epickup.data.model.UserModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewRestaurantActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SharedPreferences sp;
    Gson gson = new Gson();

    JSONObject restaurantRecord = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        databaseHelper = new DatabaseHelper(this);
        this.sp = this.getSharedPreferences("sp", MODE_PRIVATE);

        final Button viewRestaurantProceedButton = (Button) findViewById(R.id.viewRestaurantProceedButton);

        Intent myIntent = getIntent(); // gets the previously created intent
        String restaurant = myIntent.getStringExtra("restaurant");
        try {
            restaurantRecord = new JSONObject(restaurant);
            reloadList(restaurantRecord.getInt("Id"));
            TextView name = findViewById(R.id.textView15);
            name.setText(restaurantRecord.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        viewRestaurantProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentOrderString = sp.getString("currentOrder", String.valueOf(MODE_PRIVATE));

                String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
                UserModel uM = gson.fromJson(jsonString, UserModel.class);

                JSONObject mainObj = new JSONObject();
                try {
                    mainObj.put("RestaurantId",restaurantRecord.getInt("Id"));
                    mainObj.put("UserId",uM.getId());
                    mainObj.put("orderItem",currentOrderString);

                    Intent goIntent = new Intent(ViewRestaurantActivity.this, PaymentActivity.class);
                    goIntent.putExtra("mainObj", String.valueOf(mainObj));
                    startActivity(goIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    public void reloadList(int Id){
        final ListView viewRestaurantListView = (ListView) findViewById(R.id.viewRestaurantListView);
        ArrayList<JSONObject> allItems = (ArrayList<JSONObject>) databaseHelper.viewAllItems(Id);
        ViewRestaurantAdapter itemsArrayAdapter = new ViewRestaurantAdapter(this, allItems);
        viewRestaurantListView.setAdapter(itemsArrayAdapter);
    }
}