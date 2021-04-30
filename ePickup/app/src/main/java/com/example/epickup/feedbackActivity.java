package com.example.epickup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.epickup.ui.login.LoginActivity;

public class feedbackActivity extends AppCompatActivity {
    EditText mFeedback;
    Button mSendFeedback;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        TextView mRatingScale = (TextView) findViewById(R.id.ratingres);
        mFeedback = (EditText) findViewById(R.id.feedback);
        mSendFeedback= (Button) findViewById(R.id.fBtn);

        Intent myIntent = getIntent(); // gets the previously created intent
        String orderId = myIntent.getStringExtra("Id");

        db = new DatabaseHelper(this);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                mRatingScale.setText(String.valueOf(rating));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });
        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFeedback.getText().toString().isEmpty()) {
                   Toast.makeText(feedbackActivity.this,"Please provide the Feedback",Toast.LENGTH_LONG).show();
                } else {
                    String feedbackmessage=mFeedback.getText().toString();
                    int feedbackRating = (int)mRatingBar.getRating();
//                    String orderId = "1";
                    boolean checkfeedback = db.feedbackData(feedbackRating,feedbackmessage,orderId);
                    if(checkfeedback==true){
                        mFeedback.setText("");
                        mRatingBar.setRating(0);
                        Toast.makeText(feedbackActivity.this, "Thank you for your Feedback", Toast.LENGTH_LONG).show();
                        Intent goIntent = new Intent(feedbackActivity.this, HomeActivity.class);
                        startActivity(goIntent);
                    }
                    else{
                        Toast.makeText(feedbackActivity.this, "failed", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
