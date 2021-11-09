package com.dustinscharf.sepsismonitor.data;

import androidx.annotation.NonNull;

import com.dustinscharf.sepsismonitor.util.ICallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DataAccess implements IDataAccess {
    private static final String URL = "https://sepsismonitor-default-rtdb.europe-west1.firebasedatabase.app";

    private final DatabaseReference databaseReference;

    public DataAccess(String reference) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(URL);
        this.databaseReference = firebaseDatabase.getReference(reference);
    }

    @Override
    public void subscribeContainer(String containerKey, ICallback<Map<String, Object>> callback) {
        this.databaseReference.child(containerKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    @Override
    public void subscribeContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback) {
        this.databaseReference.child(containerKey).child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback((Map<String, Object>) snapshot.getValue()); // TODO CHECK CAST
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO ERROR HANDLE
            }
        });
    }

    @Override
    public void addItemToContainer(String containerKey, Map<String, Object> addItem) {
        this.databaseReference.child(containerKey).setValue(addItem); // TODO CHECK FOR SUCCESS
    }

    @Override
    public void editItemInContainer(String containerKey, String itemKey, Map<String, Object> editItem) {
        this.databaseReference.child(containerKey).child(itemKey).updateChildren(editItem); // TODO CHECK FOR SUCCESS
    }

    @Override
    public void removeItemFromContainer(String containerKey, String itemKey) {
        this.databaseReference.child(containerKey).child(itemKey).removeValue(); // TODO CHECK FOR SUCCESS
    }
}
