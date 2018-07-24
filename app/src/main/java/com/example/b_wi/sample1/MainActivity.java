package com.example.b_wi.sample1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText ph,otpuser,countryCode,pwd,pwdre;

    String phoneNumber, otp,verificationCode;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ph = (EditText) findViewById(R.id.phone);
        otpuser=(EditText) findViewById(R.id.otp);
        countryCode = (EditText) findViewById(R.id.country_code);
        pwd = (EditText) findViewById(R.id.pwd);
        pwdre = (EditText) findViewById(R.id.pwd_confirmation);


        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("", "onVerificationCompleted:" + credential);
                Toast.makeText(getApplicationContext(),"Signin completed with otp:"+credential,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("exp:",e.toString());
                Toast.makeText(MainActivity.this,"verification failed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId,token);

                verificationCode = verificationId;
                Toast.makeText(MainActivity.this,"Code sent to user",Toast.LENGTH_SHORT).show();
            }
        };


    }

    public void send_sms(View view){
        phoneNumber=countryCode.getText()+ph.getEditableText().toString();
        //check if the phone number exists already in the db , else send otp
        DatabaseHelper d = new DatabaseHelper(getApplicationContext(), null, null, 1);
        Log.d("in loop","in sending otp loop");
        User t = d.findHandler(phoneNumber);
       // Log.d("user details:",t.phone);
        if (t != null) {
            Toast.makeText(getApplicationContext(),"user found",Toast.LENGTH_SHORT);
            ph.setError("User already exists");
            ph.setText(" ");
        }
        else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
    }

    public void verify(View view){
        //verify user typed otp and sent otp

        String input_code=otpuser.getEditableText().toString();

        verifyPhoneNumber(verificationCode,input_code);
    }

    private void verifyPhoneNumber(String verificationCode,String input_code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCode, input_code);
        signInWithPhone(credential);
    }

    //TODO: make the user enter the otp using an alert box and then make the passwords visible ,as of now direct connection
    private void signInWithPhone(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String p = pwd.getEditableText().toString();
                            String r = pwdre.getEditableText().toString();

                            Toast.makeText(MainActivity.this, "Verification Succesfull!", Toast.LENGTH_SHORT).show();
                            DatabaseHelper d = new DatabaseHelper(getApplicationContext(),null,null,1);
                                if (p.equals(r)) {
                                    User w = new User(p, phoneNumber);
                                    d.addUser(w);
                                    Log.d("note:","User added");
                                    Intent i = new Intent(getApplicationContext(),location.class);
                                    startActivity(i);
                                    //Toast.makeText(MainActivity.this, "User added!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Passwords Don't match", Toast.LENGTH_SHORT).show();
                                    pwd.setText(" ");
                                    pwdre.setText(" ");
                                }


                        }else{
                            Toast.makeText(MainActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            otpuser.setText(" ");
                        }

                    }
                });


    }



}