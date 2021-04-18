package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epickup.data.model.OrderModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class ViewOrdersActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        databaseHelper = new DatabaseHelper(ViewOrdersActivity.this);


        final ListView viewOrderListView = (ListView) findViewById(R.id.viewOrderListView);

        ArrayList<JSONObject> viewOrder = (ArrayList<JSONObject>) databaseHelper.viewOrder();

        OrderAdapter orderArrayAdapter = new OrderAdapter(this, viewOrder);

        viewOrderListView.setAdapter(orderArrayAdapter);
    }
}