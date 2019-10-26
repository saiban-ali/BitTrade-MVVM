package com.fyp.bittrade.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.fyp.bittrade.R;
import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.models.UserResponse;
import com.fyp.bittrade.utils.Constents;
import com.fyp.bittrade.utils.FilePath;
import com.fyp.bittrade.utils.PreferenceUtil;
import com.fyp.bittrade.utils.VerifyPermissions;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadImageActivity extends AppCompatActivity {

    private static final String TAG = "UploadImageActivity";

    private CircleImageView profileImage;
    private Button uploadButton;
    private TextView skipButton;
    private Button doneButton;
    private ProgressBar uploadImageProgress;
    private Context context;
    private Activity activity = this;

    private User user;

    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        user = getIntent().getParcelableExtra("user");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Upload Image");
        toolbar.setTitleTextColor(Color.RED);
        setSupportActionBar(toolbar);

        init();

        setUpListeners();
    }

    private void setUpListeners() {
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyPermissions.verifyStoragePermissions(activity);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, Constents.UPLOAD_IMAGE_REQUEST_CODE);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddAddressActivity();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri == null) {
                    Toast.makeText(UploadImageActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                doneButton.setVisibility(View.GONE);
                uploadImageProgress.setVisibility(View.VISIBLE);

                Client client = Client.getInstance();
                Retrofit retrofit = client.getClient();
                Service service = retrofit.create(Service.class);

                File imageFile = new File(FilePath.getPath(context, imageUri));

                RequestBody fileReqBody = RequestBody.create(imageFile, MediaType.parse(getContentResolver().getType(imageUri)));
                MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), fileReqBody);

                Call<UserResponse> call = service.uploadImage(part, user.getId());

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UploadImageActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            doneButton.setVisibility(View.VISIBLE);
                            uploadImageProgress.setVisibility(View.GONE);
                            user = response.body().getUser();
                            if (PreferenceUtil.saveImageUrl(response.body().getUser().getProfileImageUrl(), context)) {
                                gotoAddAddressActivity();
                            } else {
                                Toast.makeText(UploadImageActivity.this, "Something went wrong! Upload Again", Toast.LENGTH_SHORT).show();
                                doneButton.setVisibility(View.VISIBLE);
                                profileImage.setVisibility(View.GONE);
                            }

                        } else {
                            doneButton.setVisibility(View.VISIBLE);
                            uploadImageProgress.setVisibility(View.GONE);
                            Toast.makeText(UploadImageActivity.this, "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        doneButton.setVisibility(View.VISIBLE);
                        uploadImageProgress.setVisibility(View.GONE);
                        Toast.makeText(UploadImageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putParcelable("contact", new Contact());
        intent.putExtra("userBundle", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void gotoAddAddressActivity() {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("user", user);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void init() {
        context = this;

        profileImage = findViewById(R.id.profile_image);
        uploadButton = findViewById(R.id.btn_upload_image);
        skipButton = findViewById(R.id.txt_skip);
        doneButton = findViewById(R.id.btn_done);
        uploadImageProgress = findViewById(R.id.upload_image_progress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constents.UPLOAD_IMAGE_REQUEST_CODE) {

                imageUri = data.getData();
                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    profileImage.setImageBitmap(bitmapImage);
                } catch (IOException e) {
                    Toast.makeText(this, "Unable to get image try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Toast.makeText(this, "Pic Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
