package com.fyp.bittrade.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.bittrade.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {

    private static final String TAG = FavoritesViewModel.class.getName();

    private MutableLiveData<List<Product>> mutableLiveData;
    private List<Product> list = new ArrayList<>();
    private MutableLiveData<Double> priceLiveData;

    public MutableLiveData<List<Product>> getMutableLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            priceLiveData = new MutableLiveData<>();
            priceLiveData.setValue(0.0);
        }
        return mutableLiveData;
    }

    public MutableLiveData<Double> getPriceLiveData() {
        if (priceLiveData == null) {
            priceLiveData = new MutableLiveData<>();
            priceLiveData.setValue(0.0);
        }

        return priceLiveData;
    }

    public List<Product> getProductsList() {
        List<Product> list = mutableLiveData.getValue();
        return list;
    }

    public List<Product> getList() {
        return list;
    }

    public double getPrice() {
        double price = 0.0;
        if (priceLiveData.getValue() != null)
            price = priceLiveData.getValue();

        return price;
    }

    public void setList(List<Product> list) {
        this.list = list;
        mutableLiveData.setValue(list);
    }

    public void add(Product p) {
        list.add(p);
        double prePrice = priceLiveData.getValue();
        double newPrice = prePrice + p.getPrice();
        priceLiveData.setValue(newPrice);
        mutableLiveData.setValue(list);
    }

    public void remove(Product p) {
        list.remove(p);
        double prePrice = priceLiveData.getValue();
        double newPrice = prePrice - p.getPrice();
        priceLiveData.setValue(newPrice);
        mutableLiveData.setValue(list);
    }

    public boolean hasProduct(Product p) {
        return list.contains(p);
    }

}
