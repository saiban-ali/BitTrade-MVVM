package com.fyp.bittrade.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fyp.bittrade.R;
import com.fyp.bittrade.utils.IDialogCallBack;

public class ChangePasswordDialog extends DialogFragment {

    private static final String TAG = "ChangePasswordDialog";

    private IDialogCallBack dialogCallBack;

    private TextView buttonOk;
    private TextView buttonCancel;
    private EditText newPassword;
    private EditText confirmPassword;


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
        View view = inflater.inflate(R.layout.dialog_change_password, container, false);

        buttonOk = view.findViewById(R.id.ok);
        buttonCancel = view.findViewById(R.id.cancel);
        newPassword = view.findViewById(R.id.edittext_new_password);
        confirmPassword = view.findViewById(R.id.edittext_confirm_password);

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

                dialogCallBack.changePassword("");

                Toast.makeText(getActivity(), "Ok Pressed", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        // TODO: 9/14/2019 password validation

        return view;
    }
}
