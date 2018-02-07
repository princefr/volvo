package com.vallyse.test.volvo;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.jorgecastilloprz.FABProgressCircle;

public class pdfpage extends AppCompatActivity implements OnDrawListener, OnLoadCompleteListener, OnPageErrorListener, OnPageScrollListener {

    PDFView pdfView;
    private FABProgressCircle fabProgressCircle;
    private String TAG = "PDFPAGES";
    public static final String SAMPLE_FILE = "sample.pdf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfpage);


        pdfView = (PDFView) findViewById(R.id.pdfView2);
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        fabProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(pdfpage.this, home.class));
                finish();
            }
        });







    }

    @Override
    protected void onStart() {
        super.onStart();
        displayPDF(SAMPLE_FILE);
    }



    private void displayPDF(String assetfinename){
        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .onPageError(this)
                .onLoad(this)
                .onDraw(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .load();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        Log.i(TAG, "i'm drawning like a thug");
    }

    @Override
    public void loadComplete(int nbPages) {
        Log.i(TAG, "loading completed");
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.i(TAG, t.toString());
    }

    @Override
    public void onPageScrolled(int page, float positionOffset) {

    }
}
