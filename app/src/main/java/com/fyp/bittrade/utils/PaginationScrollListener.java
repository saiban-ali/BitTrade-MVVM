package com.fyp.bittrade.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = PaginationScrollListener.class.getName();

    GridLayoutManager gridLayoutManager;

    public PaginationScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

//        Log.d(TAG, "onScrolled: " + gridLayoutManager.findLastCompletelyVisibleItemPosition());

        int totalItems = gridLayoutManager.getItemCount();

//        Log.d(TAG, "onScrolled: " + totalItems);

        if(hasNextPage() && !isLastPage()) {
            if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == totalItems - 1) {
                loadMoreItem();
            }
        }

    }

    protected abstract void loadMoreItem();
    public abstract boolean isLastPage();
    public abstract boolean hasNextPage();

}
