package com.fyp.bittrade.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.bittrade.R;
import com.fyp.bittrade.dialogs.ChangePasswordDialog;
import com.fyp.bittrade.dialogs.ChangeUsernameDialog;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.utils.IDialogCallBack;
import com.fyp.bittrade.utils.IFragmentCallBack;

public class ProfileFragment extends Fragment implements IDialogCallBack {

    private static final String TAG = ProfileFragment.class.getName();

//    private Button editProfile;
    private TextView username;
    private TextView email;
    private ImageView editUsername;
    private Button changePassword;
    private Button logout;

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

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        User user = getArguments().getParcelable("user");

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Account Settings");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        getActivity().getWindow().setStatusBarColor(Color.RED);

        init(view);

        username.setText(user.getUsername());
        email.setText(user.getEmail());

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
        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUsernameDialog changeUsernameDialog = new ChangeUsernameDialog();
                changeUsernameDialog.setTargetFragment(ProfileFragment.this, 1);
                changeUsernameDialog.show(getFragmentManager(), "ChangeUsernameDialog");
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
                changePasswordDialog.setTargetFragment(ProfileFragment.this, 1);
                changePasswordDialog.show(getFragmentManager(), "ChangePasswordDialog");
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.logout();
            }
        });
    }

    private void init(View view) {
//        editProfile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.txt_username);
        email = view.findViewById(R.id.txt_email);
        editUsername = view.findViewById(R.id.edit_username);
        changePassword = view.findViewById(R.id.btn_change_password);
        logout = view.findViewById(R.id.btn_logout);
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

        // TODO: make server call to update username
    }

    @Override
    public void changePassword(String newPassword) {
        // TODO: 9/14/2019 make server call to change password
    }


}
