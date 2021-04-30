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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class searchRestaurantAdapter extends ArrayAdapter<JSONObject> {
    private Context mContext;
    private List<JSONObject> searchList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    public searchRestaurantAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<JSONObject> list) {
        super(context, 0 , list);
        mContext = context;
        searchList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        databaseHelper = new DatabaseHelper(mContext);

        if(listItem == null)
            try {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.search_restaurant_item_view, parent, false);
            } catch(Exception e){ }
        JSONObject currentSearch = searchList.get(position);

        try {
            TextView orderId = (TextView) listItem.findViewById(R.id.textView12);
            orderId.setText(String.valueOf(currentSearch.get("Name")));
            TextView orderMenu = (TextView) listItem.findViewById(R.id.textView13);
            orderMenu.setText(String.valueOf(currentSearch.get("Location")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listItem;
    }
}
