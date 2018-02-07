package com.vallyse.test.volvo;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class retour_constructeur extends AppCompatActivity {
    private FABProgressCircle fabProgressCircle;
    private android.support.v7.widget.Toolbar toolbar;
    private CalendarPickerView calendarPickerView;
    private SweetAlertDialog mDialog;
    private TextView lieu;
    private LinearLayout livraisonadress;
    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
    private LinearLayout purpose;
    private LinearLayout crenauxhoraire;
    private TextView crenautext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retour_constructeur);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_bis);
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

        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        lieu = (TextView) findViewById(R.id.lieu);
        purpose = (LinearLayout) findViewById(R.id.purpose);
        crenauxhoraire = (LinearLayout) findViewById(R.id.crenauxhoraire);
        crenautext = (TextView) findViewById(R.id.crenautext);

        crenauxhoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(retour_constructeur.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        crenautext.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Choisissez l'heure de début ddu test");
                mTimePicker.show();
            }
        });


        fabProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },1000);

            }
        });


        purpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(retour_constructeur.this);
                // String array for alert dialog multi choice items
                String[] colors = new String[]{
                        "Climatisation",
                        "Carrosserie",
                        "Freinage",
                        "Embrayage",
                        "Contrôle technique",
                        "Diagnostic electronique",
                        "Distribution",
                        "Echappement",
                        "Révision constructeur",
                        "Vidange",
                        "Pneumatique",
                        "Pré contrôle technique",
                        "Amortisseurs",
                        "Bilan été",
                        "Vitrerie"
                };

                // Boolean array for initial selected items
                final boolean[] checkedColors = new boolean[]{
                        false, // Red
                        false, // Green
                        false,
                        false, // Blue
                        false, // Blue
                        false, // Red
                        false, // Green
                        false,
                        false, // Blue
                        false, // Blue
                        false, // Red
                        false, // Green
                        false,
                        false, // Blue
                        false, // Blue

                };

                final List<String> colorsList = Arrays.asList(colors);
                builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        Log.i("TAG", "hahda");
                    }
                });

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setCancelable(false);
                builder.setTitle("Choisissez votre raison!");
                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);



        calendarPickerView.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(new Date());

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String strdate = "01-02-2018";
        String strdate2 = "15-02-2018";


        try {
            Date newdate = dateformat.parse(strdate);
            Date newdate2 = dateformat.parse(strdate2);
            ArrayList<Date> arrayList = new ArrayList<>();
            arrayList.add(newdate);
            arrayList.add(newdate2);
            calendarPickerView.highlightDates(arrayList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(7);

        calendarPickerView.deactivateDates(list);

        livraisonadress = (LinearLayout) findViewById(R.id.livraisonadress);
        livraisonadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(retour_constructeur.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                lieu.setText(place.getAddress().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "Error " + status, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
