package com.example.b_wi.sample1;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class Login extends AppCompatActivity {

    EditText ph , pass;
    Button signup,ok;
    FloatingActionButton login;
    TextView forgotpass,country_code;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ph=(EditText)findViewById(R.id.phone);
        pass=(EditText)findViewById(R.id.password);
        forgotpass=(TextView)findViewById(R.id.forgot_password);
        country_code=(TextView)findViewById(R.id.country_code);
        signup=(Button)findViewById(R.id.signup);
        login=(FloatingActionButton)findViewById(R.id.login);

        //to make the textview act like a link
        forgotpass.setMovementMethod(LinkMovementMethod.getInstance());

        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.password, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.passwordError);
        awesomeValidation.addValidation(this, R.id.phone, "^[2-9]{2}[0-9]{8}$", R.string.phoneError);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO DO : verify username and password
                if (awesomeValidation.validate()) {
                    DatabaseHelper d = new DatabaseHelper(getApplicationContext(), null, null, 1);

                    String s = country_code.getText().toString() + ph.getEditableText().toString();
                    Log.d("s:", s);
                    User t = d.findHandler(s);
                    if(t==null){
                        Toast.makeText(getApplicationContext(),"Please register",Toast.LENGTH_SHORT).show();
                        ph.setText(" ");
                        pass.setText(" ");
                    }
                    else {
                        String p = pass.getEditableText().toString().trim();
                        Log.d("pass", p);

                        if (p.equals(t.pwd)) {
                            //TODO: get user id also
                            int userId = t.Uid;
                            Toast.makeText(getApplicationContext(), "Login success" + userId, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),location.class);
                            i.putExtra("UserID",userId);
                            startActivity(i);

                            //TODO: connect to maps activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_LONG).show();
                            pass.setText(" ");
                        }
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder= new AlertDialog.Builder(Login.this);
                View mView= getLayoutInflater().inflate(R.layout.dialog,null);
                final EditText phoneno= (EditText) mView.findViewById(R.id.phone);
                final TextView country=(TextView)findViewById(R.id.country_code);
                 ok=(Button) mView.findViewById(R.id.verify);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forgotpass.setTextColor(Color.RED);
                        if(!phoneno.getText().toString().isEmpty()) {
                            //TODO: verify phone no in db
                            DatabaseHelper d = new DatabaseHelper(getApplicationContext(),null,null,1);
                            String search=country.getText()+phoneno.getEditableText().toString();
                            User t =d.findHandler(search);
                            if(t==null){
                                Toast.makeText(Login.this, "Please register!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Login.this, "Phone Number verified", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(Login.this,ChangePassword.class);
                                intent.putExtra("userid",t.Uid);
                                startActivity(intent);
                            }

                        }

                        //Intent intent= new Intent(Login.this,Main2Activity.class);
                       // startActivity(intent);
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog= mBuilder.create();
                dialog.show();
            }
        });
    }
}
