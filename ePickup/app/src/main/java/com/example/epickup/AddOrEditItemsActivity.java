package com.example.epickup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddOrEditItemsActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_items);



        databaseHelper = new DatabaseHelper(AddOrEditItemsActivity.this);

        final ListView addOrEditItemsListView = (ListView) findViewById(R.id.addOrEditItemsListView);
        final FloatingActionButton addItem = findViewById(R.id.addItem);
        final Button deleteItemButton = (Button) findViewById(R.id.deleteItemButton);
        final Button editItemButton = (Button) findViewById(R.id.editItemButton);

        reloadList();





        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

//        final Button editItemsUpdateButton = findViewById(R.id.editItemsUpdateButton);

//        final FloatingActionButton addButton = findViewById(R.id.addItemsButton);




        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditItemsActivity.this);

                LinearLayout lila1= new LinearLayout(AddOrEditItemsActivity.this);
                lila1.setOrientation(LinearLayout.VERTICAL);
                final EditText input0 = new EditText(AddOrEditItemsActivity.this);
                final EditText input1 = new EditText(AddOrEditItemsActivity.this);
                input0.setHint("Item Name");
                input1.setHint("Item Cost");
                lila1.addView(input0);
                lila1.addView(input1);
                builder.setView(lila1);
                builder.setTitle("Add Item:");
//                input.setInputType(InputType.TYPE_CLASS_TEXT);
//                builder.setView(input);
//                input2.setInputType(InputType.TYPE_CLASS_TEXT);
//                builder.setView(input2);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String addItemName = input0.getText().toString();
                        String addItemCost = input1.getText().toString();
                        databaseHelper.addItem(addItemName,addItemCost);
                        reloadList();
                        Toast.makeText(AddOrEditItemsActivity.this,"Item successfully added.",Toast.LENGTH_LONG).show();
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
            }
    });



//        addOrEditItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UserModel uM = (UserModel) parent.getItemAtPosition(position);
//                databaseHelper.deleteItem(uM);
//                Toast.makeText(getApplicationContext(),"Item deleted.",Toast.LENGTH_LONG).show();
//
//                ArrayList<JSONObject> allItems = (ArrayList<JSONObject>) databaseHelper.allItems();
//                AddOrEditItemsAdapter itemsArrayAdapter = new AddOrEditItemsAdapter(AddOrEditItemsActivity.this, allItems);
//                addOrEditItemsListView.setAdapter(itemsArrayAdapter);
//            }
//        });


//
//
//        editItemsUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                alertDialogBuilder.setMessage("Item successfully edited.");
//                alertDialogBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1)
//                    {
////                        Intent loginIntent = new Intent(AddOrEditItemsActivity.this, AddOrEditItemsActivity.class);
////                        startActivity(loginIntent);
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//
//
//            }
//        });





    }

    public void reloadList(){
        final ListView addOrEditItemsListView = (ListView) findViewById(R.id.addOrEditItemsListView);
        ArrayList<JSONObject> allItems = (ArrayList<JSONObject>) databaseHelper.allItems();
        AddOrEditItemsAdapter itemsArrayAdapter = new AddOrEditItemsAdapter(this, allItems);
        addOrEditItemsListView.setAdapter(itemsArrayAdapter);
    }


}