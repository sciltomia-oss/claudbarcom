package com.example.claudbarcom;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import com.example.claudbarcom.ui.home.HomeScreenKt;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(() -> HomeScreenKt.HomeScreen());
    }
}