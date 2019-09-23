package com.fyp.bittrade.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.repositories.FavoritesRepository;
import com.fyp.bittrade.utils.IResponseCallBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FavoritesViewModel extends ViewModel {
    private static final String TAG = FavoritesViewModel.class.getName();

    private MutableLiveData<List<Product>> mutableLiveData;
    private List<Product> list = new ArrayList<>();

    private FavoritesRepository favoritesRepository;

    public FavoritesViewModel() {
        favoritesRepository = FavoritesRepository.getInstance();
    }

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

    public void add(final Product p, String userId, final IResponseCallBack callBack) {
        list.add(p);
        mutableLiveData.setValue(list);

        favoritesRepository.addToFavorites(p.getId(), userId, new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                callBack.onResponseSuccessful(response);
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                list.remove(p);
                mutableLiveData.setValue(list);
                callBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                list.remove(p);
                mutableLiveData.setValue(list);
                callBack.onCallFailed(message);
            }
        });
    }

    public void remove(final Product p, String userId, final IResponseCallBack callBack) {
        list.remove(p);
        mutableLiveData.setValue(list);

        favoritesRepository.removeFromFavorites(p.getId(), userId, new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                callBack.onResponseSuccessful(response);
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                list.add(p);
                mutableLiveData.setValue(list);
                callBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                list.add(p);
                mutableLiveData.setValue(list);
                callBack.onCallFailed(message);
            }
        });
    }

    public void loadFavoritesList(String userId, final IResponseCallBack responseCallBack) {
        favoritesRepository.getFavorites(userId, new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                ProductsResponse productsResponse = (ProductsResponse) response.body();
                if (productsResponse != null) {
                    if (productsResponse.getProductList() != null) {
                        setList(productsResponse.getProductList());
                    }
                }

                responseCallBack.onResponseSuccessful(response);
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                responseCallBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                responseCallBack.onCallFailed(message);
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
