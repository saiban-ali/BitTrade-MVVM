package com.fyp.bittrade.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.CartRepository;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class CartViewModel extends ViewModel {

    private static final String TAG = FavoritesViewModel.class.getName();

    private MutableLiveData<List<Product>> mutableLiveData;
    private List<Product> list = new ArrayList<>();
    private MutableLiveData<Double> priceLiveData;
    private CartRepository cartRepository;

    public CartViewModel() {
        cartRepository = CartRepository.getInstance();
    }

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

    public void setPriceLiveData(double price) {
        priceLiveData.setValue(price);
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

    public void add(Product p, String userId, CartRepository.IResponseAddCartCallBack responseCart) {
        p.incrementProductCount();
        list.add(p);
        double prePrice = priceLiveData.getValue();
        double newPrice = prePrice + p.getPrice();
        priceLiveData.setValue(newPrice);
        mutableLiveData.setValue(list);

        cartRepository.addToCart(p.getId(), userId, responseCart);
    }

    public void remove(final Product p, String userId, final CartRepository.IResponseAddCartCallBack responseCart) {
        list.remove(p);
        final int productCount = p.getProductCount();
        p.setProductCount(0);
        double prePrice = priceLiveData.getValue();
        double newPrice = prePrice - p.getPrice();
        priceLiveData.setValue(newPrice);
        mutableLiveData.setValue(list);

        cartRepository.removeFromCart(p.getId(), userId, new CartRepository.IResponseAddCartCallBack() {
            @Override
            public void onResponseSuccessful(ResponseBody response) {
                responseCart.onResponseSuccessful(response);
            }

            @Override
            public void onResponseUnsuccessful(ResponseBody responseBody) {
                responseCart.onResponseUnsuccessful(responseBody);
                list.add(p);
                p.setProductCount(productCount);
                double prePrice = priceLiveData.getValue();
                double newPrice = prePrice + p.getPrice();
                priceLiveData.setValue(newPrice);
                mutableLiveData.setValue(list);
            }

            @Override
            public void onCallFailed(String message) {
                responseCart.onCallFailed(message);
                list.add(p);
                p.setProductCount(productCount);
                double prePrice = priceLiveData.getValue();
                double newPrice = prePrice + p.getPrice();
                priceLiveData.setValue(newPrice);
                mutableLiveData.setValue(list);
            }
        });
    }

    public void incrementProductCount(String userId, final Product product, final CartRepository.IResponseAddCartCallBack cartCallBack) {
        double prePrice = priceLiveData.getValue();
        double newPrice = prePrice + product.getPrice();
        priceLiveData.setValue(newPrice);
        cartRepository.incrementProductCount(product.getId(), userId, product.getProductCount(), new CartRepository.IResponseAddCartCallBack() {
            @Override
            public void onResponseSuccessful(ResponseBody response) {
                cartCallBack.onResponseSuccessful(response);
            }

            @Override
            public void onResponseUnsuccessful(ResponseBody responseBody) {
                double prePrice = priceLiveData.getValue();
                double newPrice = prePrice - product.getPrice();
                priceLiveData.setValue(newPrice);
                cartCallBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                double prePrice = priceLiveData.getValue();
                double newPrice = prePrice - product.getPrice();
                priceLiveData.setValue(newPrice);
                cartCallBack.onCallFailed(message);
            }
        });
    }

    public void decrementProductCount(String userId, final Product product, final CartRepository.IResponseAddCartCallBack cartCallBack) {
        double prePrice = priceLiveData.getValue();
        double newPrice = prePrice - product.getPrice();
        priceLiveData.setValue(newPrice);

        cartRepository.decrementProductCount(product.getId(), userId, product.getProductCount(), new CartRepository.IResponseAddCartCallBack() {
            @Override
            public void onResponseSuccessful(ResponseBody response) {
                cartCallBack.onResponseSuccessful(response);
            }

            @Override
            public void onResponseUnsuccessful(ResponseBody responseBody) {
                double prePrice = priceLiveData.getValue();
                double newPrice = prePrice + product.getPrice();
                priceLiveData.setValue(newPrice);

                cartCallBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                double prePrice = priceLiveData.getValue();
                double newPrice = prePrice + product.getPrice();
                priceLiveData.setValue(newPrice);

                cartCallBack.onCallFailed(message);
            }
        });
    }

    public boolean hasProduct(Product p) {
        for (Product product:
             list) {
            if (product.getId().equals(p.getId())) {
                return true;
            }
        }
        return false;
    }

}
