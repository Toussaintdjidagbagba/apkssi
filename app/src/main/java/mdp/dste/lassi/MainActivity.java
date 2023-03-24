package mdp.dste.lassi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton mAddSiteFab,mAddRechercherFab,mAddAgenceFab,mAddVenteFab, mAddQuitterFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addSiteActionText,addRechercherActionText,addVenteActionText,addAgenceActionText, addQuitterActionText;
    Boolean isAllFabsVisible;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private WebView webView;
    public String weburlrecu;
    SharedPreferences sh = null;
    SharedPreferences.Editor she;
    Common checkIn = new Common();

    private GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sh = getSharedPreferences(getApplicationContext().getPackageName(),MODE_PRIVATE);

        webView = (WebView) findViewById(R.id.webView1);

        mAddFab = findViewById(R.id.add_fab);
        // FAB button
        mAddSiteFab = findViewById(R.id.add_site_fab);
        mAddRechercherFab = findViewById(R.id.add_rechercher_fab);
        mAddAgenceFab = findViewById(R.id.add_agence_fab);
        mAddVenteFab = findViewById(R.id.add_vente_fab);
        mAddQuitterFab = findViewById(R.id.add_quitter_fab);

        addSiteActionText = findViewById(R.id.add_site_action_text);
        addRechercherActionText = findViewById(R.id.add_rechercher_action_text);
        addAgenceActionText = findViewById(R.id.add_agence_action_text);
        addVenteActionText = findViewById(R.id.add_vente_action_text);
        addQuitterActionText = findViewById(R.id.add_quitter_action_text);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        weburlrecu = getIntent().getStringExtra("urlsend");


        mAddSiteFab.setVisibility(View.GONE);
        mAddRechercherFab.setVisibility(View.GONE);
        mAddAgenceFab.setVisibility(View.GONE);
        mAddVenteFab.setVisibility(View.GONE);
        mAddQuitterFab.setVisibility(View.GONE);
        addSiteActionText.setVisibility(View.GONE);
        addRechercherActionText.setVisibility(View.GONE);
        addAgenceActionText.setVisibility(View.GONE);
        addVenteActionText.setVisibility(View.GONE);
        addQuitterActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;
        mAddFab.shrink();

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {
                            mAddSiteFab.show();
                            mAddRechercherFab.show();
                            mAddAgenceFab.show();
                            mAddVenteFab.show();
                            mAddQuitterFab.show();
                            addSiteActionText.setVisibility(View.VISIBLE);
                            addRechercherActionText.setVisibility(View.VISIBLE);
                            addAgenceActionText.setVisibility(View.VISIBLE);
                            addVenteActionText.setVisibility(View.VISIBLE);
                            addQuitterActionText.setVisibility(View.VISIBLE);
                            mAddFab.extend();
                            isAllFabsVisible = true;
                        } else {
                            mAddSiteFab.hide();
                            mAddRechercherFab.hide();
                            mAddAgenceFab.hide();
                            mAddVenteFab.hide();
                            mAddQuitterFab.hide();
                            addSiteActionText.setVisibility(View.GONE);
                            addRechercherActionText.setVisibility(View.GONE);
                            addAgenceActionText.setVisibility(View.GONE);
                            addVenteActionText.setVisibility(View.GONE);
                            addQuitterActionText.setVisibility(View.GONE);
                            mAddFab.shrink();

                            isAllFabsVisible = false;
                        }
                    }
                });

        mAddQuitterFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        she = sh.edit();
                        she.clear().commit();
                        finishAffinity();
                    }
                });

        mAddAgenceFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getLocation(view,MapsAllAgence.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mAddVenteFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getLocation(view,MapsAllVente.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        mAddRechercherFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getLocation(view,agence.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        mAddSiteFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Vous êtes déjà sur le site", Toast.LENGTH_SHORT).show();
                    }
                });

    // connexion
        if (!checkIn.isConnectedToInternet(this)) {

            Intent nei = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(nei);
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);

            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setAllowContentAccess(true);

            webView.setWebViewClient(new WebViewClient());

            webView.clearCache(true);
            webView.clearHistory();

            if(sh.getString("isClick","").equals("true")) {
                she = sh.edit();
                she.clear().commit();
                webView.loadUrl(weburlrecu);
            }else{
                webView.loadUrl("https://sourcedusuccesinternational.com/connexion");
            }
        }


    }

    public void getLocation(View view, Class m) throws IOException {
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            String adr = gpsTracker.getAdresse();

            if (m == agence.class){
                String webUrl = webView.getUrl();
                Intent i = new Intent(MainActivity.this, m);
                i.putExtra("longitude",longitude);
                i.putExtra("latitude",latitude);
                i.putExtra("weburl", webUrl);
                startActivity(i);
            }
            else{
                Intent i = new Intent(MainActivity.this, m);
                i.putExtra("longitude",longitude);
                i.putExtra("latitude",latitude);
                startActivity(i);
            }

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}