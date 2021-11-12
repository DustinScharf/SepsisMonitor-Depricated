package com.dustinscharf.sepsismonitor.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;
import com.dustinscharf.sepsismonitor.ui.adapters.PatientsRecyclerViewAdapter;
import com.dustinscharf.sepsismonitor.util.ICallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {
    private IHospital hospital;

    private RecyclerView patientsRecyclerView;

    private CardView popUpPatientOverviewCardView;
    private TextView popUpPatientNameTextView;
    private Button popUpPatientToNextPhaseButton;
    private Button popUpAssignPatientToStaffButton;

    private TextView popUpMultipleNextPhasesTextView;
    private Button popUpPatientToAdmissionPhaseButton;
    private Button popUpPatientToERSepsisTriagePhaseButton;

    private CardView assignStaffToPatientCardView;
    private EditText enterStaffIdEditText;

    private FloatingActionButton addPatientFloatingActionButton;
    private CardView addNewPatientCardView;
    private EditText enterNewPatientIdEditText;
    private EditText enterNewPatientFirstNameEditText;
    private EditText enterNewPatientLastNameEditText;

    private String lastSelectedPatientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.hospital = IHospital.getNewInstance();

        this.patientsRecyclerView = findViewById(R.id.patientsRecyclerView);
        PatientsRecyclerViewAdapter patientsRecyclerViewAdapter = new PatientsRecyclerViewAdapter(this, hospital);
        this.hospital.subscribeHospital(new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> hospitalMap) {
                Map<String, Object> patientsMap = (Map<String, Object>) hospitalMap.get("patients");
                if (hospital.loggedInStaffIsLMMP()) {
                    patientsRecyclerViewAdapter.setPatients(hospital.getPatients(patientsMap));
                } else {
                    patientsRecyclerViewAdapter.setPatients(hospital.getPatientsByStaff(hospitalMap, hospital.getLoggedInStaffId()));
                }
            }
        });
        patientsRecyclerView.setAdapter(patientsRecyclerViewAdapter);
        patientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.popUpPatientOverviewCardView = findViewById(R.id.popUpPatientOverviewCardView);
        this.popUpPatientNameTextView = findViewById(R.id.popUpPatientNameTextView);
        this.popUpPatientToNextPhaseButton = findViewById(R.id.popUpPatientToNextPhaseButton);

        this.popUpAssignPatientToStaffButton = findViewById(R.id.popUpAssignPatientToStaffButton);
        this.popUpMultipleNextPhasesTextView = findViewById(R.id.popUpMultipleNextPhasesTextView);
        this.popUpPatientToAdmissionPhaseButton = findViewById(R.id.popUpPatientToAdmissionPhaseButton);
        this.popUpPatientToERSepsisTriagePhaseButton = findViewById(R.id.popUpPatientToERSepsisTriagePhaseButton);

        this.assignStaffToPatientCardView = findViewById(R.id.assignStaffToPatientCardView);
        this.enterStaffIdEditText = findViewById(R.id.enterStaffIdEditText);

        this.addPatientFloatingActionButton = findViewById(R.id.addPatientFloatingActionButton);

        this.addNewPatientCardView = findViewById(R.id.addNewPatientCardView);
        this.enterNewPatientIdEditText = findViewById(R.id.enterNewPatientIdEditText);
        this.enterNewPatientFirstNameEditText = findViewById(R.id.enterNewPatientFirstNameEditText);
        this.enterNewPatientLastNameEditText = findViewById(R.id.enterNewPatientLastNameEditText);

        this.popUpPatientOverviewCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePatientOverview();
            }
        });

        this.popUpPatientToNextPhaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital.fetchSinglePatient(lastSelectedPatientId, new ICallback<Map<String, Object>>() {
                    @Override
                    public void onCallback(Map<String, Object> patientMap) {
                        int currentPhaseId = Integer.parseInt(patientMap.get("phase").toString());
                        if (currentPhaseId == 1) {
                            popUpMultipleNextPhasesTextView.setVisibility(View.VISIBLE);
                            popUpPatientToAdmissionPhaseButton.setVisibility(View.VISIBLE);
                            popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.VISIBLE);
                        } else {
                            hospital.putPatientIntoPhase(lastSelectedPatientId, currentPhaseId + 1);
                        }
                    }
                });
            }
        });

        this.popUpAssignPatientToStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignStaffToPatientCardView.getVisibility() == View.GONE) {
                    assignStaffToPatientCardView.setVisibility(View.VISIBLE);
                } else {
                    String inputStaffId = enterStaffIdEditText.getText().toString();
                    hospital.fetchStaff(new ICallback<Map<String, Object>>() {
                        @Override
                        public void onCallback(Map<String, Object> stringObjectMap) {
                            if (stringObjectMap.containsKey(inputStaffId) && ((Map<String, Object>) stringObjectMap.get(inputStaffId)).containsKey("patients")) {
                                hospital.assignPatientToStaff(lastSelectedPatientId, inputStaffId);
                                assignStaffToPatientCardView.setVisibility(View.GONE);
                            } else {
                                // TODO TOAST
                            }
                        }
                    });
                }
            }
        });

        this.popUpPatientToAdmissionPhaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital.putPatientIntoPhase(lastSelectedPatientId, 4);
                popUpMultipleNextPhasesTextView.setVisibility(View.GONE);
                popUpPatientToAdmissionPhaseButton.setVisibility(View.GONE);
                popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.GONE);
            }
        });

        this.popUpPatientToERSepsisTriagePhaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital.putPatientIntoPhase(lastSelectedPatientId, 2);
                popUpMultipleNextPhasesTextView.setVisibility(View.GONE);
                popUpPatientToAdmissionPhaseButton.setVisibility(View.GONE);
                popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.GONE);
            }
        });

        this.assignStaffToPatientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignStaffToPatientCardView.setVisibility(View.GONE);
            }
        });

        this.addNewPatientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPatientCardView.setVisibility(View.GONE);
            }
        });

        this.addPatientFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNewPatientCardView.getVisibility() == View.GONE) {
                    addNewPatientCardView.setVisibility(View.VISIBLE);
                } else {
                    // TODO check if patient id exists already
                    String newPatientIdInput = enterNewPatientIdEditText.getText().toString();
                    String newPatientFirstNameInput = enterNewPatientFirstNameEditText.getText().toString();
                    String newPatientLastNameInput = enterNewPatientLastNameEditText.getText().toString();
                    if (Math.min(Math.min(newPatientIdInput.length(), newPatientFirstNameInput.length()), newPatientLastNameInput.length()) > 0) {
                        hospital.addPatient(newPatientIdInput, newPatientFirstNameInput, newPatientLastNameInput);
                        addNewPatientCardView.setVisibility(View.GONE);
                    } else {
                        // TODO TOAST
                    }
                }
            }
        });
    }

    public void popUpPatientOverview(String patientId) {
        if (!patientId.equals(this.lastSelectedPatientId)) {
            popUpMultipleNextPhasesTextView.setVisibility(View.GONE);
            popUpPatientToAdmissionPhaseButton.setVisibility(View.GONE);
            popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.GONE);
        }
        this.lastSelectedPatientId = patientId;

        this.popUpPatientOverviewCardView.setVisibility(View.VISIBLE);
        hospital.subscribeSinglePatient(patientId, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> patient) {
                popUpPatientNameTextView.setText(patient.get("firstName").toString() + " " + patient.get("lastName").toString());
            }
        });
    }

    public void hidePatientOverview() {
        this.popUpPatientOverviewCardView.setVisibility(View.GONE);
    }
}