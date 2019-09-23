package com.fyp.bittrade.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fyp.bittrade.R;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.ProductsDiffUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FavoriteProductsAdapter extends RecyclerView.Adapter<FavoriteProductsAdapter.FavoritesRecyclerViewHolder> {
    
    private List<Product> favoritesList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public FavoriteProductsAdapter(Context context) {
        this.favoritesList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        
        return new FavoritesRecyclerViewHolder
                (
                        LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.cardview_favorites, viewGroup, false),
                        onItemClickListener
                );
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoritesRecyclerViewHolder favoritesRecyclerViewHolder, int i) {

        String[] imageUrls = favoritesList.get(i).getImages();
        if (imageUrls.length == Product.HAS_NO_IMAGE_URL) {
            favoritesRecyclerViewHolder.progressBar.setVisibility(View.GONE);
            favoritesRecyclerViewHolder.product_image.setImageResource(R.drawable.ic_logo_light);
        } else {
            Glide.with(context)
                    .load(favoritesList.get(i).getImages()[0])
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            favoritesRecyclerViewHolder.progressBar.setVisibility(View.GONE);
                            favoritesRecyclerViewHolder.product_image.setImageResource(R.drawable.ic_broken_image_black_24dp);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            favoritesRecyclerViewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(favoritesRecyclerViewHolder.product_image);
        }
        favoritesRecyclerViewHolder.product_title.setText(favoritesList.get(i).getTitle());
        favoritesRecyclerViewHolder.product_brand.setText(
                (favoritesList.get(i).getBrand() == null || favoritesList.get(i).getBrand().isEmpty()) ?
                        "No brand" :
                        favoritesList.get(i).getBrand()
        );
        favoritesRecyclerViewHolder.product_price.setText(String.format("$%.2f", favoritesList.get(i).getPrice()));
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void setFavoritesList(List<Product> list) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ProductsDiffUtil(this.favoritesList, list), true);
        this.favoritesList.clear();
        this.favoritesList.addAll(list);
        diffResult.dispatchUpdatesTo(this);

//        this.favoritesList = favoritesList;
//        notifyDataSetChanged();
    }

    public Product getProduct(int position) {
        if (position > -1)
            return favoritesList.get(position);

        return null;
    }

    public interface OnItemClickListener {
        void onRemoveIconClick(int position, View view, View itemView);

        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    static class FavoritesRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_title;
        TextView product_brand;
        TextView product_price;
        ImageView removeFromFavorites;
        ProgressBar progressBar;
        
        FavoritesRecyclerViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            
            product_image = itemView.findViewById(R.id.img_product_image_cardview_favorites);
            product_title = itemView.findViewById(R.id.txt_product_name);
            product_brand = itemView.findViewById(R.id.txt_brand_name);
            product_price = itemView.findViewById(R.id.txt_price);
            removeFromFavorites = itemView.findViewById(R.id.img_remove_from_favorites);
            progressBar = itemView.findViewById(R.id.cardview_item_progress_bar);

            setUpClickListeners(listener);
        }

        private void setUpClickListeners(final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, v);
                        }
                    }
                }
            });

            removeFromFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveIconClick(position, v, itemView);
                        }
                    }
                }
            });
        }
    }
}
