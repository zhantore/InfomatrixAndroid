package com.example.infomatrix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.infomatrix.R;
import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.FoodBackgroundImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {

    private List<Food> foodArrayList;
    private Context context;
    private OnFoodItemClickListener onFoodItemClickListener;

    public FoodsAdapter(Context context, List<Food> foodArrayList) {
        this.context = context;
        this.foodArrayList = foodArrayList;
    }

    public OnFoodItemClickListener getOnFoodItemClickListener() {
        return onFoodItemClickListener;
    }

    public void setOnFoodItemClickListener(OnFoodItemClickListener onFoodItemClickListener) {
        this.onFoodItemClickListener = onFoodItemClickListener;
    }

    @NonNull
    @Override
    public FoodsAdapter.FoodViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodsAdapter.FoodViewHolder foodViewHolder, int position) {
        foodViewHolder.bindData(foodArrayList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.food_item;
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemBackgroundImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView dateTextView;
        private ImageView qrCodeButton;


        private FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBackgroundImageView = itemView.findViewById(R.id.item_background_image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            qrCodeButton = itemView.findViewById(R.id.qr_code_button);
        }

        private void bindData(final Food food)  {

            DateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            FoodBackgroundImage foodBackgroundImage = food.getFoodBackgroundImage();
            if (foodBackgroundImage != null) {
                Glide.with(context).load(foodBackgroundImage.getImage()).into(itemBackgroundImageView);
            }
            titleTextView.setText(food.getTitle());
            descriptionTextView.setText(food.getDescription());
            dateTextView.setText(simpleDateFormat.format(food.getDate()));
            qrCodeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onFoodItemClickListener != null) {
                        onFoodItemClickListener.onFoodItemClickListener(food);
                    }
                }

            });

        }

    }

    public interface OnFoodItemClickListener {

        void onFoodItemClickListener(Food food);

    }

}
