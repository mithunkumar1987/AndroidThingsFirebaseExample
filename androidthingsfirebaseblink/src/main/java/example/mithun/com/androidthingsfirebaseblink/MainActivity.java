package example.mithun.com.androidthingsfirebaseblink;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import example.mithun.com.androidthingsfirebaseblink.FirebaseFirestore.FirebaseFirestoreHelper;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;
    private static final String LED = "BCM6";

    private Handler mHandler = new Handler();
    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PeripheralManager manager = PeripheralManager.getInstance();

        try {
            mLedGpio = manager.openGpio(LED);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
            // Post a Runnable that continuously switch the state of the GPIO, blinking the
            // corresponding LED
            //mHandler.post(mBlinkRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
        FirebaseFirestoreHelper.getLedOnStatus(ledHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove pending blink Runnable from the handler.
        mHandler.removeCallbacks(mBlinkRunnable);
        // Close the Gpio pin.
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mLedGpio.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mLedGpio = null;
        }
    }

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLedGpio == null) {
                return;
            }
            try {
                // Toggle the GPIO state
                mLedGpio.setValue(!mLedGpio.getValue());
                Log.d(TAG, "State set to " + mLedGpio.getValue());

                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };

    public static enum LED_STATUS {
        LED_OFF, LED_ON, LED_BLINK
    }

    Handler ledHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LED_STATUS status = LED_STATUS.values()[msg.what];
            mHandler.removeCallbacks(mBlinkRunnable);
            try {
                switch ((status)) {
                    case LED_ON:
                        Log.i(TAG, "LED_ON");
                        mLedGpio.setValue(true);
                        break;

                    case LED_BLINK:
                        Log.i(TAG, "LED_BLINK");
                        mHandler.post(mBlinkRunnable);
                        break;

                    case LED_OFF:
                    default:
                        Log.i(TAG, "LED_OFF");
                        mLedGpio.setValue(false);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}