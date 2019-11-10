package ru.chizhikov.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.chizhikov.weather.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewAboutFragment = inflater
                .inflate(R.layout.fragment_about, container, false);
        TextView textView = viewAboutFragment.findViewById(R.id.text_about_view);
        textView.setText(R.string.about_app_text);
        return viewAboutFragment;
    }
}
