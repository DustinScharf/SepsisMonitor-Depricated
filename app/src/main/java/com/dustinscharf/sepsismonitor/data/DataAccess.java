package com.dustinscharf.sepsismonitor.data;

import androidx.annotation.NonNull;

import com.dustinscharf.sepsismonitor.util.ICallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * This class can be used to access a Firebase Realtime Database
 */
public class DataAccess implements IDataAccess {
    private static DataAccess dataAccessSingleton;

    /**
     * Gives an instance of this DataAccess object
     * The object is a singleton
     *
     * @return singleton instance of DataAccess
     */
    public static DataAccess getInstance() {
        if (dataAccessSingleton == null) {
            dataAccessSingleton = new DataAccess();
        }
        return dataAccessSingleton;
    }

    // This links to the SepsisMonitor Hospital Firebase Database, it can be changed to access other Firebase Databases
    private static final String URL = "https://sepsismonitor-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String REFERENCE = "hospital";

    private final DatabaseReference databaseReference;

    private DataAccess() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(URL);
        this.databaseReference = firebaseDatabase.getReference(REFERENCE);
    }

    /**
     * Fetches ("one time load") the whole database
     *
     * @param callback a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetch(ICallback<Map<String, Object>> callback) {
        this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Event fired whenever data in the database changes
             *
             * @param snapshot the (new) data in the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            /**
             * Event fired whenever the database connection cancels
             *
             * @param error the error sent by the database
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    /**
     * Fetches ("one time load") all objects of a specific type from the database
     *
     * @param containerKey the type of the objects
     * @param callback     a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchContainer(String containerKey, ICallback<Map<String, Object>> callback) {
        this.databaseReference.child(containerKey).addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Event fired whenever data in the database changes
             *
             * @param snapshot the (new) data in the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            /**
             * Event fired whenever the database connection cancels
             *
             * @param error the error sent by the database
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    /**
     * Fetches ("one time load") a single objects of a specific type from the database
     *
     * @param containerKey the type of the object
     * @param itemKey      the unique key of the object
     * @param callback     a callback containing the received data that is fired when the data is received
     */
    @Override
    public void fetchContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback) {
        this.databaseReference.child(containerKey).child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Event fired whenever data in the database changes
             *
             * @param snapshot the (new) data in the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            /**
             * Event fired whenever the database connection cancels
             *
             * @param error the error sent by the database
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    /**
     * Subscribes ("load every change, whenever it will happen") the whole database
     *
     * @param callback a callback containing the received data that is fired whenever data in the database changes and the data is fully received
     */
    @Override
    public void subscribe(ICallback<Map<String, Object>> callback) {
        this.databaseReference.addValueEventListener(new ValueEventListener() {
            /**
             * Event fired whenever data in the database changes
             *
             * @param snapshot the (new) data in the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            /**
             * Event fired whenever the database connection cancels
             *
             * @param error the error sent by the database
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    /**
     * Subscribes ("load every change, whenever it will happen") all objects of a specific type from the database
     *
     * @param containerKey the type of the objects
     * @param callback     a callback containing the received data that is fired whenever data in the database changes and the data is fully received
     */
    @Override
    public void subscribeContainer(String containerKey, ICallback<Map<String, Object>> callback) {
        this.databaseReference.child(containerKey).addValueEventListener(new ValueEventListener() {
            /**
             * Event fired whenever data in the database changes
             *
             * @param snapshot the (new) data in the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            /**
             * Event fired whenever the database connection cancels
             *
             * @param error the error sent by the database
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    /**
     * Subscribes ("load every change, whenever it will happen") a single objects of a specific type from the database
     *
     * @param containerKey the type of the object
     * @param itemKey      the unique key of the object
     * @param callback     a callback containing the received data that is fired whenever data in the database changes and the data is fully received
     */
    @Override
    public void subscribeContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback) {
        this.databaseReference.child(containerKey).child(itemKey).addValueEventListener(new ValueEventListener() {
            /**
             * Event fired whenever data in the database changes
             *
             * @param snapshot the (new) data in the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            /**
             * Event fired whenever the database connection cancels
             *
             * @param error the error sent by the database
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    /**
     * Adds an item to a specific item list
     * (if item list does not exist, it will be created)
     *
     * @param containerKey the type of the object to add
     * @param itemKey      the key of the object to add
     * @param addItem      the item to add
     */
    @Override
    public void addItemToContainer(String containerKey, String itemKey, Map<String, Object> addItem) {
        this.databaseReference.child(containerKey).child(itemKey).setValue(addItem); // TODO CHECK FOR SUCCESS
    }

    /**
     * Edits an item in a specific list
     *
     * @param containerKey the type of the object to edit
     * @param itemKey      the key of the object to edit
     * @param editItem     a map containing all the object changed like ("name", "Siegfried")
     */
    @Override
    public void editItemInContainer(String containerKey, String itemKey, Map<String, Object> editItem) {
        this.databaseReference.child(containerKey).child(itemKey).updateChildren(editItem); // TODO CHECK FOR SUCCESS
    }

    /**
     * Removes an item from an item list
     *
     * @param containerKey the type of the object to remove
     * @param itemKey      the key of the object to remove
     */
    @Override
    public void removeItemFromContainer(String containerKey, String itemKey) {
        this.databaseReference.child(containerKey).child(itemKey).removeValue(); // TODO CHECK FOR SUCCESS
    }
}
