package com.example.epickup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReceiveOrderAdapter extends ArrayAdapter<JSONObject> {
    private Context mContext;
    private List<JSONObject> orderList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    public ReceiveOrderAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<JSONObject> list) {
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
                listItem = LayoutInflater.from(mContext).inflate(R.layout.receive_order_item_view, parent, false);
            } catch(Exception e){
//                e.printStackTrace();
            }
        JSONObject currentOrder = orderList.get(position);

        try {
            TextView orderId = (TextView) listItem.findViewById(R.id.textView1);
            orderId.setText("Order Id: " + String.valueOf(currentOrder.get("orderId")));
            TextView orderMenu = (TextView) listItem.findViewById(R.id.textView2);
            orderMenu.setText("Orders: " + String.valueOf(currentOrder.get("orderMenu")));
            TextView status = (TextView) listItem.findViewById(R.id.textView3);
            status.setText("Status: " + String.valueOf(currentOrder.get("status")));
            TextView estimatedTime = (TextView) listItem.findViewById(R.id.textView4);
            estimatedTime.setText("Estimated Pickup By: " + String.valueOf(currentOrder.get("estimatedTime")));
            TextView time = (TextView) listItem.findViewById(R.id.textView5);
            time.setText("Latest Pickup By: " + currentOrder.get("time"));
            TextView feedbackRating = (TextView) listItem.findViewById(R.id.textView9);
            int fR = Integer.valueOf((String) currentOrder.get("feedbackRating"));
            if(fR != -1){
                feedbackRating.setText("Feedback Rating: " + currentOrder.get("feedbackRating") + "/5");
            } else {
                feedbackRating.setText("Feedback Rating: ");
            }

            TextView feedbackMessage = (TextView) listItem.findViewById(R.id.textView11);
            feedbackMessage.setText("Feedback: " + currentOrder.get("feedbackMessage"));

            TextView payment = (TextView) listItem.findViewById(R.id.textView10);
            payment.setText("Total: $" + currentOrder.get("payment"));

            Button preparedButton = listItem.findViewById(R.id.preparedButton);
            if(String.valueOf(currentOrder.get("status")).equals("Prepared")){
                preparedButton.setEnabled(false);
            } else {
                preparedButton.setEnabled(true);
            }
            preparedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    try {
                        databaseHelper.changeOrderStatus((Integer) currentOrder.get("orderId"));
//                        preparedButton.setEnabled(false);
                        if (mContext instanceof OrdersReceivedActivity) {
                            ((OrdersReceivedActivity)mContext).reloadList();
                        }
                    } catch (JSONException e) {
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
