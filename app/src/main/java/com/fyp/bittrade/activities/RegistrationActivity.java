package com.fyp.bittrade.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.bittrade.R;
import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.models.UserResponse;
import com.fyp.bittrade.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getName();

    private TextView loginNowButton;
    private Context context;
    private Button registerButton;

    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;

    private ProgressBar registerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        firstNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String firstName = firstNameField.getText().toString();
                    if(firstName.length() < 3 || firstName.length() > 12) {
                        firstNameField.setError("First Name must be between 3-12 characters");
                    }
                }
            }
        });

        lastNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String firstName = lastNameField.getText().toString();
                    if(firstName.length() < 3 || firstName.length() > 12) {
                        lastNameField.setError("First Name must be between 3-12 characters");
                    }
                }
            }
        });

        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String email = emailField.getText().toString();
                    if(email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                        emailField.setError("Invalid Email Format");
                    }
                }
            }
        });

        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String password = passwordField.getText().toString();
                    if(password.isEmpty() || (password.length() < 6)) {
                        passwordField.setError("password must be at least 6 characters");
                    }
                }
            }
        });

        confirmPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String password = confirmPasswordField.getText().toString();
                    if(!passwordField.getText().toString().equals(password)) {
                        confirmPasswordField.setError("passwords don't match");
                    }
                }
            }
        });

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
                v.setVisibility(View.GONE);
                registerProgress.setVisibility(View.VISIBLE);
                performRegisterOperations();
            }
        });
    }

    private void performRegisterOperations() {
        if (validateFields()) {

            Toast.makeText(context, "Validation Successful", Toast.LENGTH_SHORT).show();

            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            sendRegisterRequest(firstName, lastName, email, password);
        } else {
            registerButton.setVisibility(View.VISIBLE);
            registerProgress.setVisibility(View.GONE);
        }
    }

    private void sendRegisterRequest(String firstName, String lastName, String email, final String password) {


        final User user = new User(firstName, lastName, email, password);

        Client client = Client.getInstance();
        Retrofit retrofit = client.getClient();
        Service service = retrofit.create(Service.class);
        Call<UserResponse> call = service.registerUser(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
//                        getIntent().putExtra("user", response.body());
//                        setResult(RESULT_OK, new Intent().putExtra("user", response.body()));
                        if (PreferenceUtil.saveUser(response.body().getUser(), password, context)) {
                            gotoUploadImageActivity(response.body().getUser());
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Something went wrong! Register Again", Toast.LENGTH_SHORT).show();
                            registerButton.setVisibility(View.VISIBLE);
                            registerProgress.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Something went wrong! Register Again", Toast.LENGTH_SHORT).show();

                    registerButton.setVisibility(View.VISIBLE);
                    registerProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(RegistrationActivity.this, "Network Error! Register Again", Toast.LENGTH_SHORT).show();

                registerButton.setVisibility(View.VISIBLE);
                registerProgress.setVisibility(View.GONE);
            }
        });
    }

    private void gotoUploadImageActivity(User user) {
        Intent intent = new Intent(context, UploadImageActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private boolean validateFields() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (firstName.length() < 3 || firstName.length() > 12) {
            firstNameField.setError("First Name must be between 3-12 characters");

            return false;
        }

        if (lastName.length() < 3 || lastName.length() > 12) {
            lastNameField.setError("First Name must be between 3-12 characters");

            return false;
        }

        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            emailField.setError("Invalid Email Format");

            return false;
        }

        if (password.length() < 6) {
            passwordField.setError("password must be at least 6 characters");

            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("passwords don't match");

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

        firstNameField = findViewById(R.id.input_first_name);
        lastNameField = findViewById(R.id.input_last_name);
        passwordField = findViewById(R.id.input_password);
        emailField = findViewById(R.id.input_email);
        confirmPasswordField = findViewById(R.id.input_confirm_password);

        registerProgress = findViewById(R.id.register_progress);
    }
}
