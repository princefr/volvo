package com.vallyse.test.volvo.countdown;

/**
 * Created by princejackes on 03/02/2018.
 */

import android.os.CountDownTimer;
import android.widget.TextView;

import com.vallyse.test.volvo.R;


public class countdown extends CountDownTimer {

    public static final int TIME_COUNT = 60000;
    private TextView btn;
    private int endStrRid;

    public countdown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public countdown(TextView btn, int endStrRid) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public countdown(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = R.string.resend_identify_code;
    }

    @Override
    public void onTick(long l) {
        btn.setEnabled(false);
        btn.setText(l / 1000 + "seconde(s)");
    }

    @Override
    public void onFinish() {
        btn.setText(endStrRid);
        btn.setEnabled(true);
    }
}