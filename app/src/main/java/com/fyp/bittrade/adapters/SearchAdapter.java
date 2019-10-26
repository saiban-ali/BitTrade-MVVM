package com.fyp.bittrade.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.bittrade.R;
import com.fyp.bittrade.models.Product;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ExploreProductsAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private List<Product> favoritesList;
    private List<Product> cartList;
    private ExploreProductsAdapter.OnItemClickListener onItemClickListener;

    public SearchAdapter(Context context, List<Product> productList, List<Product> favoritesList, List<Product> cartList) {
        this.context = context;
        this.productList = productList;
        this.favoritesList = favoritesList;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ExploreProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExploreProductsAdapter.ProductViewHolder viewHolder = null;

        View productView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_item, parent, false);
        viewHolder = new ExploreProductsAdapter.ProductViewHolder(productView, onItemClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreProductsAdapter.ProductViewHolder holder, int position) {
        holder.titleText.setText(productList.get(position).getTitle());
        holder.brandText.setText(
                (productList.get(position).getBrand() == null || productList.get(position).getBrand().isEmpty()) ?
                        "No Brand" :
                        productList.get(position).getBrand()
        );
        holder.priceText.setText(String.format("$%.2f", productList.get(position).getPrice()));

        String[] imageUrls = productList.get(position).getImages();

        if (imageUrls.length == Product.HAS_NO_IMAGE_URL) {
//            holder.progressBar.setVisibility(View.GONE);
            holder.productImage.setImageResource(R.drawable.ic_logo_light);
//            holder.noImageText.setVisibility(View.VISIBLE);
        } else {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(productList.get(position).getImages()[0])
                    .apply(requestOptions)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.productImage);
        }

        if (favoritesList.contains(productList.get(position))) {
            holder.addToFavorites.setVisibility(View.GONE);
            holder.removeFromFavorites.setVisibility(View.VISIBLE);
        } else {
            holder.addToFavorites.setVisibility(View.VISIBLE);
            holder.removeFromFavorites.setVisibility(View.GONE);
        }

        if (cartList.contains(productList.get(position))) {
            holder.addToCart.setVisibility(View.GONE);
            holder.removeFromCart.setVisibility(View.VISIBLE);
        } else {
            holder.addToCart.setVisibility(View.VISIBLE);
            holder.removeFromCart.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void submitList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ExploreProductsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Product getProduct(int position) {
        if (position > -1)
            return productList.get(position);

        return null;
    }
}
