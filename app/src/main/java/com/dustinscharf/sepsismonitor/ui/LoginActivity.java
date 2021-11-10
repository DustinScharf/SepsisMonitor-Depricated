package com.dustinscharf.sepsismonitor.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        IHospital hospital = IHospital.getNewInstance();

        EditText idEditText = findViewById(R.id.idEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputId = idEditText.getText().toString();
                String inputPassword = passwordEditText.getText().toString();
                hospital.fetchSingleStaff(inputId, new ICallback<Map<String, Object>>() {
                    @Override
                    public void onCallback(Map<String, Object> stringObjectMap) {
                        boolean loginSuccess = hospital.tryLogin(stringObjectMap, inputId, inputPassword);
                        if (loginSuccess) {
                            openStartActivity();
                        } else {
                            // TODO TOAST
                        }
                    }
                });
            }
        });
    }

    private void openStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}