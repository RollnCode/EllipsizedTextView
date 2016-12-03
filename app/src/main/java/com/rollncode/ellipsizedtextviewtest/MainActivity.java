package com.rollncode.ellipsizedtextviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rollncode.ellipsizedtextview.EllipsizedTextView;
import com.rollncode.ellipsizedtextview.OnEllipsizedTextViewListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EllipsizedTextView tvEllipsized = (EllipsizedTextView) findViewById(R.id.el_info);
        tvEllipsized.setOnEllipsizedTextViewListener(new OnEllipsizedTextViewListener() {
            @Override
            public void onTextClicked() {
                Toast.makeText(MainActivity.this, "onTextClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPostfixClicked() {
                Toast.makeText(MainActivity.this, "onPostfixClicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
