package ru.chizhikov.weather;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {
    private ArrayList everyTime;
    private ItemViewAdapter.OnItemClickListener itemClickListener = null;

    public ItemViewAdapter(ArrayList everyTime){
        if (everyTime != null){
            this.everyTime=everyTime;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.every_time, parent, false);
        ViewHolder vh = new ItemViewAdapter.ViewHolder(view);
        if (itemClickListener != null) {
            vh.setOnClickListener(itemClickListener);
        }
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemEveryTime everyTimex = (ItemEveryTime) everyTime.get(position);
        holder.imageView.setImageResource(everyTimex.picture);
        holder.textViewTemperature.setText(everyTimex.temperature);
        holder.textViewDescription.setText(everyTimex.description);
        holder.textViewHumidity.setText(everyTimex.humidity + "");
        holder.textViewPressure.setText(everyTimex.pressure +"");
        holder.textViewSpeedWind.setText(everyTimex.speedWind +"");
        holder.textViewTime.setText(everyTimex.time);
    }

    @Override
    public int getItemCount() {
        return everyTime.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescription;
        TextView textViewHumidity;
        TextView textViewPressure;
        TextView textViewSpeedWind;
        TextView textViewTime;
        ImageView imageView;
        TextView textViewTemperature;

        ViewHolder (View view) {
            super(view);
            imageView = view.findViewById(R.id.imageViewBot);
            textViewTemperature = view.findViewById(R.id.textViewTemperatureBot);
            textViewDescription = view.findViewById(R.id.textViewDescriptionBot);
            textViewHumidity = view.findViewById(R.id.textViewHumidityBot);
            textViewPressure = view.findViewById(R.id.textViewPressureBot);
            textViewSpeedWind = view.findViewById(R.id.textViewSpeedWindBot);
            textViewTime = view.findViewById(R.id.textViewTimeBot);
        }

        void setOnClickListener(final ItemViewAdapter.OnItemClickListener listener){
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

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
}
