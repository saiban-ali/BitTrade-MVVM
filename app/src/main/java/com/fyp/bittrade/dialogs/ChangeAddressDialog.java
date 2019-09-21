package com.fyp.bittrade.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.AddAddressActivity;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.UserResponse;
import com.fyp.bittrade.utils.IDialogCallBack;
import com.fyp.bittrade.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeAddressDialog extends DialogFragment {

    private static final String TAG = "ChangePasswordDialog";

    private IDialogCallBack dialogCallBack;

    private EditText address;
    private EditText city;
    private EditText zip;
    private EditText phone;
    private Spinner spinner;

    private TextView buttonOk;
    private TextView buttonCancel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dialogCallBack = (IDialogCallBack) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException", e);
            Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_address, container, false);

        spinner = view.findViewById(R.id.country);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone);
        zip = view.findViewById(R.id.zip);
        city = view.findViewById(R.id.city);

        buttonOk = view.findViewById(R.id.ok);
        buttonCancel = view.findViewById(R.id.cancel);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.countries_array, R.layout.spinner_layout);
        spinner.setPrompt("Select Country");
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cancel Pressed", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonOk.setClickable(false);

                String tempAddress = address.getText().toString();
                String tempCity = city.getText().toString();
                String tempZip = zip.getText().toString();
                String tempPhone = phone.getText().toString();
                String tempCountry = spinner.getSelectedItem().toString();

                if(tempAddress.isEmpty()) {
                    address.setError("Enter address");
                    return;
                } else if (tempCity.isEmpty()) {
                    city.setError("Enter city");
                    return;
                } else if (tempZip.isEmpty()) {
                    zip.setError("Enter zip");
                    return;
                } else if (tempPhone.isEmpty()) {
                    phone.setError("Enter phone");
                    return;
                } else if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Select Country", Toast.LENGTH_SHORT).show();
                    return;
                }



                Client client = Client.getInstance();
                Retrofit retrofit = client.getClient();
                Service service = retrofit.create(Service.class);

                Call<UserResponse> call = service.addAddress(new Contact(tempAddress, tempCity, tempCountry, tempZip, tempPhone), ((MainActivity) getActivity()).getUser().getId());

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                        buttonOk.setClickable(true);

                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Response Successful", Toast.LENGTH_SHORT).show();



                            if (response.body() != null) {

                                dialogCallBack.changeAddress(response.body().getUser());
                                getDialog().dismiss();

                            } else {
                                Toast.makeText(getActivity(), "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Response Not Successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {

                        buttonOk.setClickable(true);

                        Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getActivity(), "Ok Pressed", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }
}
