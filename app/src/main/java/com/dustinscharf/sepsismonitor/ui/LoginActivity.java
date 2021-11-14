package com.dustinscharf.sepsismonitor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;

/**
 * This activity contains a login area
 * This includes a name- and password text field as well as a confirm button
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Builds the initial gui for this activity
     *
     * @param savedInstanceState the data from events happened before the start of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = findViewById(R.id.toolbar);

        IHospital hospital = IHospital.getNewInstance();

        EditText idEditText = findViewById(R.id.idEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            String inputId = idEditText.getText().toString().toLowerCase();
            String inputPassword = passwordEditText.getText().toString();
            hospital.fetchSingleStaff(inputId, stringObjectMap -> {
                boolean loginSuccess = hospital.tryLogin(stringObjectMap, inputId, inputPassword);
                if (loginSuccess) {
                    hospital.setLoggedInStaffId(inputId);
                    hospital.setLoggedInStaffIsLMMP((Boolean) stringObjectMap.get("isLMMP"));
                    openStartActivity();
                } else {
                    Toast.makeText(
                            this,
                            "Wrong credentials, please try again",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
    }

    /**
     * Opens the start activity
     */
    private void openStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}