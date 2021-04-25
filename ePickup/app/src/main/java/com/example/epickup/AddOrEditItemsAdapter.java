package com.example.epickup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddOrEditItemsAdapter extends ArrayAdapter<JSONObject>{
    private Context mContext;
    private List<JSONObject> orderList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    public AddOrEditItemsAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<JSONObject> list) {
        super(context, 0 , list);
        mContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        databaseHelper = new DatabaseHelper(mContext);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        if(listItem == null)
            try {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.add_or_edit_items_item_view, parent, false);
            } catch(Exception e){
//                e.printStackTrace();
            }
        JSONObject currentItem = orderList.get(position);

        try {
            TextView name = (TextView) listItem.findViewById(R.id.textView8);
            name.setText(String.valueOf(currentItem.get("Name")));


            Button deleteItemButton = listItem.findViewById(R.id.deleteItemButton);
            deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    try {
                        databaseHelper.deleteItem((Integer) currentItem.get("Id"));
                        if (mContext instanceof AddOrEditItemsActivity) {
                            ((AddOrEditItemsActivity)mContext).reloadList();
                        }
                        Toast.makeText(mContext,"Item deleted.",Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button editItemButton = listItem.findViewById(R.id.editItemButton);
            editItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        final EditText input = new EditText(mContext);

                        builder.setTitle("Add Item:");
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        String itemName = (String) currentItem.get("Name");
                        input.setText(itemName);
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String editItemName = input.getText().toString();
                                try {
                                    databaseHelper.editItem((Integer) currentItem.get("Id"),editItemName);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (mContext instanceof AddOrEditItemsActivity) {
                                    ((AddOrEditItemsActivity)mContext).reloadList();
                                }
                                Toast.makeText(mContext,"Item successfully edited.",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();

                    } catch (Exception e) {
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
