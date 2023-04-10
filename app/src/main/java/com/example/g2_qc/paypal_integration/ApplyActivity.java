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

/**
 ApplyActivity class to handle PayPal payments.
 */
public class ApplyActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = ApplyActivity.class.getName();
    private static final String USER_PAYMENTS_NODE = "payments";
    private static final String WAGE_KEY = "wage";
    private static final String PAY_ID_KEY = "id";
    private static final String PAY_STATE_KEY = "state";

    // UI Elements
    public EditText enter_amount;
    private Button payNowBtn;
    private TextView paymentStatus;
    private TextView wageTV;

    // Variables
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    private String wage;
    private String postUserId;
    private String postId;
    private String email;
    private double wageDouble;
    private double totalIncomeDataBase;

    /**
     Called when the activity is starting.
     @param savedInstanceState If the activity is being re-initialized after previously being
            shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
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
        postUserId = intent.getStringExtra("userId");
        postId = intent.getStringExtra("postId");
        email = intent.getStringExtra("email");

        // Retrieve the wage from the Intent
        wage = intent.getStringExtra(WAGE_KEY);
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

    /**
     Initializes UI elements.
     */
    private void init() {
        enter_amount = findViewById(R.id.enterAmtET);
        payNowBtn = findViewById(R.id.payNowBtn);
        paymentStatus = findViewById(R.id.paymentStatusTV);
        wageTV = findViewById(R.id.wageTV);
    }

    /**
     * Configures PayPal settings.
     */
    private void configPayPal() {
        //defining we're using SANDBOX Environment and setting the paypal client id
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(client_ID.PAYPAL_CLIENT_ID);
    }

    /**
     * Initializes the ActivityResultLauncher to handle the PayPal payment activity result.
     */
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

        // Set up the PayPal configuration and launch the PayPal activity when the "Pay Now" button is clicked
        payNowBtn.setOnClickListener(v -> {
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(wageDouble), "USD", "Payment for job post ID: " + postId,
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(ApplyActivity.this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            activityResultLauncher.launch(intent);
        });
    }

    /**
     Saves the payment details to the Firebase Realtime Database and updates the current user's income with the wage amount.
     @param paymentId The ID of the payment.
     @param state The state of the payment (e.g. "approved", "completed", etc.).
     @param wage The wage amount paid to the employee.
     @param postUserId The ID of the user who posted the job.
     @param postId The ID of the job post.
     */
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
                    updateCurrentUserIncome(postUserId, wage);

                    // Delete the post from the database
                    deleteJobPostFromDatabase(postUserId, postId);

                    // Show an alert dialog to indicate that the payment was successful
                    showPaymentSuccessDialog(email, postUserId, postId, wage);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error saving payment to database", e));
    }

    /**
     * Sets the event listeners for the pay button.
     */
    private void setListeners() {
        //event listeners for the pay button
        payNowBtn.setOnClickListener(v -> processPayment());
    }

    /**
     * Processes the payment by getting the tip amount entered by the user, calculating the total payment amount,
     * showing a confirmation dialog to the user, and launching the PayPal PaymentActivity with the total amount.
     */
    public void processPayment() {

        // Get the tip amount entered by the user
        final String tipAmountStr = enter_amount.getText().toString();
        final double tipAmount = tipAmountStr.isEmpty() ? 0.0 : Double.parseDouble(tipAmountStr);

        // Calculate the total payment amount by adding the tip amount to the wage amount
        final double totalAmount = wageDouble + tipAmount;
        totalIncomeDataBase = totalAmount;

        // Show a confirmation dialog to the user with the total amount
        showPaymentConfirmationDialog();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    /**
     * Updates the payment status TextView with the given status and payment ID.
     * @param status The status of the payment (e.g. "approved", "completed", etc.).
     * @param paymentId The ID of the payment.
     */
    private void updatePaymentStatus(String status, String paymentId) {
        final String message = String.format("Payment %s with payment id %s", status, paymentId);
        paymentStatus.setText(message);
    }

    /**
     * Sets the wage amount for the current job.
     * @param wageDouble The wage amount for the current job.
     */
    public void setWageDouble(double wageDouble) {
        this.wageDouble = wageDouble;
    }

    /**
     * Gets the wage amount for the current job.
     * @return The wage amount for the current job.
     */
    public double getWageDouble() {
        return wageDouble;
    }

    private void showPaymentSuccessDialog(String email, String postUserId, String postId, double wage) {
        // Show an alert dialog to indicate that the payment was successful
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Successful");
        builder.setMessage("The payment has been successfully completed. The money is now on hold until both sides agree that the work is done. Please contact the employee at "+ email +" to discuss further details.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Update current user's income
            updateCurrentUserIncome(postUserId, wage);

            // Delete job post from database
            deleteJobPostFromDatabase(postUserId, postId);

            // Use a Handler to post a delayed Runnable that performs the intent to the main page
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, MainPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }, 1000); // Wait 1 second before performing the intent
        });
        builder.show();
    }

    private void updateCurrentUserIncome(String postUserId, double wage) {
        // Get a reference to the "Users" node in the Firebase Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Query the database for the current user's income
        userRef.child(postUserId).child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double currentIncome = snapshot.child("totalIncome").getValue(Double.class);

                    // Update the user's income by adding the wage amount to it
                    double updatedIncome = currentIncome + wage;
                    userRef.child(postUserId).child("History").child("totalIncome").setValue(updatedIncome);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void deleteJobPostFromDatabase(String postUserId, String postId) {
        // Get a reference to the "Employee" node in the Firebase Database for the current user
        final DatabaseReference userEmployeeRef = FirebaseDatabase.getInstance().getReference().child("Users").child(postUserId).child("Employee");

        // Remove the job post from the "Posts" node in the database
        userEmployeeRef.child("Posts").child(postId).removeValue()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Job post deleted from database"))
                .addOnFailureListener(e -> Log.e(TAG, "Error deleting job post from database", e));
    }

    /**
     * Displays an alert dialog to confirm the payment details and process the payment.
     * If the user confirms the payment, the payment process will be initiated and the payment status will be updated.
     */
    private void showPaymentConfirmationDialog() {
        // Get the tip amount entered by the user
        final String tipAmountStr = enter_amount.getText().toString();
        final double tipAmount = tipAmountStr.isEmpty() ? 0.0 : Double.parseDouble(tipAmountStr);

        // Calculate the total payment amount by adding the tip amount to the wage amount
        final double totalAmount = wageDouble + tipAmount;
        totalIncomeDataBase = totalAmount;

        // Show an alert dialog with the total amount and the confirmation button
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
}
