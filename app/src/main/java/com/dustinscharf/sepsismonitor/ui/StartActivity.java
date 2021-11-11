package com.dustinscharf.sepsismonitor.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;
import com.dustinscharf.sepsismonitor.ui.adapters.PatientsRecyclerViewAdapter;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {
    private IHospital hospital;

    private RecyclerView patientsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        hospital = IHospital.getNewInstance();

        this.patientsRecyclerView = findViewById(R.id.patientsRecyclerView);
        PatientsRecyclerViewAdapter patientsRecyclerViewAdapter = new PatientsRecyclerViewAdapter();
        hospital.subscribePatients(new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                patientsRecyclerViewAdapter.setPatients(hospital.getPatients(stringObjectMap));
            }
        });
        patientsRecyclerView.setAdapter(patientsRecyclerViewAdapter);
        patientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}