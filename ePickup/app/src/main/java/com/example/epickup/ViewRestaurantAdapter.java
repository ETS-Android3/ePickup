package com.example.epickup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ViewRestaurantAdapter extends ArrayAdapter<JSONObject> {
    private Context mContext;
    private List<JSONObject> orderList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    SharedPreferences sp;

    public ViewRestaurantAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<JSONObject> list) {
        super(context, 0 , list);
        mContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        databaseHelper = new DatabaseHelper(mContext);
        this.sp = mContext.getSharedPreferences("sp", MODE_PRIVATE);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        if(listItem == null)
            try {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.view_restaurant_item_view, parent, false);
            } catch(Exception e){
//                e.printStackTrace();
            }
        JSONObject currentItem = orderList.get(position);

        try {
            TextView name = (TextView) listItem.findViewById(R.id.textView16);
            name.setText(String.valueOf(currentItem.get("Name")));
            TextView cost = (TextView) listItem.findViewById(R.id.textView17);
            cost.setText(String.valueOf(currentItem.get("Cost")));
            EditText number = (EditText) listItem.findViewById(R.id.editTextNumber);
            int maxLength = 2;
            number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            number.setText("0");

            number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String id = String.valueOf(currentItem.get("Id"));
                        for (int i = 0; i < orderList.size(); i++) {
                            if(id.equals(String.valueOf(orderList.get(i).get("Id")))){
                                orderList.get(i).put("Quantity",number.getText());
                            };
                        }
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("currentOrder", String.valueOf(orderList));
                        editor.commit();

                        JSONArray obj = new JSONArray(String.valueOf(orderList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return listItem;
    }




}