package com.example.b_wi.sample1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;

public class ChangePassword extends AppCompatActivity {

    EditText pass,confirmpass;
    Button ok;
    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pass=(EditText)findViewById(R.id.pwd);
        confirmpass=(EditText)findViewById(R.id.pwd_confirm);
        ok=(Button)findViewById(R.id.button);

        Bundle bundle= getIntent().getExtras();
        uid= bundle.getInt("userid");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass.getEditableText()==null||confirmpass.getEditableText()==null) {
                    if (pass.getEditableText() == null) {
                        pass.setError("Password can not be empty!");
                    }
                    if (confirmpass.getEditableText() == null) {
                        pass.setError("Password can not be empty!");
                    }
                }
                else{
                    //check if passwords are the same
                    if(!pass.getEditableText().toString().equals(confirmpass.getEditableText().toString())){
                        confirmpass.setError("Passwords don't match!");
                        confirmpass.setText(" ");
                    }
                    else{
                        //update the db
                        DatabaseHelper d = new DatabaseHelper(getApplicationContext(),null,null,1);
                        if(d.updatePassword(uid,pass.getEditableText().toString())){
                            Toast.makeText(getApplicationContext(),"Password changed!",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),Login.class);
                            startActivity(i);

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Could not perform operation!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }
}
