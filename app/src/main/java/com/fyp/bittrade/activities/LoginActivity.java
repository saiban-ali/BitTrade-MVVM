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
import com.fyp.bittrade.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    private TextView registerButton;
    private Context context;
    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar loginProgress;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PreferenceUtil.getUser(this) != null) {

            Intent intent = new Intent(this, MainActivity.class);
            User user = PreferenceUtil.getUser(this);
            intent.putExtra("user", user);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

//            sendLoginRequest(email, PreferenceUtil.getUserPassword(email, this));

        }


        init();

        setUpClickListeners();
    }

    private void setUpClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegistrationActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                loginProgress.setVisibility(View.VISIBLE);
                performLoginOperations();
            }
        });
    }

    private void performLoginOperations() {

        if (validateFields()) {

            Toast.makeText(context, "Validation Successful", Toast.LENGTH_SHORT).show();

//            finish();

            final String email = emailField.getText().toString();
            final String password = passwordField.getText().toString();

            sendLoginRequest(email, password);
        }

    }

    private void sendLoginRequest(final String email, final String password) {

        final User user = new User(email, password);

        Client client = Client.getInstance();
        Retrofit retrofit = client.getClient();
        Service service = retrofit.create(Service.class);
        Call<User> call = service.sendLoginRequest(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
//                        getIntent().putExtra("user", response.body());
//                        setResult(RESULT_OK, new Intent().putExtra("user", response.body()));
                        if (PreferenceUtil.saveUser(response.body(), password, context)) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("user", response.body());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Something went wrong! Login Again", Toast.LENGTH_SHORT).show();
                            loginButton.setVisibility(View.VISIBLE);
                            loginProgress.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();

                    loginButton.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(LoginActivity.this, "Network Error! Login Again", Toast.LENGTH_SHORT).show();

                loginButton.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.GONE);
            }
        });

//        final boolean[] requestSuccessfull = {false};
//
//        Api api = client.getClient(Api.BASE_URL).create(Api.class);
//
//        String email = emailField.getText().toString();
//        String password = passwordField.getText().toString();
//
//        User user = new User(email, password);
//
//        Call<User> call = api.sendLoginRequest(user);
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().isSuccessfull()) {
//                        String message = response.body().getResponseMessage();
//                        String status = response.body().getStatus();
//
//                        Toast.makeText(context,"message: " + message + "\nstatus: " + status, Toast.LENGTH_SHORT).show();
//                        gotoMainActivity(response.body());
//
//                    } else {
//                        Toast.makeText(context, "isSuccessfull: " + response.body().isSuccessfull(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(context, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

//    private void gotoMainActivity(User user) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("user", user);
//
//        startActivity(intent);
//    }

    private boolean validateFields() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            emailField.setError("Invalid Email Format");

            return false;
        }

        if (password.length() < 6) {
            passwordField.setError("make sure password is at least 6 characters long");
            return false;
        }

        return true;
    }

//    private void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.txt_register_now:
//
//                break;
//            case R.id.btn_login:
//
//                break;
//
//        }
//    }

    private void gotoRegistrationActivity() {
        startActivity(new Intent(context, RegistrationActivity.class));
    }

    private void init() {

        context = this;
//        client = new Client();

        registerButton = findViewById(R.id.txt_register_now);
        loginButton = findViewById(R.id.btn_login);
        emailField = findViewById(R.id.input_email);
        passwordField = findViewById(R.id.input_password);
        loginProgress = findViewById(R.id.login_progress);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
