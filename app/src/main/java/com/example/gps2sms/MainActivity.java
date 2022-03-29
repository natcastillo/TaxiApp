package com.example.gps2sms;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView lat, lon, day, time, puerto;
    private EditText ip;
    public String coords;
    public String coords1;
    public String latitud;
    public String longitud;
    public String tiempo;
    public String date;
    private Handler mHandler = new Handler();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText) findViewById(R.id.TextIP);
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.longi);
        day = (TextView) findViewById(R.id.day);
        time = (TextView) findViewById(R.id.time);
        puerto = (TextView) findViewById(R.id.port);

        permisos();


        }



    public void permisos(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1000);
        }
    }

    public String localizacion(View view) {

        actu.run();

        return null;
    }

    private Runnable actu = new Runnable(){

        @Override
        public void run(){

            LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {


                public void onLocationChanged(Location location) {

                    CharSequence fecha = DateFormat.format("yyyy-MM-dd", location.getTime());
                    String fecha1 = String.valueOf(fecha);

                    Long time1 = location.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String hora = sdf.format(time1);
                    Log.i("tiempo", "tiempo: " + hora);

                    String myLatitude = String.valueOf(location.getLatitude());
                    String myLongitude = String.valueOf(location.getLongitude());
                    latitud = myLatitude;
                    longitud = myLongitude;
                    tiempo = hora;
                    date = fecha1;

                    Log.i("Coords", "Coordenadas: " + coords);

                    coords = latitud + "," + longitud + "," + tiempo + "," + date;
                    lat.setText(latitud);
                    lon.setText(longitud);
                    time.setText(tiempo);
                    day.setText(date);


                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(@NonNull String provider) {
                }

                public void onProviderDisabled(@NonNull String provider) {
                }
            };

            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            mHandler.postDelayed(actu, 10000);

        }

    };

    public String sendUDP (View view){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (coords != null && ip != null && puerto != null) {

                    Log.i("tiempo", "tiempo1: " + tiempo);


                    byte[] buffer;

                    try {
                        // Para enviar por UDP
                        DatagramSocket socketudp = new DatagramSocket();

                        try {

                            InetAddress address = InetAddress.getByName(ip.getText().toString());
                            int port = Integer.parseInt(puerto.getText().toString());

                            buffer = coords.getBytes();

                            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length, address, port);
                            socketudp.send(peticion);
                            Log.i("Confirmation", "Packet Sent!");

                        }catch(NumberFormatException ex){

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        }, 0, 10000);


        return null;
    }



}


    
