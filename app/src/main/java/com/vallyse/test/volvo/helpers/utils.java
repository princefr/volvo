package com.vallyse.test.volvo.helpers;

/**
 * Created by princejackes on 06/02/2018.
 */

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Account;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.net.RequestOptions;

import org.json.JSONException;

public class utils {
    private static final String LOG_TAG = "IOUtils";
    public static final String PREFS_FILE = "javaeye.prefs";



    public static Drawable getDrawableFromUrl(URL url) {
        try {
            InputStream is = url.openStream();
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return null;
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Log.e(LOG_TAG, e.getMessage());
            }
        }
    }

    private static  Customer CreateUser(String email) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
         return Customer.create(customerParams);
    }



    private static Charge charge(String amount, String CardToken, String customer) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "eur");
        chargeParams.put("source",  CardToken);
        chargeParams.put("customer", customer);
        return Charge.create(chargeParams);
    }


    private static ChargeCollection Allcharges(String customer, String limit) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("limit", limit);
        chargeParams.put("customer", customer);
       return Charge.list(chargeParams);
    }

    public static ExternalAccount addcreditcardSource(Integer month, Integer year, String numbers, String customerID, String cvc) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException, JSONException {
        Customer customer = Customer.retrieve(customerID);
        Map<String, Object> params = new HashMap<String, Object>();

        Map<String, Object> cardObject = new HashMap<String, Object>();

        cardObject.put("exp_month", month);
        cardObject.put("exp_year",  year);
        cardObject.put("number", numbers);
        cardObject.put("cvc", cvc);
        cardObject.put("object", "card");

        params.put("source", cardObject);
        return customer.getSources().create(params);
    }



    public static ExternalAccountCollection ListAllCard(String customerID, Integer limit) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("limit", limit);
        cardParams.put("object", "card");
        return Customer.retrieve(customerID).getSources().all(cardParams);
    }

    private static void Deletesource(String customerID, String cardId) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Customer customer = Customer.retrieve(customerID);
        customer.getSources().retrieve(cardId).delete();

    }


    // coupon managment system


    private static Coupon createCoupon(String couponID, String amount_off, String currency) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> couponParams = new HashMap<String, Object>();
        couponParams.put("amount_off", amount_off);
        couponParams.put("duration", "forever");
        couponParams.put("id", couponID);
        couponParams.put("currency", currency);
        return Coupon.create(couponParams);
    }


    public static Balance retrieveBalance(String customer) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("customer", customer);
        return Balance.retrieve((RequestOptions) customerParams);
    }


    public static Account CreateAccount(String type, String country, String email, String firstname, String last_name, String city, String line1, String postal_code, String state) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> accountParams = new HashMap<String, Object>();
        Map<String, Object> legalEntity = new HashMap<String, Object>();
        Map<String, Object> personal_address = new HashMap<String, Object>();
        
        accountParams.put("type", type);
        accountParams.put("country", country);
        accountParams.put("email", email);
        accountParams.put("business_name", "");
        personal_address.put("city", city);
        personal_address.put("country", country);
        personal_address.put("line1", line1);
        personal_address.put("postal_code", postal_code);
        personal_address.put("state", state);
        legalEntity.put("first_name", firstname);
        legalEntity.put("last_name", last_name);
        legalEntity.put("type", "individual");
        legalEntity.put("personal_address", personal_address);
        accountParams.put("legal_entity",legalEntity);

       return  Account.create(accountParams);
    }


    public static void AddBankAccount(){

    }


}
