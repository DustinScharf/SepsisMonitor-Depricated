package com.dustinscharf.sepsismonitor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;
import com.dustinscharf.sepsismonitor.ui.adapters.PatientsRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

/**
 * This activity contains info about patients and a control panel for operations on patients
 */
public class StartActivity extends AppCompatActivity {
    /**
     * The id of the the clicked patient
     */
    public static final String EXTRA_ID = "com.dustinscharf.sepsismonitor.ui.StartActivity.EXTRA_ID";

    private IHospital hospital;

    private RecyclerView patientsRecyclerView;

    private CardView popUpPatientOverviewCardView;
    private Button popUpAssignPatientToStaffButton;
    private TextView popUpPatientNameTextView;
    private Button popUpPatientToNextPhaseButton;

    private TextView popUpMultipleNextPhasesTextView;
    private Button popUpPatientToAdmissionPhaseButton;
    private Button popUpPatientToERSepsisTriagePhaseButton;

    private FloatingActionButton addPatientFloatingActionButton;

    private CardView addNewPatientCardView;
    private EditText enterNewPatientIdEditText;
    private EditText enterNewPatientFirstNameEditText;
    private EditText enterNewPatientLastNameEditText;
    private Button addPatientButton;

    private CardView assignStaffToPatientCardView;
    private EditText enterStaffIdEditText;
    private Button assignStaffToPatientButton;

    private String lastSelectedPatientId;

    private void initializeFields() {
        this.hospital = IHospital.getNewInstance();

        this.patientsRecyclerView = findViewById(R.id.patientsRecyclerView);

        this.popUpPatientOverviewCardView = findViewById(R.id.popUpPatientOverviewCardView);
        this.popUpAssignPatientToStaffButton = findViewById(R.id.popUpAssignPatientToStaffButton);
        this.popUpPatientNameTextView = findViewById(R.id.popUpPatientNameTextView);
        this.popUpPatientToNextPhaseButton = findViewById(R.id.popUpPatientToNextPhaseButton);

        this.popUpMultipleNextPhasesTextView = findViewById(R.id.popUpMultipleNextPhasesTextView);
        this.popUpPatientToAdmissionPhaseButton = findViewById(R.id.popUpPatientToAdmissionPhaseButton);
        this.popUpPatientToERSepsisTriagePhaseButton = findViewById(R.id.popUpPatientToERSepsisTriagePhaseButton);

        this.addPatientFloatingActionButton = findViewById(R.id.addPatientFloatingActionButton);

        this.addNewPatientCardView = findViewById(R.id.addNewPatientCardView);
        this.enterNewPatientIdEditText = findViewById(R.id.enterNewPatientIdEditText);
        this.enterNewPatientFirstNameEditText = findViewById(R.id.enterNewPatientFirstNameEditText);
        this.enterNewPatientLastNameEditText = findViewById(R.id.enterNewPatientLastNameEditText);
        this.addPatientButton = findViewById(R.id.addPatientButton);

        this.assignStaffToPatientCardView = findViewById(R.id.assignStaffToPatientCardView);
        this.enterStaffIdEditText = findViewById(R.id.enterStaffIdEditText);
        this.assignStaffToPatientButton = findViewById(R.id.assignStaffToPatientButton);

        this.lastSelectedPatientId = null;
    }

    /**
     * Builds the initial gui for this activity
     *
     * @param savedInstanceState the data from events happened before the start of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.initializeFields();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(hospital.loggedInStaffIsLMMP() ? "All Patients" : "Your Patients");

        PatientsRecyclerViewAdapter patientsRecyclerViewAdapter = new PatientsRecyclerViewAdapter(this, hospital);
        this.hospital.subscribeHospital(hospitalMap -> {
            Map<String, Object> patientsMap = (Map<String, Object>) hospitalMap.get("patients");
            if (hospital.loggedInStaffIsLMMP()) {
                patientsRecyclerViewAdapter.setPatients(hospital.getPatients(patientsMap));
            } else {
                patientsRecyclerViewAdapter.setPatients(hospital.getPatientsByStaff(hospitalMap, hospital.getLoggedInStaffId(), false));
            }
        });
        patientsRecyclerView.setAdapter(patientsRecyclerViewAdapter);
        patientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.popUpPatientOverviewCardView.setOnClickListener(v -> popUpPatientOverviewCardView.setVisibility(View.GONE));

        if (hospital.loggedInStaffIsLMMP()) {
            this.popUpAssignPatientToStaffButton.setText(R.string.assign);
        } else {
            this.popUpAssignPatientToStaffButton.setText(R.string.remove);
        }
        this.popUpAssignPatientToStaffButton.setOnClickListener(v -> {
            if (hospital.loggedInStaffIsLMMP()) {
                assignStaffToPatientCardView.setVisibility(View.VISIBLE);
            } else {
                hospital.removePatientFromStaff(lastSelectedPatientId, hospital.getLoggedInStaffId());
            }
        });

        this.popUpPatientToNextPhaseButton.setOnClickListener(v -> hospital.fetchSinglePatient(lastSelectedPatientId, patientMap -> {
            int currentPhaseId = Integer.parseInt(patientMap.get("phase").toString());
            if (currentPhaseId == 1) {
                popUpMultipleNextPhasesTextView.setVisibility(View.VISIBLE);
                popUpPatientToAdmissionPhaseButton.setVisibility(View.VISIBLE);
                popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.VISIBLE);
            } else {
                hospital.putPatientIntoPhase(lastSelectedPatientId, currentPhaseId + 1);
            }
        }));

        this.popUpPatientToAdmissionPhaseButton.setOnClickListener(v -> {
            hospital.putPatientIntoPhase(lastSelectedPatientId, 4);
            popUpMultipleNextPhasesTextView.setVisibility(View.GONE);
            popUpPatientToAdmissionPhaseButton.setVisibility(View.GONE);
            popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.GONE);
        });

        this.popUpPatientToERSepsisTriagePhaseButton.setOnClickListener(v -> {
            hospital.putPatientIntoPhase(lastSelectedPatientId, 2);
            popUpMultipleNextPhasesTextView.setVisibility(View.GONE);
            popUpPatientToAdmissionPhaseButton.setVisibility(View.GONE);
            popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.GONE);
        });


        this.addPatientFloatingActionButton.setOnClickListener(v -> addNewPatientCardView.setVisibility(View.VISIBLE));

        this.addNewPatientCardView.setOnClickListener(v -> addNewPatientCardView.setVisibility(View.GONE));
        this.addPatientButton.setOnClickListener(v -> {
            // TODO check if patient id exists already
            String newPatientIdInput = enterNewPatientIdEditText.getText().toString().toLowerCase();
            String newPatientFirstNameInput = enterNewPatientFirstNameEditText.getText().toString();
            String newPatientLastNameInput = enterNewPatientLastNameEditText.getText().toString();
            if (Math.min(Math.min(newPatientIdInput.length(), newPatientFirstNameInput.length()), newPatientLastNameInput.length()) > 0) {
                hospital.addPatient(newPatientIdInput, newPatientFirstNameInput, newPatientLastNameInput);
                if (!hospital.loggedInStaffIsLMMP()) {
                    hospital.assignPatientToStaff(newPatientIdInput, hospital.getLoggedInStaffId());
                }
                addNewPatientCardView.setVisibility(View.GONE);
            } else {
                Toast.makeText(
                        v.getContext(),
                        "Please enter every field",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        this.assignStaffToPatientCardView.setOnClickListener(v -> assignStaffToPatientCardView.setVisibility(View.GONE));
        this.assignStaffToPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputStaffId = enterStaffIdEditText.getText().toString().toLowerCase();
                hospital.fetchStaff(stringObjectMap -> {
                    if (stringObjectMap.containsKey(inputStaffId) && ((Map<String, Object>) stringObjectMap.get(inputStaffId)).containsKey("patients")) {
                        hospital.assignPatientToStaff(lastSelectedPatientId, inputStaffId);
                        assignStaffToPatientCardView.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(
                                view.getContext(),
                                "The staff does not exists or can not get patients assigned",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        });
    }

    /**
     * Show a small pop up to perform operations on the given patient
     *
     * @param patientId the id of the patient to perform operations on
     */
    public void popUpPatientOverview(String patientId) {
        if (!patientId.equals(this.lastSelectedPatientId)) {
            popUpMultipleNextPhasesTextView.setVisibility(View.GONE);
            popUpPatientToAdmissionPhaseButton.setVisibility(View.GONE);
            popUpPatientToERSepsisTriagePhaseButton.setVisibility(View.GONE);
        }
        this.lastSelectedPatientId = patientId;

        this.popUpPatientOverviewCardView.setVisibility(View.VISIBLE);
        hospital.subscribeSinglePatient(patientId, patient -> popUpPatientNameTextView.setText(patient.get("firstName").toString() + " " + patient.get("lastName").toString()));
    }

    /**
     * Opens an extra window for detailed patient information
     *
     * @param patientId the patients id to display the information of
     */
    public void openPatientActivity(String patientId) {
        Intent intent = new Intent(this, PatientActivity.class);
        intent.putExtra(EXTRA_ID, patientId);
        startActivity(intent);
    }
}