package mdp.dste.lassi;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import mdp.dste.lassi.MyInfoWindow;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private double lat = 6.368867669448247, lon = 2.451517917215824, lonpos, latpos;
    private String titre = "ddd", description = "ff", image,id;
    private Location loc;
    private  String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        titre = getIntent().getStringExtra("titre");
        description = getIntent().getStringExtra("description");
        lat = Double.parseDouble(getIntent().getStringExtra("latitudeAgence"));
        lon = Double.parseDouble(getIntent().getStringExtra("longitudeAgence"));
        lonpos = getIntent().getDoubleExtra("lonpos", 0.00);
        latpos = getIntent().getDoubleExtra("latpos", 0.00);

        // convertir les donnees en tableau JSON

        try {
            JSONObject jo = new JSONObject();
            jo.put("latitude",lat);
            jo.put("longitude",lon);
            jo.put("name",titre);
            jo.put("description",description);

            JSONArray ja = new JSONArray();
            ja.put(jo);
            JSONObject mainObj = new JSONObject();
            mainObj.put("allagences",ja);
            data = mainObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(MapsActivity.this, "latitudeAgence : "+lat+" longitudeAgence : "+lon, Toast.LENGTH_LONG).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); */

        Fragment fragment = MapsFragment.newInstance(data, latpos, lonpos);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, fragment)
                .commit();
        //mapFragment.getMapAsync(this);

    }



}
