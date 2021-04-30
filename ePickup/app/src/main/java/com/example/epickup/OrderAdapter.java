package com.example.epickup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.epickup.data.model.OrderModel;
import com.example.epickup.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<JSONObject> {


    private Context mContext;
    private List<JSONObject> orderList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    public OrderAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<JSONObject> list) {
        super(context, 0 , list);
        mContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        databaseHelper = new DatabaseHelper(mContext);

        if(listItem == null)
            try {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
            } catch(Exception e){

            }
        JSONObject currentOrder = orderList.get(position);

        try {
            TextView restaurantName = (TextView) listItem.findViewById(R.id.textView1);
            restaurantName.setText(String.valueOf(currentOrder.get("restaurantName")));

            TextView orderId = (TextView) listItem.findViewById(R.id.textView2);
            orderId.setText("Order Id: " + String.valueOf(currentOrder.get("orderId")));
            TextView orderMenu = (TextView) listItem.findViewById(R.id.textView3);
            orderMenu.setText("Orders: " + String.valueOf(currentOrder.get("orderMenu")));
            TextView status = (TextView) listItem.findViewById(R.id.textView4);
            status.setText("Status: " + String.valueOf(currentOrder.get("status")));
            TextView estimatedTime = (TextView) listItem.findViewById(R.id.textView5);
            estimatedTime.setText("Estimated Pickup By: " + String.valueOf(currentOrder.get("estimatedTime")));
            TextView time = (TextView) listItem.findViewById(R.id.textView6);
            time.setText("Latest Pickup By: " + currentOrder.get("time"));
            TextView payment = (TextView) listItem.findViewById(R.id.textView7);
            payment.setText("Total: $" + currentOrder.get("payment"));

            Button submitFeedbackButton = listItem.findViewById(R.id.submitFeedbackButton);
            if(currentOrder.get("feedbackRating").equals(-1)){
                submitFeedbackButton.setEnabled(true);
            } else {
                submitFeedbackButton.setEnabled(false);
            }
            submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    try {
                        Intent goIntent = new Intent(mContext, feedbackActivity.class);
                        goIntent.putExtra("Id", String.valueOf(currentOrder.get("orderId")));
                        mContext.startActivity(goIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    };
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return listItem;
    }
}
