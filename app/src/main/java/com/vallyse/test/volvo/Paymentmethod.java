package com.vallyse.test.volvo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import xyz.belvi.luhn.Luhn;
import xyz.belvi.luhn.cardValidator.models.LuhnCard;
import xyz.belvi.luhn.interfaces.LuhnCallback;
import xyz.belvi.luhn.interfaces.LuhnCardVerifier;

public class Paymentmethod extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton addcreditcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmethod);


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


        addcreditcard = (ImageButton) findViewById(R.id.addcreditcard);
        addcreditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Luhn.startLuhn(Paymentmethod.this, new LuhnCallback() {
                    @Override
                    public void cardDetailsRetrieved(Context luhnContext, LuhnCard creditCard, LuhnCardVerifier cardVerifier) {

                    }

                    @Override
                    public void otpRetrieved(Context luhnContext, LuhnCardVerifier cardVerifier, String otp) {

                    }

                    @Override
                    public void onFinished(boolean isVerified) {

                    }
                });
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
