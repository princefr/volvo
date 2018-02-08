package com.vallyse.test.volvo;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.vallyse.test.volvo.User.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class Login extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "FacebookLogin";
    private LoginButton loginButton;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private CallbackManager callbackManager;
    private User muser;
    private DatabaseReference ref;
    private  GraphRequest graphRequest;
    private String first_name;
    private String last_name;
    private String gender;
    private String name;
    private String age_range;
    private String verifed;
    private String picture;
    private String locale;
    private String timezone;
    private String cover;
    private String email;

    SweetAlertDialog pDialog;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();



        // [START initialize_fblogin]
        // Initialize Facebook Login button

        loginButton = findViewById(R.id.login_query);
        loginButton.setReadPermissions("email", "public_profile", "user_birthday", "user_hometown");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookData(loginResult.getAccessToken());
                ShowCustomDialog();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "une erreur s'est produite, veuillez reesayer", Toast.LENGTH_LONG).show();
                Log.i(TAG, "handleFacebookAccessToken:" + error);
            }
        });


    }



    private void getFacebookData(final AccessToken accessToken){
        graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(response.getError() != null){
                    Log.i("tag", response.getError().toString());
                }else{
                    try {
                        first_name = object.getString("first_name");
                        last_name = object.getString("last_name");
                        gender = object.getString("gender");
                        name = object.getString("name");
                        age_range = object.getString("age_range");
                        verifed = object.getString("verified");
                        picture =  object.getString("picture");
                        locale =  object.getString("locale");
                        timezone =  object.getString("timezone");
                        cover =  object.getString("cover");
                        email =  object.getString("email");
                        handleFacebookAccessToken(accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("Tag", response.toString());
                }
            }
        });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,name, last_name, gender, link, age_range, verified, picture, locale, timezone, cover, email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }


    public void ShowCustomDialog(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Connexion en cours");
        pDialog.setCancelable(false);
        pDialog.show();
        // cancel
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.i(TAG, "handleFacebookAccessToken:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Task<AuthResult> authResultTask;
        authResultTask = mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            ref.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        startActivity(new Intent(Login.this, home.class));
                                    }else{
                                        muser = new User(first_name, last_name, user.getDisplayName(), user.getUid(), email, user.getPhoneNumber(), user.getPhotoUrl().toString(), null, null, verifed == "true" ? true: false, null, null, gender, locale, timezone, age_range);
                                        ref.child("users").child(user.getUid()).setValue(muser);
                                        startActivity(new Intent(Login.this, home.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                       // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        requestCodeQRCodePermissions();

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.BLUETOOTH};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "L'accès à ces fonctionnalités sont nécéssaires pour le bon fonctionnement de l'application", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
