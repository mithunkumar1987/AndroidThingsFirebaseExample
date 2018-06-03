package example.mithun.com.firebaseexampleauthapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import example.mithun.com.FirebaseFirestore.FirebaseFireStoreHelper;

public class LoginResultActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_STATUS_MSG = LoginResultActivity.class.getCanonicalName() + "INTENT_EXTRA_STATUS_MSG";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);
        StringBuilder statusMsg = new StringBuilder();
        statusMsg.append(getIntent().getStringExtra(INTENT_EXTRA_STATUS_MSG));
        mAuth = FirebaseAuth.getInstance();
        TextView statusTextview = findViewById(R.id.status_textview);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            statusMsg.append("\nName:" + user.getDisplayName());
            statusMsg.append("\nEmail:" + user.getEmail() + ", " + (user.isEmailVerified() ? "Verified" : "Not Verified"));
            statusMsg.append("\nPhone:" + user.getPhoneNumber());
            statusMsg.append("\nUid:" + user.getUid());
        }
        statusTextview.setText(statusMsg.toString());
    }

    public void ledOnClicked(View view) {
        FirebaseFireStoreHelper.updateLedStatus("1");
    }

    public void ledOffClicked(View view) {
        FirebaseFireStoreHelper.updateLedStatus("0");
    }

    public void ledBlinkClicked(View view) {
        FirebaseFireStoreHelper.updateLedStatus("2");
    }
}
