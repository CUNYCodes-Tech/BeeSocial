package com.example.beesocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.landing_page);

        //Switches the view to the profile page once the button is clicked
        TextView profile = findViewById(R.id.profileRedirect);
        profile.setMovementMethod(LinkMovementMethod.getInstance());
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Switches the view to the profile page once the button is clicked
        TextView menu = findViewById(R.id.menuRedirect);
        menu.setMovementMethod(LinkMovementMethod.getInstance());
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, DrawerActivity.class);
                startActivity(intent);
            }
        });
    }
}
