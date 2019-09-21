package com.fyp.bittrade.utils;

import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.User;

public interface IDialogCallBack {
    void changeUsername(String username);
    void changePassword(String newPassword);
    void changeAddress(User user);
}