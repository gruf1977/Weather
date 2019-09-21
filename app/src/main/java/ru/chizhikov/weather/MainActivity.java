package ru.chizhikov.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    private String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGretingText();

    }
    private void initGretingText() {
        TextView greetingTextView = findViewById(R.id.greetingTextView);
        String text = new GreetingsBuilder().getGreetings(getApplicationContext());
        greetingTextView.setText(text);
    }

    //подтверждение выхода из приложения
    @Override
    public void onBackPressed(){
        if (backPressedTime +2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            s = getResources().getString(R.string.clickForExit);
            backToast = Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
