package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class design to manage a Hospital
 * <p>
 * This includes a staff login system and a patient listing system
 * The main function is to add a patient at an initial state
 * and move him forward through the hospital phases
 * (like registration or admission)
 */
public interface IHospital {

    //////////////////////
    // INSTANCE CREATOR //

    /**
     * Gives an instance of the Hospital implementation
     *
     * @return instance of the Hospital implementation
     */
    public static IHospital getNewInstance() {
        return Hospital.getInstance();
    }


    /////////////////////
    // GETTER & SETTER //

    /**
     * Gives the id of the current logged in staff
     *
     * @return id of the current logged in staff or null if no staff is logged in
     */
    public String getLoggedInStaffId();

    /**
     * Sets the id of the current logged in staff
     *
     * @param id id of the current logged in staff
     */
    public void setLoggedInStaffId(String id);

    /**
     * Checks if the current logged in staff is an LMMP
     * If no one is logged in the behavior is the same as if an MMP is logged in
     *
     * @return true if the logged in staff is an LMMP, false otherwise
     */
    public boolean loggedInStaffIsLMMP();

    /**
     * Sets if the current logged in staff is an LMMP
     *
     * @param isLMMP true if the logged in staff is an LMMP, false otherwise
     */
    public void setLoggedInStaffIsLMMP(boolean isLMMP);


    /////////////////////
    // HOSPITAL ACCESS //

    /**
     * Fetches ("one time load") the whole hospital
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    public void fetchHospital(ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") the whole hospital
     *
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    public void subscribeHospital(ICallback<Map<String, Object>> callback);


    //////////////////
    // STAFF ACCESS //

    /**
     * Fetches ("one time load") every staff of the hospital
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    public void fetchStaff(ICallback<Map<String, Object>> callback);

    /**
     * Fetches ("one time load") a single staff of the hospital with a given id
     *
     * @param staffId  the unique staff id
     * @param callback a callback containing the received data that is fired when the data is received
     */
    public void fetchSingleStaff(String staffId, ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") every staff of the hospital
     *
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    public void subscribeStaff(ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") a single staff of the hospital with a given id
     *
     * @param staffId  the unique staff id
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    public void subscribeSingleStaff(String staffId, ICallback<Map<String, Object>> callback);


    ////////////////////
    // PATIENT ACCESS //

    /**
     * Fetches ("one time load") every patient of the hospital
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    public void fetchPatients(ICallback<Map<String, Object>> callback);

    /**
     * Fetches ("one time load") a single patient of the hospital with a given id
     *
     * @param patientId the unique patient id
     * @param callback  a callback containing the received data that is fired when the data is received
     */
    public void fetchSinglePatient(String patientId, ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") every patient of the hospital
     *
     * @param callback a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    public void subscribePatients(ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") a single staff of the hospital with a given id
     *
     * @param patientId the unique patient id
     * @param callback  a callback containing the received data that is fired whenever data changes (e. g. new patient is added) and the data is fully received
     */
    public void subscribeSinglePatient(String patientId, ICallback<Map<String, Object>> callback);


    ///////////
    // LOGIN //

    /**
     * Checks if given login credentials are valid
     *
     * @param singleStaffMap the data of the staff to check the login for
     * @param id             the id of the staff to check
     * @param password       the password to check for the staff
     * @return true if the credentials are valid, false otherwise
     */
    public boolean tryLogin(Map<String, Object> singleStaffMap, String id, String password);


    //////////////////////////
    // PATIENT DATA TO LIST //

    /**
     * Takes the data of all patients and puts them into a list for better iterating
     *
     * @param patientsMap the data of all patients
     * @return patient info data in a list (one list element for one patient)
     */
    public ArrayList<HashMap<String, Object>> getPatients(Map<String, Object> patientsMap);

    /**
     * Takes the data of all patients and puts all the patients assigned to the given staff into a list for better iterating
     *
     * @param fullHospitalMap the data if the whole hospital
     * @param staffId         the id of the staff to get the assigned patients of
     * @param getArchived     set true to get the archived patients of this staff aswell
     * @return patient (belonging to the given staff id) info data in a list (one list element for one patient)
     */
    public ArrayList<HashMap<String, Object>> getPatientsByStaff(Map<String, Object> fullHospitalMap, String staffId, boolean getArchived);


    ////////////////////////
    // PATIENT OPERATIONS //

    /**
     * Adds a patient to the hospital
     *
     * @param id        the id of the patient to add
     * @param firstName the first name of the patient to add
     * @param lastName  the last name of the patient to add
     */
    public void addPatient(String id, String firstName, String lastName);

    /**
     * Assigns a patient to a staff
     *
     * @param patientId the patent id that is going to be assigned to a staff
     * @param staffId   the staff id to assign the patient to
     */
    public void assignPatientToStaff(String patientId, String staffId);

    /**
     * Removes a patient from a staff
     *
     * @param patientId the patent id that is going to be removed from a staff
     * @param staffId   the staff id to remove the patient from
     */
    public void removePatientFromStaff(String patientId, String staffId);

    /**
     * Puts a patient into a specific phase
     * (For phase ids refer to {@link IHospital#getPhaseById(int)})
     *
     * @param patientId the id of the patient to put into a specific phase
     * @param phase     the phase the patient gets assigned to ( the index of the phase [0 = registration, ...] )
     */
    public void putPatientIntoPhase(String patientId, int phase);


    //////////
    // UTIL //

    /**
     * Gives the staff id by a patient this staff is assigned to
     *
     * @param staffMap  the data of all staffs
     * @param patientId the patient id to get the staff id from
     * @return the assigned staff id of the given patient
     */
    public String findStaffIdByAssignedPatientId(Map<String, Object> staffMap, String patientId);


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
    public String getPhaseById(int id);
}
