package com.vallyse.test.volvo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.vallyse.test.volvo.adaptater.travel_pager;
import com.vallyse.test.volvo.views.OfoContentLayout;
import com.vallyse.test.volvo.views.OfoMenuLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class home extends AppCompatActivity implements OnMapReadyCallback, GeoQueryEventListener, MapboxMap.OnMyLocationChangeListener, LocationEngineListener {

    private Button book;
    private LinearLayout sharebutton;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationLayerPlugin locationLayerPlugin;
    private LocationEngine locationEngine;
    private ViewPager viewPager;
    private ArgbEvaluator argbEvaluator;
    private LinearLayout layout_main;
    private ImageView hongbao;
    private LinearLayout bike_info_board;
    private ImageView information;

    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private Map<String,Marker> markers;
    private DatabaseReference Georef;
    private DatabaseReference publicationRef;
    private FrameLayout framlayout;
    private Button bt_loginOrorder;
    private ImageView dingwei;
    private FirebaseUser currentUser;


    private OfoMenuLayout ofoMenuLayout;
    private OfoContentLayout ofoContentLayout;
    private FrameLayout menu;
    private ImageButton rv_ic_menu;
    protected int getType() {
        return MenuBrawable.CONVEX;
    }
    private  Bitmap imageUrl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();


        Mapbox.getInstance(this, "pk.eyJ1IjoicHJpbmNlb25kbyIsImEiOiJjamN1MHd6Y24wbmMzMndudXloZXBqc2hmIn0.SOwDa-39eXDJDN1CcSCcCw");

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        layout_main = (LinearLayout) findViewById(R.id.layout_main);
        bike_info_board = (LinearLayout) findViewById(R.id.bike_info_board);
        information = (ImageView) findViewById(R.id.information);
        framlayout = (FrameLayout) findViewById(R.id.framlayout);





        ofoMenuLayout = ((OfoMenuLayout) findViewById(R.id.ofo_menu));
        ofoContentLayout = ((OfoContentLayout) findViewById(R.id.ofo_content));
        menu = (FrameLayout) findViewById(R.id.menu_content);

        rv_ic_menu = (ImageButton) findViewById(R.id.rv_ic_menu);
        rv_ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_main.setVisibility(View.GONE);
                ofoMenuLayout.setVisibility(View.VISIBLE);
                ofoMenuLayout.open();
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofoMenuLayout.close();
            }
        });

        ofoMenuLayout.setOfoMenuStatusListener(new OfoMenuLayout.OfoMenuStatusListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {
                layout_main.setVisibility(View.VISIBLE);
//                startConcaveBtn.setVisibility(View.VISIBLE);
            }
        });
        //给menu设置content部分
        ofoMenuLayout.setOfoContentLayout(ofoContentLayout);


        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });




        currentUser = FirebaseAuth.getInstance().getCurrentUser();





        //final MenuBrawable menuBrawable = new MenuBrawable(BitmapFactory.decodeResource(getResources(), getDrawableFromUrl(new URL(currentUser.getPhotoUrl().toString())), home.this, menu, getType());
        //final MenuBrawable menuBrawable = new MenuBrawable(BitmapFactory.decodeResource(getResources(), R.mipmap.bitmap), OfoMenuActivity.this, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //menu.setBackground(menuBrawable);
        }





        sharebutton = (LinearLayout) findViewById(R.id.sharebutton);
        hongbao = (ImageView) findViewById(R.id.hongbao);
        dingwei = (ImageView) findViewById(R.id.dingwei);


        dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, retour_constructeur.class));
            }
        });

        hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, share.class));

            }
        });



        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition( layout_main);
                }
                viewPager.setVisibility(View.VISIBLE);
                //mapboxMap.setPadding(0, viewPager.getHeight(), 0, 0);
            }
        });


        bt_loginOrorder = (Button) findViewById(R.id.bt_loginOrorder);

        bt_loginOrorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, Drivesharedcard.class));
            }
        });




        argbEvaluator = new ArgbEvaluator();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int devHeight = displayMetrics.heightPixels;
        int devWidth = displayMetrics.widthPixels;

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpPagerAdapter();
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(-devWidth / 2);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("tag", String.valueOf(position) + " " + "baltazar est la");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position < -1) { // [-Infinity,-1)

                } else if (position <= 1) { // [-1,1]

                    if (position >= -1 && position < 0) {

                        LinearLayout uberEco = (LinearLayout) page.findViewById(R.id.lluberEconomy);
                        TextView uberEcoTv = (TextView) page.findViewById(R.id.tvuberEconomy);

                        if (uberEco != null && uberEcoTv != null) {

                            uberEcoTv.setTextColor((Integer) argbEvaluator.evaluate(-2 * position, getResources().getColor(R.color.black)
                                    , getResources().getColor(R.color.grey)));

                            uberEcoTv.setTextSize(16 + 4 * position);
                            uberEco.setX((page.getWidth() * position));

                        }


                    } else if (position >= 0 && position <= 1) {

                        TextView uberPreTv = (TextView) page.findViewById(R.id.tvuberPre);
                        LinearLayout uberPre = (LinearLayout) page.findViewById(R.id.llUberPre);




                        if (uberPreTv != null && uberPre != null) {

                            uberPreTv.setTextColor((Integer) new ArgbEvaluator().evaluate((1 - position), getResources().getColor(R.color.grey)
                                    , getResources().getColor(R.color.black)));

                            uberPreTv.setTextSize(12 + 4 * (1 - position));
                            uberPre.setX(uberPre.getLeft() + (page.getWidth() * (position)));


                        }


                    }

                }
            }

        });






        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
        geoFire = new GeoFire(ref);
        geoQuery = this.geoFire.queryAtLocation(new GeoLocation(48.864716, 2.349014), 50);
        markers = new HashMap<String, Marker>();
        publicationRef = FirebaseDatabase.getInstance().getReference("publication");



    }


    private void setUpPagerAdapter() {
        List<Integer> data = Arrays.asList(0, 1);
        travel_pager adapter = new travel_pager(data);
        viewPager.setAdapter(adapter);
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin(MapboxMap map) {
        initializeLocationEngine();
        locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);

    }


    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LostLocationEngine(home.this);
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            //originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }




    public void handleS90(View view){
        Intent intent = new Intent(home.this, booking.class);
        intent.putExtra("carModele", "Volvo S90");
        intent.putExtra("modele", "S90");
        startActivity(intent);
    }


    public void handleXC90(View view){
        Intent intent = new Intent(home.this, booking.class);
        intent.putExtra("carModele", "Volvo XC90");
        intent.putExtra("modele", "XC90");
        startActivity(intent);
    }



    public void handleXC60(View view){
        Intent intent = new Intent(home.this, booking.class);
        intent.putExtra("carModele", "Volvo XC60");
        intent.putExtra("modele", "XC60");
        startActivity(intent);
    }

    public void handleXC40(View view){
        Intent intent = new Intent(home.this, booking.class);
        intent.putExtra("carModele", "Volvo XC40");
        intent.putExtra("modele", "XC40");
        startActivity(intent);
    }


    private Bitmap imagetobitmap;
    public Bitmap getBitmapFromURL(final String src) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    imagetobitmap = BitmapFactory.decodeStream(input);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return imagetobitmap;
    }

    public static Drawable LoadImageFromWebURL(String url) {
        try {
            InputStream iStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(iStream, "src name");
            return drawable;
        } catch (Exception e) {
            return null;
        }}

    @Override
    public void onBackPressed() {
        if(viewPager.getVisibility() == View.VISIBLE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(layout_main);
            }
            viewPager.setVisibility(View.INVISIBLE);
            mapboxMap.setPadding(0, 0, 0, 0);
            return;
        }if(bike_info_board.getVisibility() == View.VISIBLE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(layout_main);
            }
            mapboxMap.setPadding(0, 0, 0, 0);
            bike_info_board.setVisibility(View.GONE);

            return;
        }

        if (ofoMenuLayout.isOpen()) {
            ofoMenuLayout.close();
            return;
        }

        super.onBackPressed();

    }



    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        drawCircle(this.mapboxMap,  new LatLng(48.864716, 2.349014), Color.argb(66, 255, 0, 255), 8000);
        drawCircle(this.mapboxMap,  new LatLng(44.8378, -0.5792), Color.argb(66, 255, 0, 255), 8000);
        enableLocationPlugin(mapboxMap);
        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.i("TAG", "seems like im getting some error" +" " + String.valueOf(marker));
                //Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                //TODO
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(layout_main);
                }
                mapboxMap.setPadding(0,0, 0,   bike_info_board.getHeight());
                bike_info_board.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }


    @Override
    @SuppressLint("MissingPermission")
    public void onStart() {
        super.onStart();
        this.geoQuery.addGeoQueryEventListener(this);
        mapView.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

        this.geoQuery.removeAllListeners();
        for (Marker marker: this.markers.values()) {
            marker.remove();
        }
        this.markers.clear();


    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }


    public static void drawCircle(MapboxMap map, LatLng position, int color, double radiusMeters) {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.alpha(1);
        polygonOptions.strokeColor(color);
        polygonOptions.addAll(getCirclePoints(position, radiusMeters));
        polygonOptions.fillColor(color);
        map.addPolygon(polygonOptions);
    }


    private static ArrayList<LatLng> getCirclePoints(LatLng position, double radius) {
        int degreesBetweenPoints = 10; // change here for shape
        int numberOfPoints = (int) Math.floor(360 / degreesBetweenPoints);
        double distRadians = radius / 6371000.0; // earth radius in meters
        double centerLatRadians = position.getLatitude() * Math.PI / 180;
        double centerLonRadians = position.getLongitude() * Math.PI / 180;
        ArrayList<LatLng> polygons = new ArrayList<>(); // array to hold all the points
        for (int index = 0; index < numberOfPoints; index++) {
            double degrees = index * degreesBetweenPoints;
            double degreeRadians = degrees * Math.PI / 180;
            double pointLatRadians = Math.asin(sin(centerLatRadians) * cos(distRadians)
                    + cos(centerLatRadians) * sin(distRadians) * cos(degreeRadians));
            double pointLonRadians = centerLonRadians + Math.atan2(sin(degreeRadians)
                            * sin(distRadians) * cos(centerLatRadians),
                    cos(distRadians) - sin(centerLatRadians) * sin(pointLatRadians));
            double pointLat = pointLatRadians * 180 / Math.PI;
            double pointLon = pointLonRadians * 180 / Math.PI;
            LatLng point = new LatLng(pointLat, pointLon);
            polygons.add(point);
        }
        // add first point at end to close circle
        polygons.add(polygons.get(0));
        return polygons;
    }





    private void setCameraPosition(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    public void onMyLocationChange(@Nullable Location location) {
        this.geoQuery = this.geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 20);
        setCameraPosition(location);
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.geoQuery = this.geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 20);
        setCameraPosition(location);
    }

    @Override
    public void onKeyEntered(String key, final GeoLocation location) {
        Log.i("Myactivity", String.valueOf(key));
        DatabaseReference tempDataRef = FirebaseDatabase.getInstance().getReference("publication").child(key);
        tempDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Icon icon = IconFactory.getInstance(home.this).fromBitmap(getMarkerBitmapFromView());
                Log.i("TAG", String.valueOf(dataSnapshot.getValue()));
                Marker marker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("blabla").snippet(dataSnapshot.getKey().toString()).icon(icon));
                markers.put(dataSnapshot.getKey(), marker);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Myactivity", String.valueOf(databaseError));
            }
        });
    }

    @Override
    public void onKeyExited(String key) {
        Marker marker = this.markers.get(key);
        if (marker != null) {
            mapboxMap.removeMarker(marker);
            markers.remove(key);
        }
    }

    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
        // Method is used to interpolate the marker animation.

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Marker marker = markers.get(key);
        ValueAnimator markerAnimator = ObjectAnimator.ofObject(marker, "position",
                new LatLngEvaluator(), marker.getPosition(), new LatLng(location.latitude, location.longitude));
        markerAnimator.setDuration(2000);
        markerAnimator.start();
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }


    private Bitmap getMarkerBitmapFromView(){
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
}
