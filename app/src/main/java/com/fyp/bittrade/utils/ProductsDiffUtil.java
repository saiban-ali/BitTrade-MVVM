package com.fyp.bittrade.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.fyp.bittrade.models.Product;

import java.util.List;

public class ProductsDiffUtil extends DiffUtil.Callback {

    private List<Product> newList;
    private List<Product> oldList;

    public ProductsDiffUtil(List<Product> oldList, List<Product> newList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getTitle().equals(newList.get(newItemPosition).getTitle());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return (
                oldList.get(oldItemPosition).getTitle().equals(newList.get(newItemPosition).getTitle())
                && oldList.get(oldItemPosition).getDescription().equals(newList.get(newItemPosition).getDescription())
                && oldList.get(oldItemPosition).getStock() == newList.get(newItemPosition).getStock()
                && oldList.get(oldItemPosition).getPrice() == newList.get(newItemPosition).getPrice()
                );
    }
}
