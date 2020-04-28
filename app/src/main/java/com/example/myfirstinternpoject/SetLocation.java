package com.example.myfirstinternpoject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SetLocation extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment supportMapFragment;
    SearchView searchView1;
    private Button shareLocationUser;
    public String location1;
    double latitude_loc = 0.0;
    double longitude_loc = 0.0;

    private DatabaseReference latitude_b;
    private DatabaseReference longitude_b;

    SharedPreferences sharedPreferences;
    static final String latitudeKey = "LatitudeKey";
    static final String longitudeKey = "LongitudeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        shareLocationUser = findViewById(R.id.shareLocationUser);
        searchView1 = findViewById(R.id.sv_location1);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                location1 = searchView1.getQuery().toString();
                List<Address> addressList = null;

                if (location1 != null || !location1.equals("")) {
                    Geocoder geocoder = new Geocoder(SetLocation.this);
                    try {
                        addressList = geocoder.getFromLocationName(location1, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Pickup").draggable(true));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    latitude_loc = address.getLatitude();
                    longitude_loc = address.getLongitude();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(this);

        shareLocationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude_loc == 0.0) {
                    Toast.makeText(SetLocation.this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else {
                    sharedPreferences = getSharedPreferences("SaveData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(latitudeKey, Double.doubleToRawLongBits(latitude_loc));
                    editor.putLong(longitudeKey, Double.doubleToRawLongBits(longitude_loc));
                    editor.commit();
                        latitude_b= FirebaseDatabase.getInstance().getReference();
                        latitude_b.child("Location").child("Lat").setValue(latitude_loc);
                        longitude_b= FirebaseDatabase.getInstance().getReference();
                        longitude_b.child("Location").child("Longi").setValue(longitude_loc);
                    Intent intent = new Intent(SetLocation.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}

