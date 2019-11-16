package ru.chizhikov.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ItemEveryTime> data;
    private OnItemClickListener itemClickListener;
    public RecyclerViewAdapter(ArrayList<ItemEveryTime>  data){
        if (data != null){
            this.data=data;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        if (itemClickListener != null) {
            vh.setOnClickListener(itemClickListener);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemEveryTime dataPosition = data.get(position);
        holder.imageView.setImageResource(dataPosition.picture);
        holder.textViewTemperature.setText(dataPosition.temperature);
        String[] datePositionString = dataPosition.time.split(" ");
        holder.textViewCalendar.setText(datePositionString[0]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCalendar;
        ImageView imageView;
        TextView textViewTemperature;

        ViewHolder (View view) {
            super(view);
            textViewCalendar = view.findViewById(R.id.textViewCalendar);
            imageView = view.findViewById(R.id.imageView);
            textViewTemperature = view.findViewById(R.id.textViewTemperature);
        }

        void setOnClickListener(final OnItemClickListener listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition == RecyclerView.NO_POSITION) return;
                    listener.onItemClick(v, adapterPosition);
                }
            });
        }
    }

    public interface OnItemClickListener extends ItemViewAdapter.OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}