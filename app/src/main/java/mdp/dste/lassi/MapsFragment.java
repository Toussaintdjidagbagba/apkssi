package mdp.dste.lassi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.util.HashMap;

public class MapsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private double mParam1;
    private double mParam2;
    private String data;

    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance(String data, double param1, double param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1); //getString(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            data = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialisation de la view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            data = getArguments().getString(ARG_PARAM3);

        }
        // Initialisation map fragment

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                //Positionner la caméra à l'emplacement actuel
                LatLng centrerCamera = new LatLng(mParam1, mParam2);
                MarkerOptions markerOpt = new MarkerOptions();
                markerOpt.position(centrerCamera);
                markerOpt.title("Ma position actuelle.");
                Log.d("Position :", "Latitude " + mParam1 + " longitude : " + mParam2);
                markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centrerCamera, 10));
                Context context;


                /*if (ActivityCompat.checkSelfPermission(((MapsAllAgence) context).getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(true); */
                googleMap.addMarker(markerOpt);

                /////////////////////////////////////

                try {
                    JSONObject entries = null;
                    entries = new JSONObject(data);
                    JSONArray hasil = entries.getJSONArray("allagences");

                    LatLng centrerCame;
                    Log.d("Position :", "taille "+hasil.length());
                    for (int i= 0; i < hasil.length(); i++) {
                        JSONObject rg = hasil.getJSONObject(i);
                        centrerCame = new LatLng(Double.parseDouble(rg.getString("latitude")), Double.parseDouble(rg.getString("longitude")));
                        Log.d("Position :", "latitude "+Double.parseDouble(rg.getString("latitude"))+ " longitude "+rg.getString("longitude"));
                        markerOpt.position(centrerCame);
                        markerOpt.title(rg.getString("name"));
                        markerOpt.snippet(rg.getString("description"));
                        markerOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointssi));
                        googleMap.addMarker(markerOpt);
                    }
                    /* JSONObject rg = hasil.getJSONObject(2);
                    centrerCame = new LatLng(Double.parseDouble(rg.getString"latitude")), Double.parseDouble(rg.getString("longitude")));
                    Log.d("Position :", "Latitude "+Double.parseDouble(rg.getString("latitude"))+ " longitude "+rg.getString("longitude"));
                    markerOpt.position(centrerCame);
                    markerOpt.title(rg.getString("name"));
                    markerOpt.snippet(rg.getString("description"));
                    markerOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointssi));
                    googleMap.addMarker(markerOpt); */
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /////////////////////////////////////

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng);

                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        googleMap.clear();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng,10
                        ));

                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

        return view;
    }
}