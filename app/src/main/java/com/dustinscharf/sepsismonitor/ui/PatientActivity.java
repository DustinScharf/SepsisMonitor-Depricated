package com.dustinscharf.sepsismonitor.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public class PatientActivity extends AppCompatActivity {

    /**
     * Builds the initial gui for this activity
     *
     * @param savedInstanceState the data from events happened before the start of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent = getIntent();
        String selectedPatientId = intent.getStringExtra(StartActivity.EXTRA_ID);

//        Toolbar toolbar = findViewById(R.id.toolbar);

        IHospital hospital = IHospital.getNewInstance();

        ImageView patientBigPhaseImageView = findViewById(R.id.patientBigPhaseImageView);
        TextView patientBigNameTextView = findViewById(R.id.patientBigNameTextView);
        TextView patientBigPhaseTextView = findViewById(R.id.patientBigPhaseTextView);
        TextView patientBigStaffTextView = findViewById(R.id.patientBigStaffTextView);

        Button backButton = findViewById(R.id.backButton);

        hospital.subscribeSinglePatient(selectedPatientId, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> patientMap) {
                int patientPhaseId = (int) ((long) patientMap.get("phase"));
                int iconId;
                switch (patientPhaseId) {
                    case 0:
                        iconId = R.mipmap.registration_icon;
                        break;
                    case 1:
                        iconId = R.mipmap.er_triage_icon;
                        break;
                    case 2:
                        iconId = R.mipmap.er_sepsis_triage_icon;
                        break;
                    case 3:
                        iconId = R.mipmap.iv_antibiotics_icon;
                        break;
                    case 4:
                        iconId = R.mipmap.admission_icon;
                        break;
                    case 5:
                        iconId = R.mipmap.release_icon;
                        break;
                    default:
                        iconId = R.drawable.ic_archive;
                        break;
                }
                patientBigPhaseImageView.setImageResource(iconId);

                patientBigNameTextView.setText(patientMap.get("firstName").toString() + " " + patientMap.get("lastName").toString());

                patientBigPhaseTextView.setText(hospital.getPhaseById(patientPhaseId));

                hospital.subscribeStaff(stringObjectMap -> {
                    String staffId = hospital.findStaffIdByAssignedPatientId(stringObjectMap, selectedPatientId);
                    String staffName = "";
                    if (!staffId.equals("NOT ASSIGNED")) {
                        Map<String, Object> singleStaffMap = (Map<String, Object>) stringObjectMap.get(staffId);
                        staffName = " (" + singleStaffMap.get("firstName").toString() + " " + singleStaffMap.get("lastName").toString() + ")";

                        staffId = "@" + staffId;
                    }

                    patientBigStaffTextView.setText(staffId.toUpperCase() + staffName);
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}