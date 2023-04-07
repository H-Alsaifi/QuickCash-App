package com.example.g2_qc.paypal_integration;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.g2_qc.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class paypal_activity  extends AppCompatActivity{

    private static final String TAG = paypal_activity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    //UI Elements
    private EditText enter_amount;
    private Button complete_payment;
    private TextView paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_paypal);

        init();
        configPayPal();
        initActivityLauncher();
        setListeners();
    }

    private void init() {
        // ui elements
        enter_amount = findViewById(R.id.enterAmtET);
        complete_payment = findViewById(R.id.payNowBtn);
        paymentStatus = findViewById(R.id.paymentStatusTV);
    }

    private void configPayPal() {
        //defining we're using SANDBOX Environment and setting the paypal client id
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(client_IDs.PAYPAL_CLIENT_ID);
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                        if (confirmation != null) {
                            try {

                                //payment details
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                Log.i(TAG, paymentDetails);

                                // Extract json response and display it in a text view.
                                JSONObject payObj = new JSONObject(paymentDetails);

                                String payID = payObj.getJSONObject("response").getString("id");
                                String state = payObj.getJSONObject("response").getString("state");

                                paymentStatus.setText(String.format("Payment %s%n with payment id is %s", state, payID));
                            } catch (JSONException e) {

                                Log.e("Error", "an extremely unlikely failure occurred: ", e);
                            }
                        }

                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {

                        //returned invalid result
                        Log.d(TAG, "Launcher Result Invalid");

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                        //when transaction is cancelled
                        Log.d(TAG, "Launcher Result Cancelled");
                    }
                });
    }

    private void setListeners() {
        //event listeners for the pay button
        complete_payment.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {

        //amount from the user
        final String amount = enter_amount.getText().toString();

        //setting the parameters for payment i.e the amount, the currency, intent of the sale
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                amount), "CAD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);


        //Paypal Payment activity intent
        final Intent intent = new Intent(this, PaymentActivity.class);

        //paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);

        //paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        // Starting Activity Request launcher
        activityResultLauncher.launch(intent);
    }
}
