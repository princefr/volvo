package com.vallyse.test.volvo;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vallyse.test.volvo.User.User;

public class Adresse extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private User user;
    private FirebaseUser muser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresse);


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

        muser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void UpdateAdresse(User user){

    }


    public  void Loadadresse(){
        userRef.child(muser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
