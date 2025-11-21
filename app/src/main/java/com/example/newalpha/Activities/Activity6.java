package com.example.newalpha.Activities;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class Activity6 extends MasterActivity implements OnMapReadyCallback
{
    private final String LOC = "Nahum Sarig Street";
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap gMap;
    TextView distanceTV;
    EditText searchED;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_6);
        
        distanceTV = findViewById(R.id.distanceTV);
        searchED = findViewById(R.id.searchED);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setTrafficEnabled(true);
    }

    public void search(View view) 
    {
//        String search = searchED.getText().toString();

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //got the last known location
                        if (location != null)
                        {
                            //create a route
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            gMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                            Geocoder geocoder = new Geocoder(Activity6.this);
                            try
                            {
//                                List<Address> addresses = geocoder.getFromLocationName(search, 1);
                                List<Address> addresses = geocoder.getFromLocationName(LOC, 1);
                                if (addresses != null && !addresses.isEmpty())
                                {
                                    Address address = addresses.get(0);
                                    LatLng destinationLatLng = new LatLng(address.getLatitude(), address.getLongitude());
//                                    gMap.addMarker(new MarkerOptions().position(destinationLatLng).title(search));
                                    gMap.addMarker(new MarkerOptions().position(destinationLatLng).title(LOC));

                                    gMap.addPolyline(new PolylineOptions()
                                            .add(currentLatLng, destinationLatLng)
                                            .width(5)
                                            .color(Color.RED));

                                    float[] results = new float[1];
                                    Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, destinationLatLng.latitude, destinationLatLng.longitude, results);
                                    float distance = results[0];
                                    distanceTV.setText("Distance: " + String.format("%.2f", distance / 1000) + " km");
                                }
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            // Logic to handle location object
                        }
                    }
                });
        //TODO: get the user's current location, show the user the route on the map and show the distance in the distanceTV
        
    }
}