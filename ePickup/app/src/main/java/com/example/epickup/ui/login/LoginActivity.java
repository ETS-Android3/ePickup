package com.example.epickup.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.epickup.DatabaseHelper;
import com.example.epickup.HomeActivity;
import com.example.epickup.R;
import com.example.epickup.forgotPassword;
import com.example.epickup.register;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    DatabaseHelper databaseHelper;
    SharedPreferences sp;

    AlertDialog.Builder alertBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


//        databaseHelper = new DatabaseHelper(LoginActivity.this);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.registerNow);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView fPass = findViewById(R.id.fPass);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                boolean check = databaseHelper.isValidEmail(usernameEditText.getText().toString());
//                if (!check) {
//                    alertBuilder = new AlertDialog.Builder(LoginActivity.this);
//                    alertBuilder.setMessage("Please enter the valid Email ID !");
//                    alertBuilder.setPositiveButton("OKAY", (arg0, arg1) -> {
//                    });
//                    AlertDialog alertDialog = alertBuilder.create();
//                    alertDialog.show();
//                } else {
                    String[] creds = new String[]{usernameEditText.getText().toString(), passwordEditText.getText().toString()};
                    DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
                    boolean login_success = databaseHelper.login(creds);
                    if (login_success) {
                        Intent goIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(goIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Credentials invalid.", Toast.LENGTH_LONG).show();
                    }
//                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, register.class);
                startActivity(registerIntent);
            }
        });

        fPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, forgotPassword.class);
                startActivity(forgotPasswordIntent);
            }
        });

        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        String userObjectString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        if (userObjectString.equals("0")) {

        } else {
            Intent goIntent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(goIntent);
        }

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}