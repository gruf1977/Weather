package ru.chizhikov.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private DataClass[] data = new DataClass[0];
    public RecyclerViewAdapter(DataClass[] data){
        if (data != null){
            this.data=data;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(data[position].picture);
        holder.textViewTemperature.setText(data[position].temperature);
        holder.textViewCalendar.setText(data[position].calendarData);
    }
    @Override
    public int getItemCount() {
        return data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCalendar;
        ImageView imageView;
        TextView textViewTemperature;
        ViewHolder (View view){
            super(view);
            textViewCalendar = view.findViewById(R.id.textViewCalendar);
            imageView = view.findViewById(R.id.imageView);
            textViewTemperature = view.findViewById(R.id.textViewTemperature);
        }
    }
}
