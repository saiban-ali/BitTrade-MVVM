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

public class ChangeUsernameDialog extends DialogFragment {

    private static final String TAG = "ChangeUsernameDialog";

    private IDialogCallBack dialogCallBack;

    private TextView buttonOk;
    private TextView buttonCancel;
    private EditText editText;


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
        View view = inflater.inflate(R.layout.dialog_change_username, container, false);

        buttonOk = view.findViewById(R.id.ok);
        buttonCancel = view.findViewById(R.id.cancel);
        editText = view.findViewById(R.id.edittext);

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

                String username = editText.getText().toString();

                if (username.equals("")) {
                    editText.setError("Field can not be empty");
                    return;
                } else if (username.length() < 5 || username.length() > 15) {
                    editText.setError("username must be between 5 to 15 characters");
                    return;
                }

                dialogCallBack.changeUsername(username);

                Toast.makeText(getActivity(), "Ok Pressed", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    buttonOk.performClick();
                }
                return false;
            }
        });

        return view;
    }
}
