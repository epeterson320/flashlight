package co.ericp.flashlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Screen to all white
    }

    @Override
    protected void onDestroy() {
        // Release anything?
        super.onDestroy();
    }
}
