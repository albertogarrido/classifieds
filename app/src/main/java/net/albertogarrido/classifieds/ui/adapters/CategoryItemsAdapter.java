package net.albertogarrido.classifieds.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.albertogarrido.classifieds.R;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.presenters.ICategoryItemsPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.CategoryItemViewHolder> {

    private ICategoryItemsPresenter presenter;
    private List<GoogleImage> googleImages;
    private Context context;

    public CategoryItemsAdapter(Context context, ICategoryItemsPresenter presenter, List<GoogleImage> googleImages) {
        this.googleImages = googleImages;
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public CategoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new CategoryItemViewHolder(itemView, new CategoryItemViewHolder.ICategoryHolderClicks() {
            @Override
            public void onCategoryClick(View caller, int adapterPosition) {
                GoogleImage googleImageItem = googleImages.get(adapterPosition);
                presenter.onItemSelected(googleImageItem);
            }
        });
    }

    @Override
    public void onBindViewHolder(CategoryItemViewHolder holder, int position) {
        holder.categoryTitle.setText(googleImages.get(position).getTitle());
        Picasso.with(context).load(googleImages.get(position).getFullSizeLink()).into(holder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return this.googleImages.size();
    }

    public List<GoogleImage> getList() {
        return googleImages;
    }

    public static class CategoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.category_title)
        public TextView categoryTitle;
        @Bind(R.id.category_image)
        public ImageView categoryImage;
        private final ICategoryHolderClicks mListener;

        public interface ICategoryHolderClicks {
            void onCategoryClick(View caller, int adapterPosition);
        }

        public CategoryItemViewHolder(View itemView, ICategoryHolderClicks listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mListener = listener;
            categoryImage.setOnClickListener(this);
            categoryTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onCategoryClick(view, getAdapterPosition());
        }
    }
}