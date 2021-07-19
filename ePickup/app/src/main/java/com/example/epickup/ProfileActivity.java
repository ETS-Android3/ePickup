package com.example.epickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epickup.data.model.UserModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    EditText profileName,profileEmail,profilePassword,profileRestaurantName,profileRestaurantLocation;
    TextView SprofileName,SprofileEmail,SprofilePassword,SprofileRestaurantN,SprofileRestaurantL;
    Button update;
    DatabaseHelper db;
    SharedPreferences sp;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileName =findViewById(R.id.profileName);
        profileEmail=findViewById(R.id.profileEmail);
        profilePassword=findViewById(R.id.profilePassword);
        profileRestaurantName=findViewById(R.id.profileRestaurantName);
        profileRestaurantLocation=findViewById(R.id.profileRestaurantLocation);
        SprofileRestaurantN= findViewById(R.id.SprofileRestaurantN);
        SprofileRestaurantL= findViewById(R.id.SprofileRestaurantL);
        update = findViewById(R.id.profileUpdate);
        db = new DatabaseHelper(this);
        sp = this.getSharedPreferences("sp", MODE_PRIVATE);

        profileEmail.setInputType(EditorInfo.TYPE_NULL);

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);
        if(uM.getRoleId()==1){
        profileRestaurantName.setVisibility(View.GONE);
        profileRestaurantLocation.setVisibility(View.GONE);
            SprofileRestaurantN.setVisibility(View.GONE);
            SprofileRestaurantL.setVisibility(View.GONE);
        } else {
            profileRestaurantName.setVisibility(View.VISIBLE);
            profileRestaurantLocation.setVisibility(View.VISIBLE);
            SprofileRestaurantN.setVisibility(View.VISIBLE);
            SprofileRestaurantL.setVisibility(View.VISIBLE);
        }


        JSONObject profileObj = db.getProfile();
        try {
            profileName.setText(profileObj.getString("Name"));
            profileEmail.setText(profileObj.getString("Email"));
            profilePassword.setText(profileObj.getString("Password"));
            profileRestaurantName.setText(profileObj.getString("RestaurantName"));
            profileRestaurantLocation.setText(profileObj.getString("RestaurantLocation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName = profileName.getText().toString();
                // String pEmail=profileEmail.getText().toString();
                String pPass=profilePassword.getText().toString();
                String pResName=profileRestaurantName.getText().toString();
                String pResLoc=profileRestaurantLocation.getText().toString();

                Boolean checkupdatedata = db.updateuserdata( pName, pPass, pResName, pResLoc);
                if(checkupdatedata==true)
                    Toast.makeText(ProfileActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ProfileActivity.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}