package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersReceivedActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_received);

        databaseHelper = new DatabaseHelper(OrdersReceivedActivity.this);

        reloadList();
    }

    public void reloadList(){
        final ListView ordersReceivedView = (ListView) findViewById(R.id.ordersReceivedView);

        ArrayList<JSONObject> receivedOrder = (ArrayList<JSONObject>) databaseHelper.receivedOrder();

        ReceiveOrderAdapter orderArrayAdapter = new ReceiveOrderAdapter(this, receivedOrder);

        ordersReceivedView.setAdapter(orderArrayAdapter);
    }
}