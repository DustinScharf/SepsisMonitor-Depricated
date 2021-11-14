package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Hospital implements IHospital {

    private static Hospital hospitalSingleton;

    /**
     * Gives an instance of this Hospital object
     * The object is a singleton
     *
     * @return singleton instance of Hospital
     */
    public static Hospital getInstance() {
        if (hospitalSingleton == null) {
            hospitalSingleton = new Hospital();
        }
        return hospitalSingleton;
    }

    private final IDataAccess dataAccess;

    private String loggedInStaffId;
    private boolean loggedInStaffIsLMMP;

    private Hospital() {
        this.dataAccess = IDataAccess.getInstance();
    }

    /**
     * Gives the id of the current logged in staff
     *
     * @return id of the current logged in staff or null if no staff is logged in
     */
    @Override
    public String getLoggedInStaffId() {
        return this.loggedInStaffId;
    }

    /**
     * Sets the id of the current logged in staff
     *
     * @param id id of the current logged in staff
     */
    @Override
    public void setLoggedInStaffId(String id) {
        this.loggedInStaffId = id;
    }

    /**
     * Checks if the current logged in staff is an LMMP
     * If no one is logged in the behavior is the same as if an MMP is logged in
     *
     * @return true if the logged in staff is an LMMP, false otherwise
     */
    @Override
    public boolean loggedInStaffIsLMMP() {
        return this.loggedInStaffIsLMMP;
    }

    /**
     * Sets if the current logged in staff is an LMMP
     *
     * @param isLMMP true if the logged in staff is an LMMP, false otherwise
     */
    @Override
    public void setLoggedInStaffIsLMMP(boolean isLMMP) {
        this.loggedInStaffIsLMMP = isLMMP;
    }

    /**
     * Fetches ("one time load") the whole hospital
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchHospital(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetch(callback);
    }

    /**
     * Subscribes ("load every change, whenever it will happen") the whole hospital
     *
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    @Override
    public void subscribeHospital(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribe(callback);
    }

    /**
     * Fetches ("one time load") every staff of the hospital
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchStaff(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainer("staff", callback);
    }

    /**
     * Fetches ("one time load") a single staff of the hospital with a given id
     *
     * @param staffId  the unique staff id
     * @param callback a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchSingleStaff(String staffId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainerItem("staff", staffId, callback);
    }

    /**
     * Subscribes ("load every change, whenever it will happen") every staff of the hospital
     *
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    @Override
    public void subscribeStaff(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainer("staff", callback);
    }

    /**
     * Subscribes ("load every change, whenever it will happen") a single staff of the hospital with a given id
     *
     * @param staffId  the unique staff id
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    public void subscribeSingleStaff(String staffId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainerItem("staff", staffId, callback);
    }

    /**
     * Fetches ("one time load") every patient of the hospital
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchPatients(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainer("patients", callback);
    }

    /**
     * Fetches ("one time load") a single patient of the hospital with a given id
     *
     * @param patientId the unique patient id
     * @param callback  a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchSinglePatient(String patientId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainerItem("patients", patientId, callback);
    }

    /**
     * Subscribes ("load every change, whenever it will happen") every patient of the hospital
     *
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    @Override
    public void subscribePatients(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainer("patients", callback);
    }

    /**
     * Subscribes ("load every change, whenever it will happen") a single staff of the hospital with a given id
     *
     * @param patientId the unique patient id
     * @param callback  a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    @Override
    public void subscribeSinglePatient(String patientId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainerItem("patients", patientId, callback);
    }

    /**
     * Checks if given login credentials are valid
     *
     * @param singleStaffMap the data of the staff to check the login for
     * @param id             the id of the staff to check
     * @param password       the password to check for the staff
     * @return true if the credentials are valid, false otherwise
     */
    @Override
    public boolean tryLogin(Map<String, Object> singleStaffMap, String id, String password) {
        if (singleStaffMap == null) {
            return false;
        }

        if (singleStaffMap.get("password").toString().equals(password)) { // TODO NULL CHECK
            return true;
        } else {
            return false;
        }
    }

    /**
     * Takes the data of all patients and puts them into a list for better iterating
     *
     * @param patientsMap the data of all patients
     * @return patient info data in a list (one list element for one patient)
     */
    @Override
    public ArrayList<HashMap<String, Object>> getPatients(Map<String, Object> patientsMap) {
        ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

        for (String patientKey : patientsMap.keySet()) {
            HashMap<String, Object> patient = new HashMap<>();
            patient.put(patientKey, patientsMap.get(patientKey));
            patients.add(patient);
        }

        return this.sortPatientsListByPhase(patients);
    }

    /**
     * Takes the data of all patients and puts all the patients assigned to the given staff into a list for better iterating
     *
     * @param fullHospitalMap the data if the whole hospital
     * @param staffId         the id of the staff to get the assigned patients of
     * @param getArchived     set true to get the archived patients of this staff aswell
     * @return patient (belonging to the given staff id) info data in a list (one list element for one patient)
     */
    @Override
    public ArrayList<HashMap<String, Object>> getPatientsByStaff(Map<String, Object> fullHospitalMap, String staffId, boolean getArchived) {
        ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

        Map<String, Object> allPatients = (Map<String, Object>) fullHospitalMap.get("patients");

        Map<String, Object> allStaffs = (Map<String, Object>) fullHospitalMap.get("staff");
        Map<String, Object> singleStaff = (Map<String, Object>) allStaffs.get(staffId);
        Map<String, Object> singleStaffsPatients = (Map<String, Object>) singleStaff.get("patients");

        for (String patientKey : singleStaffsPatients.keySet()) {
            Map<String, Object> currentPatient = (Map<String, Object>) allPatients.get(patientKey);
            if (((long) currentPatient.get("phase")) <= 5) {
                HashMap<String, Object> patient = new HashMap<>();
                patient.put(patientKey, currentPatient);
                patients.add(patient);
            }
        }

        return this.sortPatientsListByPhase(patients);
    }

    private ArrayList<HashMap<String, Object>> sortPatientsListByPhase(ArrayList<HashMap<String, Object>> patientsList) {
        // TODO IMPLEMENT ANOTHER SORT ALGORITHM
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < patientsList.size() - 1; ++i) {
                if (this.extractPhaseId(patientsList.get(i)) > this.extractPhaseId(patientsList.get(i + 1))) {
                    Collections.swap(patientsList, i, i + 1);
                    sorted = false;
                }
            }
        }
        return patientsList;
    }

    private int extractPhaseId(HashMap<String, Object> patientWithIdMap) {
        String patientKey = new ArrayList<>(patientWithIdMap.keySet()).get(0);
        HashMap<String, Object> patient = (HashMap<String, Object>) patientWithIdMap.get(patientKey);

        return Integer.parseInt(patient.get("phase").toString());
    }

    /**
     * Adds a patient to the hospital
     *
     * @param id        the id of the patient to add
     * @param firstName the first name of the patient to add
     * @param lastName  the last name of the patient to add
     */
    @Override
    public void addPatient(String id, String firstName, String lastName) {
        HashMap<String, Object> addItem = new HashMap<>();
        addItem.put("firstName", firstName);
        addItem.put("lastName", lastName);
        addItem.put("phase", 0);
        this.dataAccess.addItemToContainer("patients", id, addItem);
    }

    /**
     * Assigns a patient to a staff
     *
     * @param patientId the patent id that is going to be assigned to a staff
     * @param staffId   the staff id to assign the patient to
     */
    @Override
    public void assignPatientToStaff(String patientId, String staffId) {
        this.fetchSingleStaff(staffId, stringObjectMap -> {
            ((Map<String, Object>) stringObjectMap.get("patients")).put(patientId, true);
            dataAccess.editItemInContainer("staff", staffId, stringObjectMap);
        });
    }

    /**
     * Removes a patient from a staff
     *
     * @param patientId the patent id that is going to be removed from a staff
     * @param staffId   the staff id to remove the patient from
     */
    public void removePatientFromStaff(String patientId, String staffId) {
        this.fetchSingleStaff(staffId, stringObjectMap -> {
            ((Map<String, Object>) stringObjectMap.get("patients")).remove(patientId);
            dataAccess.editItemInContainer("staff", staffId, stringObjectMap);
        });
    }

    /**
     * Puts a patient into a specific phase
     * (For phase ids refer to {@link IHospital#getPhaseById(int)})
     *
     * @param patientId the id of the patient to put into a specific phase
     * @param phase     the phase the patient gets assigned to ( the index of the phase [0 = registration, ...] )
     */
    @Override
    public void putPatientIntoPhase(String patientId, int phase) {
        HashMap<String, Object> editItem = new HashMap<>();
        editItem.put("phase", phase);
        this.dataAccess.editItemInContainer("patients", patientId, editItem);
    }

    /**
     * Gives the staff id by a patient this staff is assigned to
     *
     * @param staffMap  the data of all staffs
     * @param patientId the patient id to get the staff id from
     * @return the assigned staff id of the given patient
     */
    @Override
    public String findStaffIdByAssignedPatientId(Map<String, Object> staffMap, String patientId) {
        for (Map.Entry<String, Object> staffInfo : staffMap.entrySet()) {

            Map<String, Object> staff = (Map<String, Object>) staffInfo.getValue();

            if (staff.get("patients") != null && ((Map<String, Object>) staff.get("patients")).containsKey(patientId)) {
                return staffInfo.getKey();
            }
        }
        return "NOT ASSIGNED";
    }

    /**
     * Converts the phase id to a string
     * <p>
     * 0: Registration
     * 1: ER Triage
     * 2: ER Sepsis Triage
     * 3: IV Antibiotics
     * 4: Admission
     * 5: Release
     *
     * @param id the phase id
     * @return phase as word
     */
    @Override
    public String getPhaseById(int id) {
        switch (id) {
            case 0:
                return "Registration";
            case 1:
                return "ER Triage";
            case 2:
                return "ER Sepsis Triage";
            case 3:
                return "IV Antibiotics";
            case 4:
                return "Admission";
            case 5:
                return "Release";
            default:
                return "Archive";
        }
    }
}
