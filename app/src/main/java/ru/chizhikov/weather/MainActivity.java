package ru.chizhikov.weather;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import ru.chizhikov.weather.fragments.WeatherValue;

public class  MainActivity extends AppCompatActivity implements OnSelectedPositionCity {
    private long backPressedTime;
    private Toast backToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onBackPressed(){
        if (backPressedTime +2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            int countOfFragmentInManager = getSupportFragmentManager().getBackStackEntryCount();
            if(countOfFragmentInManager > 0) {
                getSupportFragmentManager().popBackStack();
            }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        WeatherValue weatherValue = (WeatherValue) fragmentManager
                .findFragmentById(R.id.weather_value);
        weatherValue.setWeatherValue(numberPosition);
    }
}




