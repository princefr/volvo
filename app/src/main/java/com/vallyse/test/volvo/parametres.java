package com.vallyse.test.volvo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class parametres extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private LinearLayout informations;
    private LinearLayout Adresses;
    private LinearLayout Verification;
    private LinearLayout conditions;
    private LinearLayout mpreferences;
    private LinearLayout propos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_left_arrow);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setTitleTextAppearance(this, R.style.TitilliumTextAppearance);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        informations = (LinearLayout) findViewById(R.id.informations);
        Adresses = (LinearLayout) findViewById(R.id.Adresses);


        informations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parametres.this, mes_informations.class));
            }
        });


        Adresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parametres.this, Adresse.class));
            }
        });


        Verification = (LinearLayout) findViewById(R.id.Verification);

        Verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parametres.this, verification_identite.class));

            }
        });


        conditions = (LinearLayout) findViewById(R.id.conditions);
        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parametres.this, conditions.class));
            }
        });


        mpreferences = (LinearLayout) findViewById(R.id.mpreferences);
        mpreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parametres.this, preferences.class));
            }
        });


        propos = (LinearLayout) findViewById(R.id.propos);
        propos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parametres.this, apropos.class));
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
