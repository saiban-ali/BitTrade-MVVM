package com.fyp.bittrade.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.bittrade.R;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getName();

    private TextView loginNowButton;
    private Context context;
    private Button registerButton;

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        setUpClickListeners();
    }

    private void setUpClickListeners() {
        loginNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginActivity();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegisterOperations();
            }
        });
    }

    private void performRegisterOperations() {
        if (validateFields()) {

            Toast.makeText(context, "Validation Successfull", Toast.LENGTH_SHORT).show();

            if (sendRegisterRequest())
                gotoMainActivity();
        }
    }

    private boolean sendRegisterRequest() {
        return true;
    }

    private void gotoMainActivity() {
        startActivity(new Intent(context, MainActivity.class));
    }

    private boolean validateFields() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String name = nameField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "password and confirm password don't match", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "password and confirm password don't match", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(context, "Invalid Emial Format", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(context, "make sure password is at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void gotoLoginActivity() {
        finish();
    }

    private void init() {

        context = this;
        loginNowButton = findViewById(R.id.txt_login_now);
        registerButton = findViewById(R.id.btn_regiter);

        nameField = findViewById(R.id.input_name);
        passwordField = findViewById(R.id.input_password);
        emailField = findViewById(R.id.input_email);
        confirmPasswordField = findViewById(R.id.input_confirm_password);
    }
}
