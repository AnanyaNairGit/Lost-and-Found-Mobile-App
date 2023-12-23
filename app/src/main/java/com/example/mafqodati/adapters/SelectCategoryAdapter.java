package com.example.mafqodati.adapters;

import static com.example.mafqodati.util.Constants.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.example.mafqodati.models.Category;

import java.util.List;

public class SelectCategoryAdapter  extends ArrayAdapter<Category> {
    private LayoutInflater inflater;

    public SelectCategoryAdapter(Context context) {
        super(context, 0, categories);
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            // Inflate the layout for each list item
            view = inflater.inflate(R.layout.item_category_spn, parent, false);
            // Create ViewHolder and store references to the child views
            viewHolder = new ViewHolder();
            viewHolder.txtCategory = view.findViewById(R.id.txtCategory);
            viewHolder.imgCategory = view.findViewById(R.id.imgCategory);

            // Set the tag for later reference
            view.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the ViewHolder from the tag
            viewHolder = (ViewHolder) view.getTag();
        }

        // Set the text of the TextView using the data from the items list
        Category category = categories.get(position);
        viewHolder.txtCategory.setText(category.getNameEn());
        Glide.with(parent.getContext())
                .load(category.getImageUri())
                .into(viewHolder.imgCategory);
        return view;
    }

    private static class ViewHolder {
        TextView txtCategory;
        ImageView imgCategory;
    }
}
