package com.fyp.bittrade.fragments;


import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.GridViewAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.AddProductsRepository;
import com.fyp.bittrade.utils.Constents;
import com.fyp.bittrade.utils.IAddProductCallBack;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.VerifyPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class AddProductImagesFragment extends Fragment implements IAddProductCallBack {

    private static final String TAG = "AddProductImagesFragment";

    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    private Product product;
    private List<Uri> imageUris = new ArrayList<>();

    private IFragmentCallBack fragmentCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentCallBack = (IFragmentCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentCallBack");
        }
    }

    public AddProductImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        product = bundle.getParcelable("product");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product_images, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Select Images");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Button backButton = view.findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.onBackPressed();
            }
        });

        Button nextButton = view.findViewById(R.id.btn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Uploading product", Toast.LENGTH_SHORT).show();
                AddProductsRepository.getInstance(getActivity()).uploadProduct(
                        ((MainActivity) getActivity()).getUser().getId(),
                        product,
                        imageUris,
                        AddProductImagesFragment.this
                );
            }
        });

        Button button = view.findViewById(R.id.btn_select_images);
        gridView = view.findViewById(R.id.grid_view);

        gridViewAdapter = new GridViewAdapter(getActivity());
        gridView.setAdapter(gridViewAdapter);

//        Bitmap tempBitmap = drawableToBitmap(getActivity().getResources().getDrawable(R.drawable.ic_image_black_24dp));

        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(drawableToBitmap(getActivity().getResources().getDrawable(R.drawable.ic_image_black_24dp)));
        }
        gridViewAdapter.setImagesList(list);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VerifyPermissions.verifyStoragePermissions(getActivity());

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), Constents.SELECT_MULTIPLE_IMAGS_REQUEST_CODE);

//                Button btn = new Button(getActivity());
//                btn.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                ));
//                gridLayout.addView(btn);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == getActivity().RESULT_OK ) {
            if(requestCode == Constents.SELECT_MULTIPLE_IMAGS_REQUEST_CODE) {
//                Log.d(TAG, "onActivityResult: Selected");

                if (data.getClipData() == null) {

                    if (data != null) {
                        Uri imageUri = data.getData();
                        imageUris.add(imageUri);
                        Bitmap bitmapImage = null;
                        try {
                            bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        List<Bitmap> images = new ArrayList<>();
                        images.add(bitmapImage);

                        gridViewAdapter.setImagesList(images);
                    }

                } else {
                    ClipData clipData = data.getClipData();

                    if (clipData.getItemCount() > 6) {
                        Toast.makeText(getActivity(), "You can only upload up to 6 images", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Bitmap> imagesList = new ArrayList<>();

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        try {
                            imagesList.add(
                                    MediaStore.Images.Media.getBitmap(
                                            getActivity().getContentResolver(),
                                            clipData.getItemAt(i).getUri()
                                    )
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageUris.add(clipData.getItemAt(i).getUri());
                    }

                    gridViewAdapter.setImagesList(imagesList);
                }
            }
        }

    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * get uri to any resource type
     * @param context - context
     * @param resId - resource id
     * @throws Resources.NotFoundException if the given ID does not exist.
     * @return - Uri to resource by given id
     */
    public Uri getUriToResource(@NonNull Context context,
                                       @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        /** return uri */
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        return uri;
    }

    @Override
    public void onResponseSuccessful(ResponseBody response) {
        Toast.makeText(getActivity(), "Product Uploaded", Toast.LENGTH_SHORT).show();
        fragmentCallBack.loadExploreFragment();
    }

    @Override
    public void onResponseUnsuccessful(ResponseBody responseBody) {
        Toast.makeText(getActivity(), "Something went wrong! try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCallFailed(String message) {
        Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show();
    }
}
