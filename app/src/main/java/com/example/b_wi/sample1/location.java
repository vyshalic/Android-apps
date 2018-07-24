package com.example.b_wi.sample1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class location extends AppCompatActivity implements LocationListener
{
    FloatingActionButton bu1;
    TextView txtLocationAddress;
    SupportMapFragment mapFragment;
    GoogleMap map;
    LatLng center;
    CardView cardView;
    ImageView imageView;

    ElegantNumberButton q;
    TextView mrp;
    FloatingActionButton next;
    AlertDialog.Builder builder;
    int id;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        txtLocationAddress = findViewById(R.id.txtLocationAddress);
        txtLocationAddress.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtLocationAddress.setSingleLine(true);
        txtLocationAddress.setMarqueeRepeatLimit(-1);
        txtLocationAddress.setSelected(true);
        cardView =findViewById(R.id.cardView);
        bu1 = (FloatingActionButton) findViewById(R.id.b1);
        imageView = findViewById(R.id.imageView);

        Bundle bundle= getIntent().getExtras();
        if(bundle !=null) {
            //String u=bundle.getString("uid");
             id=bundle.getInt("UserID");
        }
        q=(ElegantNumberButton)findViewById(R.id.quantity);
        mrp=(TextView)findViewById(R.id.amount);
        next=(FloatingActionButton)findViewById(R.id.next);

        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num=q.getNumber();
                mrp.setText(String.valueOf(Integer.valueOf(num)*Integer.valueOf(String.valueOf(30))));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random rand=new Random();
                int randno=rand.nextInt();
                String no=String.valueOf(randno);
                Intent intent=new Intent(getApplicationContext(),Order.class);
                intent.putExtra("orderID",no);
                intent.putExtra("NoOfCans",Integer.parseInt(q.getNumber()));
                intent.putExtra("amount",Integer.parseInt(mrp.getText().toString()));
                intent.putExtra("location",txtLocationAddress.getText().toString());
                intent.putExtra("userID",id);
                startActivity(intent);

//                //builder.setMessage("hello").setTitle("greeting");
//
//                AlertDialog alertDialog = new AlertDialog.Builder(
//                        location.this).create();
//
//                // Setting Dialog Title
//                alertDialog.setTitle("ORDER DETAILS:");
//
//                StringBuilder d = new StringBuilder();
//                d.append("Quantity: "+q.getNumber()+"\n");
//                d.append("Amount: "+mrp.getText()+"\n");
//                d.append("Location: "+txtLocationAddress.getText().toString()+"\n");
//
//                // Setting Dialog Message
//                alertDialog.setMessage(d.toString());
//
//                alertDialog.show();

            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                map = googleMap;
                map.getUiSettings().setZoomControlsEnabled(true);
                LatLng latLng = new LatLng(11.1271, 78.6569);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                initCameraIdle();
            }
        });
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Change();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(location.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    printToast("Google Play Service Repair");
                } catch (GooglePlayServicesNotAvailableException e) {
                    printToast("Google Play Service Not Available");
                }
            }
        });
    }
    private void initCameraIdle()
    {
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                center = map.getCameraPosition().target;
                getAddressFromLocation(center.latitude, center.longitude);
            }
        });
    }

    private void getAddressFromLocation(double latitude, double longitude)
    {
        LocationManager lm;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        Location currentlocation = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
        if( Math.round(latitude*1000)!=Math.round(currentlocation.getLatitude()*1000) || Math.round(longitude*1000)!=Math.round(currentlocation.getLongitude()*1000))
        {
            ImageButton imgButton = findViewById(R.id.b1);
            imgButton.setImageResource(R.drawable.gps_not_fixed);
        }
        else
        {
            ImageButton imgButton = findViewById(R.id.b1);
            imgButton.setImageResource(R.drawable.ic_current);
        }

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try
        {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
            {
                Address fetchedAddress;
                fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i <= fetchedAddress.getMaxAddressLineIndex(); i++)
                {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }
                txtLocationAddress.setText(strAddress.toString());
            }
            else
            {
                txtLocationAddress.setText("Searching Current Address");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            printToast("Could not get address..!");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (!place.getAddress().toString().contains(place.getName()))
                {
                    txtLocationAddress.setText(place.getName() + ", " + place.getAddress());
                }
                else
                {
                    txtLocationAddress.setText(place.getAddress());
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
                map.animateCamera(cameraUpdate);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                printToast("Error in retrieving place info");
            }
        }
    }
    private void printToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    boolean fl=false;
    public void Change()
    {
        LocationManager lm;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        fl=true;
        Location location = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
        onLocationChanged(location);

    }

    @Override
    public void onLocationChanged(Location location)
    {
        double longitude=location.getLongitude();
        double latitude=location.getLatitude();
        getAddressFromLocation( latitude, longitude);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        if(fl==true) {
            ImageButton imgButton = findViewById(R.id.b1);
            imgButton.setImageResource(R.drawable.ic_current);
            LatLng latLng = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            fl=false;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

