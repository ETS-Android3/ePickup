package com.example.epickup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.epickup.data.model.OrderModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<JSONObject> {


    private Context mContext;
    private List<JSONObject> orderList = new ArrayList<>();

    public OrderAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<JSONObject> list) {
        super(context, 0 , list);
        mContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
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

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return listItem;
    }
}
