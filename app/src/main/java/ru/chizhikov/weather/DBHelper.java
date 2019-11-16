package ru.chizhikov.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "weather.db", null, 1);
    }
    final static String LOG_TAG = "myLogs";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table weatherTable ("
                + "id integer primary key autoincrement,"
                + "nameCity text,"
                + "time text,"
                + "speedWind float,"
                + "pressure int,"
                + "humidity int,"
                + "description text,"
                + "temperature text,"
                + "picture int,"
                + "cityCountry text,"
                + "timeStamp long" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public  ArrayList readWeatherInCity() {
        ArrayList<ItemEveryTime> arrayCity = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("weatherTable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameCityColIndex = c.getColumnIndex("nameCity");
            int timeColIndex = c.getColumnIndex("time");
            int speedWindColIndex = c.getColumnIndex("speedWind");
            int pressureColIndex = c.getColumnIndex("pressure");
            int humidityColIndex = c.getColumnIndex("humidity");
            int descriptionColIndex = c.getColumnIndex("description");
            int temperatureColIndex = c.getColumnIndex("temperature");
            int pictureColIndex = c.getColumnIndex("picture");
            int cityCountryColIndex = c.getColumnIndex("cityCountry");
            int timeStampColIndex = c.getColumnIndex("timeStamp");
            do {
                arrayCity.add(new ItemEveryTime(c.getString(nameCityColIndex),
                        c.getString(timeColIndex),
                        Float.parseFloat(c.getString(speedWindColIndex)),
                        Integer.parseInt(c.getString(pressureColIndex)),
                        Integer.parseInt(c.getString(humidityColIndex)),
                        c.getString(descriptionColIndex),
                        c.getString(temperatureColIndex),
                        Integer.parseInt(c.getString(pictureColIndex)),
                        c.getString(cityCountryColIndex),
                        Long.parseLong(c.getString(timeStampColIndex)) ));
            } while (c.moveToNext());
        }
        c.close();
        return arrayCity;
    }

    public  ArrayList readWeatherInCityByCity(String city) {
        ArrayList<ItemEveryTime> arrayCity = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("weatherTable",
                null,
                "nameCity = ?",
                new String[] {city},
                null, null, null);
        if (c.moveToFirst()) {
            int nameCityColIndex = c.getColumnIndex("nameCity");
            int timeColIndex = c.getColumnIndex("time");
            int speedWindColIndex = c.getColumnIndex("speedWind");
            int pressureColIndex = c.getColumnIndex("pressure");
            int humidityColIndex = c.getColumnIndex("humidity");
            int descriptionColIndex = c.getColumnIndex("description");
            int temperatureColIndex = c.getColumnIndex("temperature");
            int pictureColIndex = c.getColumnIndex("picture");
            int cityCountryColIndex = c.getColumnIndex("cityCountry");
            int timeStampColIndex = c.getColumnIndex("timeStamp");
            do {
                arrayCity.add(new ItemEveryTime(c.getString(nameCityColIndex),
                        c.getString(timeColIndex),
                        Float.parseFloat(c.getString(speedWindColIndex)),
                        Integer.parseInt(c.getString(pressureColIndex)),
                        Integer.parseInt(c.getString(humidityColIndex)),
                        c.getString(descriptionColIndex),
                        c.getString(temperatureColIndex),
                        Integer.parseInt(c.getString(pictureColIndex)),
                        c.getString(cityCountryColIndex),
                        Long.parseLong(c.getString(timeStampColIndex)) ));
            } while (c.moveToNext());
        }
        c.close();

        return arrayCity;
    }

    public void writeListWeatherForBD(ArrayList<ItemEveryTime> listWeatherForBD) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int countRow = 0;
        for (ItemEveryTime o : listWeatherForBD) {
            cv.clear();
            cv.put("nameCity", o.getCity());
            cv.put("time", o.getTime());
            cv.put("speedWind", o.getSpeedWind());
            cv.put("pressure", o.getPressure());
            cv.put("humidity", o.getHumidity());
            cv.put("description", o.getDescription());
            cv.put("temperature", o.getTemperature());
            cv.put("picture", o.getPicture());
            cv.put("cityCountry", o.getCityCountry());
            cv.put("timeStamp", o.getTimeStamp());
            db.insert("weatherTable", null, cv);
            countRow++;
        }
        Log.d("myLogs", "Добавлено " + countRow + " записей");
    }

    public void deleteWeatherInCityByCity(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        int countRow = db.delete("weatherTable", "nameCity = ?", new String[] {city});
        Log.d("myLogs", "Удалено " + countRow + " записей");
    }
}

