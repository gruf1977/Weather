package ru.chizhikov.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import java.util.regex.Pattern;
import ru.chizhikov.weather.R;

public class ConnectCreateFragment extends Fragment {
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText message;
    private Button btnSentEmail;
    private Boolean[] error;
    private Pattern checkName = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}+.*$");
    private Pattern checkEmail = Pattern.compile("^.+@.+\\..+$");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewConnectCreateFragment = inflater
                .inflate(R.layout.fragment_connect_create, container, false);
        TextView textView = viewConnectCreateFragment.findViewById(R.id.text_contacts_creator_view);
        textView.setText(R.string.contact_with_creator);
        name = viewConnectCreateFragment.findViewById(R.id.editTextName);
        email = viewConnectCreateFragment.findViewById(R.id.editTextEmail);
        message = viewConnectCreateFragment.findViewById(R.id.editTextMessage);
        btnSentEmail =  viewConnectCreateFragment.findViewById(R.id.button_send_email);
        error = new  Boolean[]{true, true, true};
        btnSentEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnSentEmail.setFocusableInTouchMode(true);
                btnSentEmail.requestFocus();
                if (!error[0] && !error[1] && !error[2]){
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/email");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                            new String[] { "gpuf@bk.ru" });
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            "From App Weather in the city");
                    String messageBuilder = message.getText() +
                            "\n" +
                            "\n" +
                            email.getText() +
                            "\n" +
                            name.getText();
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, messageBuilder);
                    Objects.requireNonNull(getActivity())
                            .startActivity(Intent.createChooser(emailIntent, getResources()
                                    .getString(R.string.send_email_title)));
                }
                else {
                    Toast.makeText(getContext(), getResources()
                            .getString(R.string.check_email_data), Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkFolders();
        return viewConnectCreateFragment;
    }

    private void checkFolders() {
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                error[0] = validate(tv, checkName, getResources()
                        .getString(R.string.it_is_not_name));
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                error[1] = validate(tv, checkEmail, getResources()
                        .getString(R.string.it_is_not_email));
            }
        });
        message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                error[2] = tv.length() < 5;
            }
        });
    }

    private Boolean validate(TextView tv, Pattern check, String message){
        boolean errorField;
        String value = tv.getText().toString();
        if (check.matcher(value).matches()){
            hideError(tv);
            errorField  = false;
        }
        else{
            showError(tv, message);
            errorField  = true;
        }
        return errorField;
    }

    private void showError(TextView view, String message) {
        view.setError(message);
    }

    private void hideError(TextView view) {
        view.setError(null);
    }
}
