package com.fyp.bittrade.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.bittrade.R;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.ProductsDiffUtil;

import java.util.ArrayList;
import java.util.List;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CartViewHolder> {

    private List<Product> cartList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CartProductsAdapter(Context context) {
        this.cartList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder
                (
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.cardview_cart, parent, false),
                        onItemClickListener
                );
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.productTitle.setText(cartList.get(position).getTitle());
        holder.productPrice.setText(String.format("$%.2f", cartList.get(position).getPrice()));
        holder.productBrand.setText("Brand");
        holder.productCount.setText(String.format("%d", cartList.get(position).getProductCount()));

        if(cartList.get(position).getImages().length > 0) {
            Glide.with(context)
                    .load(cartList.get(position).getImages()[0])
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_logo_light);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setCartList(List<Product> list) {

//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ProductsDiffUtil(this.cartList, list), true);
//        this.cartList.clear();
//        this.cartList.addAll(list);
//        diffResult.dispatchUpdatesTo(this);

        cartList = list;
        notifyDataSetChanged();

//        this.cartList = list;
//        notifyDataSetChanged();
    }

    public Product getProduct(int position) {
        if (position > -1)
            return cartList.get(position);

        return null;
    }


    public interface OnItemClickListener {
        void onRemoveClick(int position, View v, View itemView);
        void onAddCountClick(int position, View v);
        void onMinusCountClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productBrand;
        TextView productPrice;
        ImageView removeFromCart;
        private ImageView addProductCount;
        private ImageView minusProductCount;
        private TextView productCount;

        public CartViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.img_product_cart);
            productTitle = itemView.findViewById(R.id.txt_title_cart);
            productBrand = itemView.findViewById(R.id.txt_brand_cart);
            productPrice = itemView.findViewById(R.id.txt_price_cart);
            removeFromCart = itemView.findViewById(R.id.img_remove_product);
            addProductCount = itemView.findViewById(R.id.img_add_product);
            minusProductCount = itemView.findViewById(R.id.img_minus_product);
            productCount = itemView.findViewById(R.id.txt_product_count);

            setUpListeners(itemView, onItemClickListener);
        }

        private void setUpListeners(final View itemView, final OnItemClickListener listener) {
            removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveClick(position, v, itemView);
                        }
                    }
                }
            });

            addProductCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddCountClick(position, v);
//                            productCount.setText(String.format("%s", productList.get(i).getProductCount()));
                        }
                    }
                }
            });

            minusProductCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMinusCountClick(position, v);
                        }
                    }
                }
            });
        }
    }
}
