package contafe.snipsmash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import contafe.snipsmash.R;

/**
 * Created by Natig on 5/21/16.
 */
public class SnipsAdapter {
}

/*
package az.tipster.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import az.tipster.R;
import az.tipster.model.Category;

/**
 * Created by Natig on 11/8/15.
 */
/*
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{
    private List<Category> ctgList; //categoryList

    public CategoriesAdapter(List<Category> categories)
    {
        this.ctgList = categories;
    }

    // Create new views
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_filter_row, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.categoryName.setText(ctgList.get(position).getCategoryName());

        viewHolder.chkSelected.setChecked(ctgList.get(position).isSelected());

        viewHolder.chkSelected.setTag(ctgList.get(position));


        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Category category = (Category) cb.getTag();

                category.setSelected(cb.isChecked());
                ctgList.get(pos).setSelected(cb.isChecked());

//                Toast.makeText(
//                        v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return ctgList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public CheckBox chkSelected;

        public Category singleCategory;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            categoryName = (TextView) itemLayoutView.findViewById(R.id.categoryName);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.checkSelected);
        }

    }
    // method to access in activity after updating selection
    public List<Category> getCategoryList() {
        return ctgList;
    }
}

 */