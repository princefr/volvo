package com.vallyse.test.volvo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.cameraview.CameraView;

public class cardID extends AppCompatActivity {
    private FABProgressCircle fabProgressCircle;
    private CameraView cameraView;
    private ImageButton takepicture;
    private ImageView pictureidone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_id);


        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        cameraView = (CameraView) findViewById(R.id.camera);

        fabProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardID.this, permis.class));
            }
        });

        if (cameraView != null) {
            cameraView.addCallback(mcallback);
        }


        takepicture = (ImageButton) findViewById(R.id.takepicture);
        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "chick chak");
                if(cameraView != null){
                    cameraView.takePicture();
                }

            }
        });

        pictureidone = (ImageView) findViewById(R.id.pictureidone);


    }


    private CameraView.Callback mcallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.i("TAG", "the camera is open");
            //super.onCameraOpened(cameraView);
            Log.i("TAG", "is open");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {

           // super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            Log.i("TAG", "i've taked the picture");
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            pictureidone.setImageBitmap(bitmap);
            //super.onPictureTaken(cameraView, data);
            Log.i("TAG", "i've taked the picture");

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
