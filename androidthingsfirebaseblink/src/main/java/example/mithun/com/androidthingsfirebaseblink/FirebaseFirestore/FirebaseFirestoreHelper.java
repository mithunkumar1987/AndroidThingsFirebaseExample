package example.mithun.com.androidthingsfirebaseblink.FirebaseFirestore;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
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

    public static void initFirebaseFirestore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void getLedOnStatus(final Handler ledHandler) {
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
                                MainActivity.LED_STATUS ledStatusVal = MainActivity.LED_STATUS.LED_OFF;
                                switch (ledStatus) {
                                    case "1":
                                        ledStatusVal = MainActivity.LED_STATUS.LED_ON;
                                        break;
                                    case "2":
                                        ledStatusVal = MainActivity.LED_STATUS.LED_BLINK;
                                        break;
                                    case "0":
                                    default:
                                        ledStatusVal = MainActivity.LED_STATUS.LED_OFF;
                                        break;

                                }
                                ledHandler.sendMessage(ledHandler.obtainMessage(ledStatusVal.ordinal()));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
