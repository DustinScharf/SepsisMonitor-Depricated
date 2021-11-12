package com.dustinscharf.sepsismonitor.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;
import com.dustinscharf.sepsismonitor.ui.StartActivity;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.ViewHolder> {
    private StartActivity startActivityContext;

    private ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

    private final IHospital hospital;

    public PatientsRecyclerViewAdapter(StartActivity startActivityContext, IHospital hospital) {
        this.startActivityContext = startActivityContext;
        this.hospital = hospital;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> patientContainer = this.patients.get(position);
        String patientKey = new ArrayList<>(patientContainer.keySet()).get(0);
        HashMap<String, Object> patient = (HashMap<String, Object>) patientContainer.get(patientKey);

        if (patient == null) {
            holder.patientNameTextView.setText("Patient could not load");
            holder.patientPhaseTextView.setText("There is a patient id assigned to you of a patient that is not in the database, " +
                    "please contact support immanently");
            holder.patientsStaffTextView.setText("ERROR (Read left)");
            return;
        }

        int patientPhaseId = (int) ((long) patient.get("phase"));
        int iconId = 0;
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
        holder.patientPhaseImageView.setImageResource(iconId);

        holder.patientNameTextView.setText(patient.get("firstName").toString() + " " + patient.get("lastName").toString());

        holder.patientPhaseTextView.setText(hospital.getPhaseById(patientPhaseId));

        // todo should be a fetch, but the db does not have this double side relationship
        hospital.subscribeStaff(new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                holder.patientsStaffTextView.setText(hospital.findStaffIdByAssignedPatientId(stringObjectMap, patientKey).toUpperCase());
            }
        });

        holder.patientListItemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityContext.popUpPatientOverview(patientKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setPatients(ArrayList<HashMap<String, Object>> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout patientListItemParent;

        private ImageView patientPhaseImageView;

        private TextView patientNameTextView;
        private TextView patientPhaseTextView;

        private TextView patientsStaffTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.patientListItemParent = itemView.findViewById(R.id.patientListItemParent);

            this.patientPhaseImageView = itemView.findViewById(R.id.patientPhaseImageView);

            this.patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            this.patientPhaseTextView = itemView.findViewById(R.id.patientPhaseTextView);

            this.patientsStaffTextView = itemView.findViewById(R.id.patientsStaffTextView);
        }
    }
}
