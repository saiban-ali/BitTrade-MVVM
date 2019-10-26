package com.fyp.bittrade.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.FilePath;
import com.fyp.bittrade.utils.IAddProductCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductsRepository {

    private static final String TAG = "AddProductsRepository";

    private static AddProductsRepository INSTANCE;
    private Context context;

    public AddProductsRepository(Context context) {
        this.context = context;
    }

    public static AddProductsRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AddProductsRepository(context);
        }

        return INSTANCE;
    }

    public void uploadProduct(String userId, Product product, List<Uri> imageUris, final IAddProductCallBack callBack) {

        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("title", createPartFromString(product.getTitle()));
        partMap.put("description", createPartFromString(product.getDescription()));
        partMap.put("stock", createPartFromString(Integer.toString(product.getStock())));
        partMap.put("price", createPartFromString(Double.toString(product.getPrice())));
        partMap.put("category", createPartFromString(product.getCategory()));
        partMap.put("brand", createPartFromString(product.getBrand()));

        List<MultipartBody.Part> parts = new ArrayList<>();

        int count = 1;
        for (Uri uri :
                imageUris) {

            parts.add(prepareFilePart("image", uri));
            count++;
        }



        Client.getInstance()
                .getClient()
                .create(Service.class)
                .uploadProduct(userId, partMap, parts)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            callBack.onResponseSuccessful(response.body());
                            Log.d(TAG, "onResponse: " + response.body());
                        } else {
                            callBack.onResponseUnsuccessful(response.body());
                            Log.d(TAG, "onResponseUnsuccessful: " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        callBack.onCallFailed(t.getMessage());
                        Log.e(TAG, "onFailure: Call Failed", t);
                    }
                });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File imageFile = new File(FilePath.getPath(context, fileUri));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        imageFile,
                        MediaType.parse(context.getContentResolver().getType(fileUri))
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(descriptionString, okhttp3.MultipartBody.FORM);
    }
}
