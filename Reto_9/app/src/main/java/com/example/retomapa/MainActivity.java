package com.example.retomapa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    private EditText edtLatitud;

    private EditText edtLongitud;

    private Button btnCurrentLocation;

    private Button btnSearchLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        //Almacenar en variables los botones y cajas de texto de la interfaz
        edtLatitud = findViewById(R.id.ETlatitud);
        edtLongitud = findViewById(R.id.ETlongitud);
        btnCurrentLocation = findViewById(R.id.BtnCurrent);
        btnSearchLocation = findViewById(R.id.BtnSearch);

        //inflate and create the map
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);

        //Habilitar los controles y definir el tilesource
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        //Habilitar la rotacion
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(getApplicationContext(), map);
        mRotationGestureOverlay.setEnabled(true);
        map.getOverlays().add(mRotationGestureOverlay);

        //Acceder a la ubicación del dispositivo y generar un icono sobre la ubicacion actual
        GpsMyLocationProvider mGpsMyLocationProvider = new GpsMyLocationProvider(ctx);
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(mGpsMyLocationProvider, map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);

        //Mostrar brujula en la esquina superior izquierda
        CompassOverlay mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        //Mostrar escala del mapa
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        //play around with these values to get the location on screen in the right place for your application
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        //Ubicar inicialmente el mapa en la posicion actual (o la ultima posicion vista por el gps)
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double currentLongitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        double currentLatitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        IMapController mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(currentLatitude, currentLongitude);
        mapController.setCenter(startPoint);

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getBaseContext(),"Latitud: "+p.getLatitude() + "\nLongitud: "+p.getLongitude(),Toast.LENGTH_SHORT).show();
                Geocoder gcd = new Geocoder(ctx, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(p.getLatitude(),p.getLongitude(),1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (addresses.size()>0){
                    Toast.makeText(getBaseContext(),"Lugar: "+ addresses.get(0).getFeatureName(),Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);

        // if you need to show the current location, uncomment the line below
        // WRITE_EXTERNAL_STORAGE is required in order to show the map
        //Los permisos solo se solicitan la primera vez que se utiliza el mapa (Tiene bugs que hace que los permisos se soliciten continuamente y danhan app)
        //requestPermissionsIfNecessary(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE});

    }


    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void goToCurrent(View view) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        double currentLongitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        double currentLatitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();

        IMapController mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint point = new GeoPoint(currentLatitude, currentLongitude);
        mapController.setCenter(point);
    }

    public void goToSearch(View view) {
        //Verificar si el usuario ingreso latitud y longitud
        edtLatitud = findViewById(R.id.ETlatitud);
        edtLongitud = findViewById(R.id.ETlongitud);
        if (!edtLatitud.getText().toString().matches("")) {
            if (!edtLongitud.getText().toString().matches("")) {
                double latitud = Double.valueOf(edtLatitud.getText().toString());
                double longitud = Double.valueOf(edtLongitud.getText().toString());
                IMapController mapController = map.getController();
                mapController.setZoom(18);
                GeoPoint point = new GeoPoint(latitud, longitud);
                mapController.setCenter(point);
            } else {
                Toast toast = Toast.makeText(this, "Ingresa la longitud", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else{
            Toast toast = Toast.makeText(this, "Ingresa la latitud", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}