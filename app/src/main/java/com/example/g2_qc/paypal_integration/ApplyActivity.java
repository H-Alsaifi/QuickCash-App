package com.example.g2_qc.paypal_integration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ApplyActivity extends AppCompatActivity {

    private static final String TAG = ApplyActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    //UI Elements
    public EditText enter_amount;
    private Button payNowBtn;
    private TextView paymentStatus;
    private String wage;
    private TextView wageTV;

    private double wageDouble;
    private double totalIncomeDataBase;
    private String postUserId;
    private String postId;
    private  String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_layout);


        init();
        configPayPal();
        initActivityLauncher();
        setListeners();

        // Get the Intent that started this activity
        Intent intent = getIntent();
        postUserId = getIntent().getStringExtra("userId");
        postId = getIntent().getStringExtra("postId");
        email = getIntent().getStringExtra("email");

        // Retrieve the wage from the Intent
        wage = intent.getStringExtra("wage");
        if (wage != null) {
            wage = wage.replace("Wage: ","");
            wage = wage.replace("$","");
        }

    // Set the wage as the text for the wageTV TextView
        if (wage != null && !wage.isEmpty()) {
            wageTV.setText(wage);
            wageDouble = Double.parseDouble(wage);
        } else {
            wageTV.setText("0.00");
        }

    }


    private void init() {
        // ui elements
        enter_amount = findViewById(R.id.enterAmtET);
        payNowBtn = findViewById(R.id.payNowBtn);
        paymentStatus = findViewById(R.id.paymentStatusTV);
        wageTV = findViewById(R.id.wageTV);
    }

    private void configPayPal() {
        //defining we're using SANDBOX Environment and setting the paypal client id
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(client_ID.PAYPAL_CLIENT_ID);
    }

    private void initActivityLauncher() {
        // Get the current user's ID
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "payments" node in the Firebase Database for the current user
        final DatabaseReference userPaymentsRef = FirebaseDatabase.getInstance().getReference().child("payments").child(userId);

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

                                // Save the payment details to Firebase Database
                                savePaymentDetailsToDatabase(payID, state, wageDouble, postUserId, postId);

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

    private void savePaymentDetailsToDatabase(String paymentId, String state, double wage, String postUserId, String postId) {
        // Get the current user's ID
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "payments" node in the Firebase Database for the current user
        final DatabaseReference userPaymentsRef = FirebaseDatabase.getInstance().getReference().child("payments").child(userId);

        // Create a new payment object
        final Payment payment = new Payment(userId, paymentId, state, wage, postUserId);

        // Add the payment object to the database
        userPaymentsRef.push().setValue(payment)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Payment saved to database");

                    // Retrieve the current user's income and update it with the wage amount
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(postUserId).child("History");
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                double currentIncome = snapshot.child("totalIncome").getValue(Double.class);

                                // Update the user's income by adding the wage amount to it
                                double updatedIncome = currentIncome + wage;
                                userRef.child("totalIncome").setValue(updatedIncome);

                                // Delete the post from the database
                                FirebaseDatabase.getInstance().getReference().child("Users").child(postUserId).child("Employee").child("Posts").child(postId).removeValue();

                                // Show an alert dialog to indicate that the payment was successful
                                AlertDialog.Builder builder = new AlertDialog.Builder(ApplyActivity.this);
                                builder.setTitle("Payment Successful");
                                builder.setMessage("The payment has been successfully completed. The money is now on hold until both sides agree that the work is done. Please contact the employee at "+ email +" to discuss further details.");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    // Use a Handler to post a delayed Runnable that performs the intent to the main page
                                    new Handler().postDelayed(() -> {
                                        Intent intent = new Intent(ApplyActivity.this, MainPageActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }, 1000); // Wait 5 seconds before performing the intent
                                });
                                builder.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error saving payment to database", e));
    }


    private void setListeners() {
        //event listeners for the pay button
        payNowBtn.setOnClickListener(v -> processPayment());
    }

    public void processPayment() {

        // Get the tip amount entered by the user
        final String tipAmountStr = enter_amount.getText().toString();
        final double tipAmount = tipAmountStr.isEmpty() ? 0.0 : Double.parseDouble(tipAmountStr);

        // Calculate the total payment amount by adding the tip amount to the wage amount
        final double totalAmount = wageDouble + tipAmount;
        totalIncomeDataBase = totalAmount;
        // Show an alert dialog with the total amount
        final String message = String.format("You are about to pay $%.2f (wage: $%.2f + tips: $%.2f)", totalAmount, wageDouble, tipAmount);
        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage(message)
                .setPositiveButton("Pay Now", (dialog, which) -> {
                    // User confirmed the payment, proceed with the payment process

                    // Setting the parameters for payment i.e the amount, the currency, intent of the sale
                    final PayPalPayment payPalPayment = new PayPalPayment(
                            new BigDecimal(totalAmount), "CAD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);

                    //Paypal Payment activity intent
                    final Intent intent = new Intent(this, PaymentActivity.class);

                    //paypal configuration to the intent
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);

                    //paypal payment to the intent
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

                    // Starting Activity Request launcher
                    activityResultLauncher.launch(intent);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void updatePaymentStatus(String status, String paymentId) {
        final String message = String.format("Payment %s with payment id %s", status, paymentId);
        paymentStatus.setText(message);
    }

    public void setWageDouble(double wageDouble) {
        this.wageDouble = wageDouble;
    }

    public double getWageDouble() {
        return wageDouble;
    }
}
