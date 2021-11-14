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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A scrollable list of patients
 */
public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.ViewHolder> {
    private StartActivity startActivityContext;

    private ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

    private final IHospital hospital;

    /**
     * The constructor to create the scrollable list of patients
     *
     * @param startActivityContext the caller activity (i. e. the start activity)
     * @param hospital             the hospital management object
     */
    public PatientsRecyclerViewAdapter(StartActivity startActivityContext, IHospital hospital) {
        this.startActivityContext = startActivityContext;
        this.hospital = hospital;
    }

    /**
     * Creates a layout for this list of patients
     *
     * @param parent   the parent containing this list
     * @param viewType the type of the parent
     * @return the layout for this list of patients
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Assigns data, functionality and data to the list items
     *
     * @param holder   the parent containing this list
     * @param position the position of the currently creating list element (in patients: ArrayList)
     */
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
        holder.patientPhaseImageView.setImageResource(iconId);

        holder.patientNameTextView.setText(patient.get("firstName").toString() + " " + patient.get("lastName").toString());

        holder.patientPhaseTextView.setText(hospital.getPhaseById(patientPhaseId));

        // todo should be a fetch, but the db does not have this double side relationship
        hospital.subscribeStaff(stringObjectMap -> holder.patientsStaffTextView.setText(hospital.findStaffIdByAssignedPatientId(stringObjectMap, patientKey).toUpperCase()));

        holder.patientListItemParent.setOnClickListener(v -> startActivityContext.popUpPatientOverview(patientKey));
        holder.patientListItemParent.setOnLongClickListener(v -> {
            startActivityContext.openPatientActivity(patientKey);
            return true;
        });
    }

    /**
     * Returns how many items are in this list
     *
     * @return the numeric amount of items in this list
     */
    @Override
    public int getItemCount() {
        return patients.size();
    }

    /**
     * Sets the patient data that will be displayed in this list
     * (And visually reloads the list to display changes in the data set)
     *
     * @param patients the patients that will be displayed in this list
     */
    public void setPatients(ArrayList<HashMap<String, Object>> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    /**
     * The holder (parent) for the list elements
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout patientListItemParent;

        private ImageView patientPhaseImageView;

        private TextView patientNameTextView;
        private TextView patientPhaseTextView;

        private TextView patientsStaffTextView;

        /**
         * Creates the holder (parent) for the list elements and initializes its fields
         *
         * @param itemView the parent containing the list elements
         */
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
