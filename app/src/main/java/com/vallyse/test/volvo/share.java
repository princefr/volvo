package com.vallyse.test.volvo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.savvi.rangedatepicker.CalendarPickerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class share extends AppCompatActivity {
    private FABProgressCircle fabProgressCircle;
    private android.support.v7.widget.Toolbar toolbar;
    private CalendarPickerView calendarPickerView;
    private SweetAlertDialog mDialog;
    private static final String TAG = "FacebookLogin";
    private static final int REQUEST_INVITE = 100;
    private FirebaseUser user;
    private DatabaseReference geofireRef;
    private DatabaseReference ref;
    private GeoFire geoFire;
    private DatabaseReference UserRef;
    private static int SPLASH_TIME_OUT = 1000;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
    private ImageView image;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private  int IsThisImage;
    private LinearLayout livraisonadress;
    private TextView lieu;
    private LatLng position;
    private String clientadress;
    private Geocoder mGeocoder;
    private String  carChoosed;
    private Boolean isChoosed = false;
    private TextView modeleChoosed;


    private LinearLayout moedelchoosing;

    private Boolean isZoned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        ref = FirebaseDatabase.getInstance().getReference("publication");
        geofireRef = FirebaseDatabase.getInstance().getReference("geofire");
        geoFire = new GeoFire(geofireRef);
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference("users");

        Log.i("firebaseRef", ref.toString());

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        modeleChoosed = (TextView) findViewById(R.id.modeleChoosed);
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
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

        mGeocoder = new Geocoder(share.this, Locale.getDefault());


        moedelchoosing = (LinearLayout) findViewById(R.id.moedelchoosing);
        moedelchoosing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(share.this);

                // String array for alert dialog multi choice items
                final String[] colors = new String[]{
                        "XC-60",
                        "XC-40",
                        "XC-90"
                };



                final List<String> colorsList = Arrays.asList(colors);

                builder.setSingleChoiceItems(colors, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            carChoosed = "XC-60";
                        }else if(i == 1){
                            carChoosed = "XC-40";
                        }else if(i == 1){
                            carChoosed = "XC-90";
                        }

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isChoosed = true;
                        modeleChoosed.setText(carChoosed);
                        Log.i("TAGR", i + " " + "j'ai sselectionné");

                    }
                }).setNegativeButton("fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isChoosed = false;
                        Log.i("TAGR", i + " " + "is negative");
                    }
                });

                builder.setCancelable(false);
                builder.setTitle("Choisissez votre volvo!");
                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();




            }
        });




        fabProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isZoned == false || isChoosed == false){
                            fabProgressCircle.hide();
                            Log.i("TAG", "he suis ici pour te niqué");
                        }else{
                            PublishMyShare("dada", carChoosed, clientadress, position, "blabla");
                        }
                    }
                },SPLASH_TIME_OUT);

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


        image = (ImageView) findViewById(R.id.image);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsThisImage = 0;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsThisImage = 1;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsThisImage = 2;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsThisImage = 3;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        livraisonadress = (LinearLayout) findViewById(R.id.livraisonadress);
        livraisonadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(share.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });



    }


    public void ShowCustomDialog(){
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("vous avez partager votre voiture");
        mDialog.setCancelable(false);
        mDialog.setConfirmText("Inviter un ami");
        mDialog.show();

    }

    public void ShowInvitelog(){
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Invitez des amis");
        mDialog.setContentText("Invitez des amis à essayer votre volvo ?");
        mDialog.setCancelable(false);
        mDialog.setConfirmText("invitez");
        mDialog.setCancelText("non");
        mDialog.setConfirmText("Inviter un ami");
        mDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                onInviteClicked("francois");
            }
        });
        mDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
            }
        });
        mDialog.show();

    }

    public void ShowErrorDialog(){
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Une erreur s'est produite, recommencez");
        mDialog.setCancelable(true);
        mDialog.show();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public static   class SharePublication {
        public String date;
        public String modele;
        public String addresse;
        public String photo;
        public String UserId;

        public SharePublication() {

        }

        public SharePublication(String date, String modele, String addresse, String photo, String UserId) {
            this.date = date;
            this.modele = modele;
            this.addresse = addresse;
            this.photo = photo;
            this.UserId = UserId;
        }

    }


    private String getCityNameByCoordinates(double lat, double lon) throws IOException {

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getLocality();
        }
        return null;
    }

    public void PublishMyShare(String date, String modele , String address, LatLng position, String photo){
        String key = ref.push().getKey();
        SharePublication publi = new SharePublication(date, modele, address, photo, user.getUid());
        ref.child(key).setValue(publi);
        UserRef.child(user.getUid()).child("publication").child("isActive").child(key).setValue(true);
        geoFire.setLocation(key, new GeoLocation( position.latitude, position.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if(error != null){

                }else{
                    fabProgressCircle.beginFinalAnimation();
                    ShowInvitelog();
                }
            }
        });

    }


    private void onInviteClicked(String name) {
        Intent intent = new AppInviteInvitation.IntentBuilder("Inviter un ami")
                .setMessage("Votre ami(e) !" + " " + name + " " + "vous invite à découvrir la vExperience Volvo !")
                .setDeepLink(Uri.parse("https://play.google.com/store/apps/details?id=com.vallyse.test.volvo"))
                .setCustomImage(Uri.parse("https://assets.volvocars.com/fr-be/~/media/shared-assets/master/images/pages/xc40/pdp/v316_slider_module_c_01_v1.jpg"))
                .setCallToActionText("Découvrir ")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    finish();
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }  else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(IsThisImage == 0){
                image.setImageBitmap(imageBitmap);
            }else if(IsThisImage == 1){
                image2.setImageBitmap(imageBitmap);
            }else if(IsThisImage == 2){
                image3.setImageBitmap(imageBitmap);
            }else{
                image4.setImageBitmap(imageBitmap);
            }

        }else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                position = place.getLatLng();
                clientadress = place.getAddress().toString();
                lieu.setText(place.getAddress().toString());

                try {
                  String adress = getCityNameByCoordinates(position.latitude, position.longitude);
                    Log.i("TAG", adress);
                  if(adress.matches("Paris") || adress.matches("Bordeaux")){
                      isZoned = true;
                  }else{
                      isZoned = false;
                  }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "Error " + status, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



}
