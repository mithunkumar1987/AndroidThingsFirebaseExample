package example.mithun.com.FirebaseFirestore;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mithun on 6/3/2018.
 */

public class FirebaseFireStoreHelper {
    private static final String TAG = FirebaseFireStoreHelper.class.getCanonicalName();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void initFireBaseFireStore() {
        // Create a new user with a first and last name
        Map<String, Object> led = new HashMap<>();
        led.put("status", "0");

        // Add a new document with a generated ID
        db.collection("led")
                .add(led)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }


}
