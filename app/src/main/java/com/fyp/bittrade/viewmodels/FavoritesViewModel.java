package com.fyp.bittrade.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.bittrade.models.Product;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private static final String TAG = FavoritesViewModel.class.getName();

    private MutableLiveData<List<Product>> mutableLiveData;
    private List<Product> list = new ArrayList<>();

    public MutableLiveData<List<Product>> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();

        return mutableLiveData;
    }

//    public List<Product> getProductsList() {
//        List<Product> list = mutableLiveData.getValue();
//        return list;
//    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
        mutableLiveData.setValue(list);
    }

    public void add(Product p) {
        list.add(p);
        mutableLiveData.setValue(list);
    }

    public void remove(Product p) {
        list.remove(p);
        mutableLiveData.setValue(list);
    }

    public boolean hasProduct(Product p) {
        return list.contains(p);
    }
}
