package com.example.epickup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.epickup.ui.login.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SharedPreferences sp;
    Gson gson = new Gson();

    JSONObject mainObj;
    String displayOrder = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        databaseHelper = new DatabaseHelper(this);
        this.sp = this.getSharedPreferences("sp", MODE_PRIVATE);

        Intent myIntent = getIntent(); // gets the previously created intent
        String mainObjString = myIntent.getStringExtra("mainObj");
        try {
            mainObj = new JSONObject(mainObjString);
            Log.e("mainObjStr",mainObjString);

            JSONArray orderItemList = new JSONArray (String.valueOf(mainObj.get("orderItem")));
            Log.e("orderItemList", String.valueOf(orderItemList));

            double total = 0;
            int estimatedTime = 0;

            for (int i=0;i<orderItemList.length();i++){
                JSONObject tempObj = new JSONObject(String.valueOf(orderItemList.get(i)));

                if(Integer.parseInt(tempObj.get("Quantity").toString())!=0) {
                    displayOrder = displayOrder + tempObj.get("Quantity") + "x " + tempObj.get("Name") + "\t $" + tempObj.get("Cost");
                    displayOrder = displayOrder + "\n";
                }
                total = total + (Double.parseDouble(tempObj.get("Quantity").toString()) * Double.parseDouble(tempObj.get("Cost").toString()));
                estimatedTime = estimatedTime + (Integer.parseInt(tempObj.get("Quantity").toString()) * 5);
            }

            total = Double.parseDouble(String.format("%.2f", total));
            displayOrder = displayOrder + "Total: \t $" + String.valueOf(total);
            Log.e("displayOrder",displayOrder);
            Log.e("total", String.valueOf(total));
            Log.e("estimatedTime", String.valueOf(estimatedTime));

            mainObj.put("Payment",total);
            mainObj.put("EstimatedTime",estimatedTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button paymentPlaceOrderButton = (Button) findViewById(R.id.paymentPlaceOrderButton);
        final TextView orderDisplay = findViewById(R.id.textView21);
        orderDisplay.setText(displayOrder);



        paymentPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean orderPlaced = databaseHelper.placeOrder(mainObj);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
                if(orderPlaced){
                    alertDialogBuilder.setMessage("You order has been placed. You can view you placed orders in View Orders section.");
                    alertDialogBuilder.setPositiveButton("OKAY", (arg0, arg1) -> {
                        Intent loginIntent = new Intent(PaymentActivity.this, HomeActivity.class);
                        startActivity(loginIntent);
                    });

                } else {
                    alertDialogBuilder.setMessage("Something went wrong. Please try again.");
                    alertDialogBuilder.setPositiveButton("OKAY", (arg0, arg1) -> { });
                }
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

    }
}