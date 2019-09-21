package ru.chizhikov.weather;

import android.content.Context;

import java.util.Date;

class GreetingsBuilder {

    String getGreetings(Context context){
        String result;
        int CurrentHour = new Date(System.currentTimeMillis()).getHours();

        if (CurrentHour >=4 && CurrentHour < 12){
            result = context.getString(R.string.GoodMorning);
        } else if (CurrentHour < 18 && CurrentHour >=12){
            result = context.getString(R.string.GoodDay);
        } else {
            result = context.getString(R.string.GoodEvening);
        }
        return result;
    }

}
