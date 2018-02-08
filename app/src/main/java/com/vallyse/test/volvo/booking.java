package com.vallyse.test.volvo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.vallyse.test.volvo.User.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class booking extends AppCompatActivity {
    private CalendarPickerView calendarPickerView;
    private RelativeLayout actionbar;
    private android.support.v7.widget.Toolbar toolbar;
    private FABProgressCircle fabProgressCircle;
    private LinearLayout crenauxhoraire;
    private TextView crenautext;
    private PlaceAutocomplete placeAutocomplete;
    private LinearLayout livraisonadress;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private TextView lieu;
    private SweetAlertDialog mDialog;
    private String DepostiAdress;
    private Integer minutes;
    private Integer hours;
    private FirebaseUser user;
    private DatabaseReference ref;
    private User myinfo;
    private String carIntent;
    private String carModele;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        getUser();
        Intent intent = getIntent();
        toolbar.setTitle("Reserver un essai - " + intent.getStringExtra("carModele"));
        carIntent = intent.getStringExtra("carModele");
        carModele = intent.getStringExtra("modele");


        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCirclebooking);
        crenautext  = (TextView) findViewById(R.id.crenautext);
        lieu = (TextView) findViewById(R.id.lieu);




        fabProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(minutes == null || hours == null || DepostiAdress == null || calendarPickerView.getSelectedDate() == null){
                    Snackbar mysnackbar = Snackbar.make(findViewById(R.id.framebooking), "veuillez remplir tous les champs", Snackbar.LENGTH_LONG);
                    mysnackbar.show();
                }else{
                  JSONObject data = GetJSON(DepostiAdress, hours, minutes, calendarPickerView.getSelectedDate(), myinfo.firstname, myinfo.lastname, myinfo.displayName, myinfo.email, carModele);
                    try {
                        MakeRequest(data);
                        ShowLoadingDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("Gadt", data.toString());
                }


            }
        });

        crenauxhoraire = (LinearLayout) findViewById(R.id.crenauxhoraire);

        crenauxhoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(booking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        minutes = selectedMinute;
                        hours = selectedHour;
                        crenautext.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Choisissez l'heure de début ddu test");
                mTimePicker.show();
            }
        });


        livraisonadress = (LinearLayout) findViewById(R.id.livraisonadress);
        livraisonadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(booking.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
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
        getUser();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                DepostiAdress = place.getAddress().toString();
                lieu.setText(place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "Error " + status, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



    public void getUser(){
        ref.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myinfo = dataSnapshot.getValue(User.class);

                Log.i("tag", dataSnapshot.getValue().toString() + " "  +  myinfo.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("d", databaseError.toString());
            }
        });

    }


    public void ShowCustomDialog(){
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Test commandé");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void ShowLoadingDialog(){
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Commande du test en cours");
        mDialog.setCancelable(false);
        mDialog.show();
    }


    public void ShowErrorDialog(){
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Erreur, veuillez recommencer");
        mDialog.show();
    }


    public void MakeRequest(JSONObject object) throws IOException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://service-dev.parkopoly.fr/api/mission";

        fabProgressCircle.show();
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("qrcode", "qrcode");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                fabProgressCircle.beginFinalAnimation();
                mDialog.dismissWithAnimation();
                ShowCustomDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       finish();
                    }
                },1000);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GAB", error.toString());
                mDialog.dismissWithAnimation();
                ShowErrorDialog();
                fabProgressCircle.hide();
                //Snackbar mysnackbar = Snackbar.make(findViewById(R.id.framebooking), "une erreur s'est produite, veuillez ressayer", Snackbar.LENGTH_LONG);
                //mysnackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      mDialog.dismissWithAnimation();
                        //fabProgressCircle.hide();
                    }
                },3000);


            }
        }){

            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwb25kb25kYUBnbWFpbC5jb20iLCJhdXRoIjoiUk9MRV9VU0VSX01BTkFHRVIsUk9MRV9VU0VSX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTUyMDQzNDYyNiwiaWF0IjoxNTE3ODQyNjI2fQ.jwQmAzzvBftFUrHzGZpPYf4SipHVYI-XG1Yb6eeRdN4blqaE0OLrEelkqsFdfqDP3XZjG00bMCEzXZPVyz3Ivg");
                headers.put("X-App-Type", "prescriber");
                return headers;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    public JSONObject GetJSON(String address, Integer hour, Integer Minutes, Date date, String firstname, String lastname, String displayname, String email, String modele){
        JSONObject jsonObject = new JSONObject();
        JSONObject customerObject = new JSONObject();
        JSONObject driverLicenceObject = new JSONObject();
        JSONArray arrayList = new JSONArray();
        JSONObject deliveryObject = new JSONObject();
        JSONObject carObject = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        date.setHours(hour);
        date.setMinutes(Minutes);
        try {
            // customer object
            jsonObject.put("address", null);
            jsonObject.put("gender", "Mr");
            jsonObject.put("firstName", firstname);
            jsonObject.put("lastName", lastname);
            jsonObject.put("companyName", "mardi");
            jsonObject.put("displayName", displayname);
            jsonObject.put("phoneNumber", "0123456789");
            jsonObject.put("email", email);
            driverLicenceObject.put("number", null);
            driverLicenceObject.put("deliveredLocation", null);
            driverLicenceObject.put("deliveredDate", null);
            jsonObject.put("driverLicense", driverLicenceObject);
            jsonObject.put("secondDriverLicense", null);
            jsonObject.put("deliveredDate", null);
            jsonObject.put("birthDate", null);
            jsonObject.put("birthLocation", null);
            customerObject.put("customer", jsonObject);
            // car object
            carObject.put("type", "VC");
            carObject.put("brand", "VC");
            carObject.put("model", modele);
            carObject.put("color", "RED");
            carObject.put("plateNumber", "CV-123-CD");
            carObject.put("serialNumber", "CVFDCVFDCV-VIN");
            carObject.put("display", null);
            carObject.put("newCar", false);
            customerObject.put("car", carObject);
            customerObject.put("brandId", "58de476aa029d65042c091af");
            customerObject.put("bookingCodeId", "58e27aa0e4b0c27d4893d016");
            customerObject.put("concessionToInvoiceId", null);
            customerObject.put("missionType", "DELIVERY");
            customerObject.put("withReplacementCar", false);

            // delivery object
            deliveryObject.put("company", false);
            deliveryObject.put("companyName", null);
            deliveryObject.put("concessionId", "59dd2e19e4b0c750ee0353e7" );
            deliveryObject.put("concessionIdVR", "59dd2e19e4b0c750ee0353e7");
            deliveryObject.put("contactName", null);
            deliveryObject.put("contactPhone", null);
            deliveryObject.put("driverMessage", null);
            deliveryObject.put("rdvAddress", address);
            arrayList.put("5a338036e4b02247308fec08");
            arrayList.put("5a2504bfe4b0ed6bbee1a0fd");
            arrayList.put("59e5d52de4b032ea3d2af250");
            arrayList.put("599aa989e4b0655d4a401343");
            arrayList.put("59dcc2f0e4b0c750ee03531d");
            arrayList.put("599eec49e4b068415a6b7219");
            arrayList.put("598d6086e4b0cf683ba769cc");
            arrayList.put("598d5a11e4b0cf683ba769c7");
            deliveryObject.put("zoneCustomerIdSet", arrayList);
            deliveryObject.put("zoneDriverStartId", "5a0411b8e4b0c798c50850bb");
            deliveryObject.put("zoneDriverEndId", "5a04116ce4b0c798c50850ba");
            deliveryObject.put("customerArrivalDateTime", dateFormat.format(date));
            customerObject.put("delivery", deliveryObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return customerObject;
    }



}
