package com.fyp.bittrade.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.activities.UploadImageActivity;
import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.dialogs.ChangeAddressDialog;
import com.fyp.bittrade.dialogs.ChangePasswordDialog;
import com.fyp.bittrade.dialogs.ChangeUsernameDialog;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.models.UserResponse;
import com.fyp.bittrade.utils.Constents;
import com.fyp.bittrade.utils.FilePath;
import com.fyp.bittrade.utils.IDialogCallBack;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.PreferenceUtil;
import com.fyp.bittrade.utils.VerifyPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment implements IDialogCallBack {

    private static final String TAG = ProfileFragment.class.getName();

//    private Button editProfile;
    private TextView username;
    private TextView email;
    private TextView address;
    private TextView cityZip;
    private TextView country;
    private TextView phone;
    private ImageView editUsername;
    private Button myOrdersButton;
    private Button myProductsButton;
    private Button logout;
    private Button changeProfilePicture;
    private Button editAddress;
    private Button withdraw;
    private CircleImageView imageView;

    private Uri imageUri;

    private IFragmentCallBack fragmentCallBack;

    private User user;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentCallBack = (IFragmentCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentCallBack");
        }
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Account Settings");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        getActivity().getWindow().setStatusBarColor(Color.RED);

        init(view);

        user = ((MainActivity) getActivity()).getUser();

       if (user != null && user.getContact() != null) {
           address.setText(user.getContact().getAddress());
           cityZip.setText(String.format("%s, %s", user.getContact().getCity(), user.getContact().getZip()));
           country.setText(user.getContact().getCountry());
       }

        if (user != null && user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("")) {
            Glide.with(getActivity())
                    .load(user.getProfileImageUrl())
                    .into(imageView);
        }

        String name = (user != null ? user.getFirstName() : "")
                + " "
                + (user != null ? user.getLastName() : "");

        username.setText(name);
        email.setText(user.getEmail());
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().equals("")) {
            phone.setText(user.getPhoneNumber());
        } else if (user.getContact().getPhone() != null && !user.getContact().getPhone().equals("")) {
            phone.setText(user.getContact().getPhone());
        } else {
            phone.setText("Not Added");
        }

        setOnClickListeners();

        return view;
    }

    private void setOnClickListeners() {
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangeUsernameDialog changeUsernameDialog = new ChangeUsernameDialog();
//                changeUsernameDialog.setTargetFragment(ProfileFragment.this, 1);
//                changeUsernameDialog.show(getFragmentManager(), "ChangeUsernameDialog");
//            }
//        });
//        editUsername.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangeUsernameDialog changeUsernameDialog = new ChangeUsernameDialog();
//                changeUsernameDialog.setTargetFragment(ProfileFragment.this, 1);
//                changeUsernameDialog.show(getFragmentManager(), "ChangeUsernameDialog");
//            }
//        });
//        changePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
//                changePasswordDialog.setTargetFragment(ProfileFragment.this, 5);
//                changePasswordDialog.show(getFragmentManager(), "ChangePasswordDialog");
//            }
//        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAddressDialog changeAddressDialog = new ChangeAddressDialog();
                changeAddressDialog.setTargetFragment(ProfileFragment.this, 6);
                changeAddressDialog.show(getFragmentManager(), "ChangeAddressDialog");
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.logout();
            }
        });

        changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constents.UPLOAD_IMAGE_REQUEST_CODE);
            }
        });

        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).loadMyOrdersFragment();
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "must have at least $5 to withdraw", Toast.LENGTH_SHORT).show();
            }
        });

        myProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadMyProductsFragment();
            }
        });
    }

    private void init(View view) {
//        editProfile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.txt_username);
        email = view.findViewById(R.id.txt_email);
        address = view.findViewById(R.id.address);
        cityZip = view.findViewById(R.id.city_zip);
        country = view.findViewById(R.id.country);
//        editUsername = view.findViewById(R.id.edit_username);
//        changePassword = view.findViewById(R.id.btn_change_password);
        logout = view.findViewById(R.id.btn_logout);
        changeProfilePicture = view.findViewById(R.id.btn_change_dp);
        imageView = view.findViewById(R.id.profile_image);
        editAddress = view.findViewById(R.id.btn_edit_address);
        myOrdersButton = view.findViewById(R.id.btn_orders);
        myProductsButton = view.findViewById(R.id.btn_my_products);
        withdraw = view.findViewById(R.id.btn_withdraw);
        phone = view.findViewById(R.id.phone_number);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(Color.WHITE);
    }

    @Override
    public void changeUsername(String username) {
        this.username.setText(username);
    }

    @Override
    public void changePassword(String newPassword) {
    }

    @Override
    public void changeAddress(User user) {
        this.user.setContact(user.getContact());
        this.user.setPhoneNumber(user.getPhoneNumber());

        address.setText(user.getContact().getAddress());
        String tempCityZip = user.getContact().getCity() + ", " + user.getContact().getZip();
        cityZip.setText(tempCityZip);
        country.setText(user.getContact().getCountry());

        if (!PreferenceUtil.saveAddress(user.getContact().getAddress(), user.getContact().getCity(), user.getContact().getZip(), user.getContact().getCountry(), getActivity())) {
            Toast.makeText(getActivity(), "Not saved to preference", Toast.LENGTH_SHORT).show();
        }

        PreferenceUtil.savePhone(user.getPhoneNumber(), getActivity());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Constents.UPLOAD_IMAGE_REQUEST_CODE) {

                imageUri = data.getData();
                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    imageView.setImageBitmap(bitmapImage);
                    uploadImage();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Unable to get image try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), "Pic Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {

        VerifyPermissions.verifyStoragePermissions(getActivity());

        Client client = Client.getInstance();
        Retrofit retrofit = client.getClient();
        Service service = retrofit.create(Service.class);

        File imageFile = new File(FilePath.getPath(getActivity(), imageUri));

        RequestBody fileReqBody = RequestBody.create(imageFile, MediaType.parse(getActivity().getContentResolver().getType(imageUri)));
        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), fileReqBody);

        Call<UserResponse> call = service.uploadImage(part, ((MainActivity)getActivity()).getUser().getId());

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                    if (!PreferenceUtil.saveImageUrl(response.body().getUser().getProfileImageUrl(), getActivity())) {
                        Toast.makeText(getActivity(), "Something went wrong! Upload Again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
