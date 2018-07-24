package com.example.b_wi.sample1;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Order extends AppCompatActivity {
    FloatingActionButton cancel , confirm;
    TextView orderid,location,quantity,amount;
    int userid;
    EditText name;
    String r=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        cancel=(FloatingActionButton)findViewById(R.id.cancel);
        confirm=(FloatingActionButton)findViewById(R.id.confirm);
        orderid=(TextView)findViewById(R.id.orderidValue);
        location=(TextView)findViewById(R.id.location);
        quantity=(TextView)findViewById(R.id.quantity);
        amount=(TextView)findViewById(R.id.amount);
        name=(EditText)findViewById(R.id.name);

        Bundle bundle= getIntent().getExtras();
        if(bundle !=null) {
            userid=bundle.getInt("userID");
             r=bundle.getString("orderID");
            String loc=bundle.getString("location");
            int q = bundle.getInt("NoOfCans");
            int a = bundle.getInt("amount");

            orderid.setText(r);
            amount.setText(Integer.toString(a));
            quantity.setText(Integer.toString(q));
            location.setText(loc);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper d = new DatabaseHelper(getApplicationContext(),null,null,1);
                if(d.cancelOrder(Integer.parseInt(orderid.getText().toString())));
                Toast.makeText(getApplicationContext(),"your order is cancelled",Toast.LENGTH_SHORT).show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });


    }

    public void insert() {
        order m = new order(    userid,
                                Integer.parseInt(orderid.getText().toString()),
                                Integer.parseInt(quantity.getText().toString()),
                                Integer.parseInt(amount.getText().toString()),
                                location.getText().toString(),
                                name.getText().toString());

        DatabaseHelper d = new DatabaseHelper(getApplicationContext(),null,null,1);
        d.addOrder(m);
        //Toast.makeText(this,"Your order has been placed successfully",Toast.LENGTH_SHORT).show();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle("ORDER ID:"+ r);

        // Setting Dialog Message
        alertDialog.setMessage("Your order is successfully placed!");

        alertDialog.show();
    }

}

