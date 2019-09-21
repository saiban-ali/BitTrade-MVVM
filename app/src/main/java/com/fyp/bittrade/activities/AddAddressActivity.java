package com.fyp.bittrade.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fyp.bittrade.R;
import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.models.UserResponse;
import com.fyp.bittrade.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddAddressActivity extends AppCompatActivity {

    String[] countryList = {
            "Select Country", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
    };

    private Button nextButton;
    private EditText address;
    private EditText city;
    private EditText zip;
    private EditText phone;
    private Spinner spinner;
    private TextView skipButton;
    private ProgressBar progressBar;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        if (getIntent().hasExtra("user")) {
            user = getIntent().getParcelableExtra("user");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Address");
        toolbar.setTitleTextColor(Color.RED);
        setSupportActionBar(toolbar);

        init();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);

                String tempAddress = address.getText().toString();
                String tempCity = address.getText().toString();
                String tempZip = zip.getText().toString();
                String tempPhone = phone.getText().toString();
                String tempCountry = spinner.getSelectedItem().toString();

                Client client = Client.getInstance();
                Retrofit retrofit = client.getClient();
                Service service = retrofit.create(Service.class);

                Call<UserResponse> call = service.addAddress(new Contact(tempAddress, tempCity, tempCountry, tempZip, tempPhone), user.getId());

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddAddressActivity.this, "Response Successful", Toast.LENGTH_SHORT).show();

                            if (response.body() != null) {
                                progressBar.setVisibility(View.GONE);
                                nextButton.setVisibility(View.VISIBLE);

                                user = response.body().getUser();
                                if (PreferenceUtil.saveAddress(user.getContact().getAddress(), user.getContact().getCity(), user.getContact().getZip(), user.getContact().getCountry(), AddAddressActivity.this)) {
                                    gotoMainActivity();
                                } else {
                                    Toast.makeText(AddAddressActivity.this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddAddressActivity.this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                nextButton.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(AddAddressActivity.this, "Response Not Successful", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            nextButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(AddAddressActivity.this, "Call Failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putParcelable("contact", user.getContact());
        intent.putExtra("userBundle", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void init() {
        spinner = findViewById(R.id.country);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        zip = findViewById(R.id.zip);
        city = findViewById(R.id.city);
        nextButton = findViewById(R.id.btn_next);
        skipButton = findViewById(R.id.txt_skip);
        progressBar = findViewById(R.id.progress);
    }
}
