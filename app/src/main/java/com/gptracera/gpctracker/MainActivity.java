package com.gptracera.gpctracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocListenerinterface {
    private LocationManager locationManager;
    private TextView tvRestDistance, tvTotal,tvVeloCity;
    private Location lastLocation;
    private MyLocListener myLocListener;
    private int distance;
    private int total_distance;
    private int rest_distance;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void updateDistance(Location loc) {
        if (loc.hasSpeed() && lastLocation != null) {
          if (distance>total_distance)total_distance += lastLocation.distanceTo(loc);
        if (rest_distance>0) rest_distance -= lastLocation.distanceTo(loc);

           progressBar.setProgress(total_distance);

        }
        lastLocation = loc;
        tvRestDistance.setText(String.valueOf(rest_distance));
        tvTotal.setText(String.valueOf(total_distance));
       tvVeloCity.setText(String.valueOf(loc.getSpeed()));

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_leayout, null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed = ad.findViewById(R.id.edText);
                if (ed != null) {
                    if (!ed.getText().toString().equals("")) setDistance(ed.getText().toString());
                }

            }
        });
        builder.setView(cl);
        builder.show();
    }

    private void setDistance(String dis) {
        progressBar.setMax(Integer.parseInt(dis));
        rest_distance=Integer.parseInt(dis);
      distance=Integer.parseInt(dis);
        tvRestDistance.setText(dis);

    }

    private void init() {
        tvTotal = findViewById(R.id.tvTotal);
        tvRestDistance = findViewById(R.id.tvRestDistance);
        tvVeloCity=findViewById(R.id.tvVeloCity);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(1000);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerinterface(this);
        checkPermision();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == RESULT_OK) {
            checkPermision();
//        }else {
//            Toast.makeText(this, "No Gps Permision", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListener);
        }
    }

    @Override
    public void OnLocationChanged(Location loc) {
updateDistance(loc);
    }

    public void onClickDistance(View view) {
        showDialog();
    }
}