package com.dustinscharf.sepsismonitor.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.logic.IHospital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

    private IHospital hospital = IHospital.getNewInstance();

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

//        holder.patientPhaseImageView.setImageIcon(null); // TODO
        holder.patientNameTextView.setText(patient.get("firstName").toString() + " " + patient.get("lastName").toString());
//        int finalPosition = position;
//        holder.patientToNextPhaseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hospital.putPatientIntoPhase(patients.get(finalPosition).getKey(), (Integer) patients.get(finalPosition).getValue().get("phase") + 1);
//            }
//        });
//
//        holder.parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO
//            }
//        });
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
        private ConstraintLayout parent;

        private ImageView patientPhaseImageView;
        private TextView patientNameTextView;
        private Button patientToNextPhaseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.parent = itemView.findViewById(R.id.parent);

            this.patientPhaseImageView = itemView.findViewById(R.id.patientPhaseImageView);
            this.patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            this.patientToNextPhaseButton = itemView.findViewById(R.id.patientToNextPhaseButton);
        }
    }
}
