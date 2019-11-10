package ru.chizhikov.weather.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import ru.chizhikov.weather.MainActivity;
import ru.chizhikov.weather.OnSelectedPositionCity;
import ru.chizhikov.weather.R;

import static android.content.Context.MODE_PRIVATE;

public class RedactorListCitiesFragment  extends Fragment {
    private Pattern checkName = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}+.*$");
    private TextInputEditText tvNewCity;
    private ListView listView;
    private TextView emptyTextView;
    private TextView nameTextView;
    private ArrayList nameCity;
    private SharedPreferences listCities;
    private ArrayAdapter<String> adapter;
    private OnSelectedPositionCity listener;
    private boolean addCity = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRedactorListCitiesFragment = inflater
                .inflate(R.layout.fragment_list_cities, container, false);
        initViews(viewRedactorListCitiesFragment);
        return viewRedactorListCitiesFragment;
    }

    public void setAddCity(boolean addCity) {
        this.addCity = addCity;
    }

    private void initViews(View view) {
        loadText();
        listView = view.findViewById(R.id.cities_list_view);
        nameTextView = view.findViewById(R.id.cities_list_name_view);
        nameTextView.setVisibility(View.VISIBLE);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
        createListView();
    }

    private void createListView() {
        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_activated_1, nameCity);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setItemChecked(position, true);
                createPopAppMenu(view, position);
            }
        });
    }

    private void createPopAppMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.pop_app_menu_for_edit, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                popAppMenuRun(menuItem, position);
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (addCity){
            addCity();
        }
    }

    private void popAppMenuRun(MenuItem menuItem, final int position){
        if(menuItem.getItemId() == R.id.menu_add) {
            addCity();
        } else if(menuItem.getItemId() == R.id.menu_del) {
            removeCity(position);
        } else if(menuItem.getItemId() == R.id.menu_edit) {
            editCity(position);
        } else {
            Toast.makeText(getContext(),
                    getString(R.string.update)+ " ", Toast.LENGTH_SHORT).show();
        }
    }

    private void editCity(final int position) {
        LayoutInflater li = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams")
        final View promptsView = li.inflate(R.layout.dialog_add, null);
        tvNewCity =promptsView.findViewById(R.id.textNewCity);
        tvNewCity.setText(nameCity.get(position).toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(getString(R.string.edit)+ " " + getString(R.string.city));
        builder.setView(promptsView);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(true);
        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (validate(String.valueOf(tvNewCity.getText()))) {
                    adapter.remove(adapter.getItem(position));
                    adapter.insert(String.valueOf(tvNewCity.getText()), position);
                    listener = (OnSelectedPositionCity) getActivity();
                    Objects.requireNonNull(listener).onPositionSelected(position);
                    Toast.makeText(getContext(), getString(R.string.edited) + " "
                            + tvNewCity.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources()
                            .getString(R.string.it_is_wrong_name_city), Toast.LENGTH_SHORT).show();
                }
            } });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void addCity() {
        LayoutInflater li = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams")
        final View promptsView = li.inflate(R.layout.dialog_add, null);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(getString(R.string.add)+ " " + getString(R.string.city));
        builder.setView(promptsView);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(true);
        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                tvNewCity =promptsView.findViewById(R.id.textNewCity);
                if (validate(String.valueOf(tvNewCity.getText()))){
                    adapter.add(String.valueOf(tvNewCity.getText()));
                    Toast.makeText(getContext(), getString(R.string.added) + " "
                            + tvNewCity.getText(), Toast.LENGTH_SHORT).show();
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    listView.setItemChecked(adapter
                            .getPosition(String.valueOf(tvNewCity.getText())), true);
                    if (addCity){
                        MainActivity.setAddCity(true);
                        listener = (OnSelectedPositionCity) getActivity();
                        Objects.requireNonNull(listener).onPositionSelected(adapter
                                .getPosition(String.valueOf(tvNewCity.getText())));
                    }
                } else {
                    Toast.makeText(getContext(), getResources()
                            .getString(R.string.it_is_wrong_name_city), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void removeCity(final int position) {
        Snackbar.make(Objects.requireNonNull(getView()), getString(R.string.remove)+ " "
                + nameCity.get(position) + "?", Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nameCity.size() > 1) {
                            Toast.makeText(getContext(), nameCity.get(position) + " "
                                    + getString(R.string.removed), Toast.LENGTH_SHORT).show();
                            adapter.remove((String) nameCity.get(position));
                            listener = (OnSelectedPositionCity) getActivity();
                            Objects.requireNonNull(listener)
                                    .onPositionSelected(nameCity.size()-1);
                        } else {
                            Toast.makeText(getContext(),
                                    R.string.err_empty_list, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    private void saveListCities() {
        listCities = Objects.requireNonNull(getActivity())
                .getSharedPreferences("listCities", MODE_PRIVATE);
        SharedPreferences.Editor ed = listCities.edit();
        ed.putString("LIST_CITIES", String.valueOf(nameCity));
        ed.apply();
    }

    private void loadText() {
        listCities = Objects.requireNonNull(getActivity())
                .getSharedPreferences("listCities", MODE_PRIVATE);
        String nameCityStr = listCities.getString("LIST_CITIES", "Moscow");
        nameCityStr = nameCityStr.replace("[", "");
        nameCityStr = nameCityStr.replace("]", "");
        String[] nameCityArrStr = nameCityStr.split(", ");
        nameCity = new ArrayList(Arrays.asList(nameCityArrStr));
    }

    @Override
    public void onStop() {
        super.onStop();
        nameTextView.setVisibility(View.GONE);
        saveListCities();
    }

    @Override
    public void onResume() {
        super.onResume();
        nameTextView.setVisibility(View.VISIBLE);
    }

    private boolean validate(String value)
    {
        return checkName.matcher(value).matches();
    }
}
