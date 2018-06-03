package example.mithun.com.androidthingsfirebaseblink.FirebaseFirestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import example.mithun.com.androidthingsfirebaseblink.MainActivity;


/**
 * Created by mithun on 6/3/2018.
 */

public class FirebaseFirestoreHelper {
    private static final String TAG = FirebaseFirestoreHelper.class.getCanonicalName();
    // Access a Cloud Firestore instance from your Activity

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void getLedOnStatus() {
        db.collection("led")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String ledStatus = (String) document.get("status");
                                Log.d(TAG, "Status = " + ledStatus);
                                MainActivity.updateLEDStatus(ledStatus);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void setRealtimeUpdateListener() {
        final DocumentReference docRef = db.collection("led").document("TNnpy3ekD74vDNHW47SC");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    MainActivity.updateLEDStatus("0");
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData() + " : status = " + snapshot.get("status"));
                    MainActivity.updateLEDStatus((String) snapshot.get("status"));
                } else {
                    Log.d(TAG, "Current data: null");
                    MainActivity.updateLEDStatus("0");
                }
            }
        });
    }


}
