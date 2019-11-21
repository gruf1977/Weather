package ru.chizhikov.weather;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import ru.chizhikov.weather.fragments.AboutFragment;
import ru.chizhikov.weather.fragments.ConnectCreateFragment;
import ru.chizhikov.weather.fragments.ListOfCities;
import ru.chizhikov.weather.fragments.RedactorListCitiesFragment;
import ru.chizhikov.weather.fragments.TemperAtUserFragment;
import ru.chizhikov.weather.fragments.WeatherValue;

public class MainActivity extends AppCompatActivity implements OnSelectedPositionCity,
        NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private long backPressedTime;
    private Toast backToast;
    public static FloatingActionButton fab;
    static boolean addCity = false;
    ListOfCities listOfCities;
    WeatherValue weatherValue;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Boolean isLandscapeOrientation;
    static int numberPositionTemp;
    int lastSaveIndex;
    AboutFragment aboutFragment;
    ConnectCreateFragment connectCreateFragment;
    TemperAtUserFragment temperAtUserFragment;
    RedactorListCitiesFragment redactorListCitiesFragment;

    public static void setAddCity(boolean addCity) {
        MainActivity.addCity = addCity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLastIndex();
        initToolBar();
        initSideMenu();
        initOrientation();
        initFab();
        initFragments();
        fragmentManager = getSupportFragmentManager();
        initSaveInstanceState(savedInstanceState);
    }

    private void getLastIndex() {
        SharedPreferences lastIndex = getSharedPreferences("lastIndex",MODE_PRIVATE);
        lastSaveIndex = Integer.valueOf(lastIndex.getString("LAST_INDEX", "-1"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences lastIndex = getSharedPreferences("lastIndex",MODE_PRIVATE);
        SharedPreferences.Editor ed = lastIndex.edit();
        ed.putString("LAST_INDEX", String.valueOf(numberPositionTemp));
        ed.apply();
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFragments() {
        listOfCities = new ListOfCities();
        weatherValue = new WeatherValue();
        aboutFragment = new AboutFragment();
        connectCreateFragment = new ConnectCreateFragment();
        temperAtUserFragment = new TemperAtUserFragment();
        redactorListCitiesFragment = new RedactorListCitiesFragment();
    }

    private void initSideMenu() {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initOrientation() {
        isLandscapeOrientation = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initFab() {
        fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_reply_all_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.change_city), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setFragmentToMainContainer(listOfCities);
                            }
                        }).show();
            }
        });
        if (isLandscapeOrientation) {
            fab.hide();
        }
    }

    private void initSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null && lastSaveIndex == -1) {
            numberPositionTemp = 0;
            setFragmentToMainContainer(listOfCities);

        } else if (savedInstanceState == null && lastSaveIndex > -1){
            numberPositionTemp = lastSaveIndex;
            onPositionSelected(numberPositionTemp);

        } else {
            numberPositionTemp = savedInstanceState.getInt("numberPosition", 0);
            onPositionSelected(numberPositionTemp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onNavigationItemSelected(item);
        return true;
    }

    public static int getNumberPositionTemp() {
        return numberPositionTemp;
    }

    public static void setNumberPositionTemp(int numberPositionTemp) {
        MainActivity.numberPositionTemp = numberPositionTemp;
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            String msgForToast = getResources().getString(R.string.clickForExit);
            backToast = Toast.makeText(getBaseContext(), msgForToast, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onPositionSelected(int numberPosition) {
        numberPositionTemp = numberPosition;
        if (isLandscapeOrientation) {
            if (addCity){
                listOfCities = new ListOfCities();
                addCity =false;
            }
            listOfCities.setNumberPosition(numberPositionTemp);
            setFragmentToMainContainer(listOfCities);
            weatherValue = new WeatherValue();
            weatherValue.setNumPosition(numberPositionTemp);
            setFragmentToWeatherValueContainer(weatherValue);
        } else {
            weatherValue.setNumPosition(numberPositionTemp);
            setFragmentToMainContainer(weatherValue);
        }
    }

    private void setFragmentToWeatherValueContainer(Fragment tagFragment) {
        transaction = fragmentManager.beginTransaction();
        fragmentManager.findFragmentByTag(String.valueOf(tagFragment));
        transaction.replace(R.id.weather_value, tagFragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("numberPosition", numberPositionTemp);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            setFragmentToMainContainer(listOfCities);
        } else if (id == R.id.nav_redactor_list_cities || id == R.id.nav_add_city) {
            setFragmentToMainContainer(redactorListCitiesFragment);
            if  (id == R.id.nav_add_city){
                redactorListCitiesFragment.setAddCity(true);
            }
        } else if (id == R.id.nav_about) {
            changeFragments(aboutFragment);
        } else if (id == R.id.nav_connect_create) {
            changeFragments(connectCreateFragment);
        } else if (id == R.id.nav_weather_in_phone) {
            changeFragments(temperAtUserFragment);
        } else {
            Toast.makeText(getBaseContext(), "Not item", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragments(Fragment fragment) {
        if (isLandscapeOrientation){
            setFragmentToWeatherValueContainer (fragment);
        } else {
            setFragmentToMainContainer(fragment);
        }
    }

    public void setFragmentToMainContainer(Fragment tagFragment) {
        fragmentManager.findFragmentByTag(String.valueOf(tagFragment));
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, tagFragment);
        transaction.commit();
    }
}