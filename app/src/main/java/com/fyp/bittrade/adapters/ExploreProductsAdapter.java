package com.fyp.bittrade.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fyp.bittrade.R;
import com.fyp.bittrade.models.Product;

import java.util.List;

public class ExploreProductsAdapter extends PagedListAdapter<Product, ExploreProductsAdapter.ProductViewHolder> {

    private static final String TAG = ExploreProductsAdapter.class.getName();
    private List<Product> cartList;
    private List<Product> favoritesList;

    private Context context;

    private static DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return (
                    oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPrice() == newItem.getPrice()
                    && oldItem.getStock() == newItem.getStock()
                    );
        }
    };

    private OnItemClickListener onItemClickListener;

    public ExploreProductsAdapter(Context context, List<Product> favoritesList, List<Product> cartList) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.favoritesList = favoritesList;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductViewHolder viewHolder = null;

        View productView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_item, parent, false);
        viewHolder = new ProductViewHolder(productView, onItemClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {

        holder.titleText.setText(getItem(position).getTitle());
        holder.brandText.setText("Brand");
        holder.priceText.setText(String.format("$%.2f", getItem(position).getPrice()));

        String[] imageUrls = getItem(position).getImages();

        if (imageUrls.length == Product.HAS_NO_IMAGE_URL) {
//            holder.progressBar.setVisibility(View.GONE);
            holder.productImage.setImageResource(R.drawable.ic_logo_light);
            holder.noImageText.setVisibility(View.VISIBLE);
        } else {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(getItem(position).getImages()[0])
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .apply(requestOptions)
                    .into(holder.productImage);
        }

//        .listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Log.e(TAG, "onLoadFailed: GlideException", e);
//
//                holder.progressBar.setVisibility(View.GONE);
//                holder.noImageText.setVisibility(View.GONE);
//                holder.productImage.setImageResource(R.drawable.ic_broken_image_black_24dp);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                holder.progressBar.setVisibility(View.GONE);
//                holder.noImageText.setVisibility(View.GONE);
//                return false;
//            }
//        })

        if (favoritesList.contains(getItem(position))) {
            holder.addToFavorites.setVisibility(View.GONE);
            holder.removeFromFavorites.setVisibility(View.VISIBLE);
        } else {
            holder.addToFavorites.setVisibility(View.VISIBLE);
            holder.removeFromFavorites.setVisibility(View.GONE);
        }

//        if (cartList.contains(getItem(position))) {
//            holder.addToCart.setVisibility(View.GONE);
////            holder.removeFromFavorites.setVisibility(View.VISIBLE);
//        } else {
//            holder.addToCart.setVisibility(View.VISIBLE);
////            holder.removeFromFavorites.setVisibility(View.GONE);
//        }
    }

    public Product getProduct(int position) {
        if (position > -1)
            return getItem(position);

        return null;
    }

    public interface OnItemClickListener {
        void onAddToFavoritesClick(int position, View v, View itemView);
        void onRemoveFromFavoritesClick(int position, View v, View itemView);
        void onProductClick(int position, View v, View itemView);
        void onAddToCartClick(int position, View v, View itemView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

         ImageView productImage;
         TextView titleText;
         TextView brandText;
         TextView priceText;
//        private ProgressBar progressBar;
         TextView noImageText;
         ImageView addToFavorites;
         ImageView removeFromFavorites;
         ImageView addToCart;

        ProductViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.img_product_image_cardview);
            titleText = itemView.findViewById(R.id.txt_product_name);
            brandText = itemView.findViewById(R.id.txt_brand_name);
            priceText = itemView.findViewById(R.id.txt_price);
//            progressBar = itemView.findViewById(R.id.cardview_item_progress_bar);
            noImageText = itemView.findViewById(R.id.txt_no_image);
            addToFavorites = itemView.findViewById(R.id.favorites_icon);
            removeFromFavorites = itemView.findViewById(R.id.favorites_icon_fill);
            addToCart = itemView.findViewById(R.id.add_to_cart_icon);

            setUpClickListeners(itemView, listener);
        }

        private void setUpClickListeners(final View itemView, final OnItemClickListener listener) {
            addToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddToFavoritesClick(position, v, itemView);
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
                            listener.onRemoveFromFavoritesClick(position, v, itemView);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onProductClick(position, v, itemView);
                        }
                    }
                }
            });

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddToCartClick(position, v, itemView);
                        }
                    }
                }
            });
        }
    }
}
