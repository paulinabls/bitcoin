package com.psc.bitcoin.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psc.bitcoin.R;
import com.psc.bitcoin.domain.model.Price;

import java.util.ArrayList;
import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    private List<Price> list = new ArrayList<Price>();
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Price price = list.get(position);
        if (price != null) {
            holder.bindTo(price);
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.clear();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(final List<Price> listToReplace) {
        this.list.clear();
        this.list.addAll(listToReplace);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
        }

        public void bindTo(final Price price) {
            name.setText(String.valueOf(price.getUnixTime()));
            description.setText(String.valueOf(price.getValue()));
        }

        public void clear() {
            itemView.setOnClickListener(null);
        }
    }

}
