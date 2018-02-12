package com.vallyse.test.volvo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;

import org.json.JSONException;

import xyz.belvi.luhn.Luhn;
import xyz.belvi.luhn.cardValidator.models.LuhnCard;
import xyz.belvi.luhn.interfaces.LuhnCallback;
import xyz.belvi.luhn.interfaces.LuhnCardVerifier;


import static com.vallyse.test.volvo.helpers.utils.ListAllCard;
import static com.vallyse.test.volvo.helpers.utils.addcreditcardSource;


public class Paymentmethod extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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

        Stripe.apiKey = "sk_test_ivrZh5kjN5EkHC5BYJF54qIS";
        getALlExternalAcount();


        addcreditcard = (ImageButton) findViewById(R.id.addcreditcard);
        addcreditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Luhn.startLuhn(Paymentmethod.this, new LuhnCallback() {
                    @Override
                    public void cardDetailsRetrieved(final Context luhnContext, LuhnCard creditCard, final LuhnCardVerifier cardVerifier) {
                       cardVerifier.startProgress();
                        String cardname = creditCard.getCardName();
                        final String number = creditCard.getPan();
                        final String cvv = creditCard.getCvv();
                        final int month = creditCard.getExpMonth();
                        final int year = creditCard.getExpYear();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            ExternalAccount externalAccount = addcreditcardSource(month, year, number, "cus_CJ1FL6Xb7MWB8J", cvv);
                                        } catch (CardException e) {
                                            e.printStackTrace();
                                        } catch (APIException e) {
                                            e.printStackTrace();
                                        } catch (AuthenticationException e) {
                                            e.printStackTrace();
                                        } catch (InvalidRequestException e) {
                                            e.printStackTrace();
                                        } catch (APIConnectionException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        cardVerifier.dismissProgress();
                                        ((Activity)(luhnContext)).finish();
                                    }
                                });

                                thread.start();

                            }
                        }, 2500);







                    }

                    @Override
                    public void otpRetrieved(Context luhnContext, LuhnCardVerifier cardVerifier, String otp) {

                    }

                    @Override
                    public void onFinished(boolean isVerified) {
                        Log.i("CREDIT", isVerified + " " + "bitch");
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


    public void getALlExternalAcount()  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ExternalAccountCollection externalAccountCollection =  ListAllCard("cus_CJ1FL6Xb7MWB8J", 3);
                    for (ExternalAccount fd: externalAccountCollection.getData()){
                        Log.i("TAG", fd.toString());
                    }
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
