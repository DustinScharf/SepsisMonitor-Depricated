package com.dustinscharf.sepsismonitor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dustinscharf.sepsismonitor.R;

/**
 * This activity contains a start screen of the app
 * with the title and a button to proceed
 * <p>
 * The activity is shown when the app is launched
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Builds the initial gui for this activity
     *
     * @param savedInstanceState the data from events happened before the start of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);

        Button toLoginButton = findViewById(R.id.toLoginButton);
        toLoginButton.setOnClickListener(view -> openLoginActivity());
    }

    /**
     * Opens the login activity
     */
    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}