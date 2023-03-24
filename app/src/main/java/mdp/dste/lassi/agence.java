package mdp.dste.lassi;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mdp.dste.lassi.listeAgence.listeAgence_Adapter;
import mdp.dste.lassi.listeAgence.listeAgence_Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class agence extends AppCompatActivity {

    EditText text_rechercher;
    Button btn_rechercher;
    ImageView icon_next;
    ScrollView scrollView;
    RecyclerView recyclerView;
    LinearLayout lin_progress;
    RelativeLayout page;
    androidx.appcompat.app.AlertDialog alertDialog = null;
    ArrayList<listeAgence_Data> donnee_array;
    private static final int REQUEST_CALL = 1;
    String numero;
    FloatingActionButton mAddSiteFab,mAddAgenceFab,mAddVenteFab,mAddRechercherFab, mAddQuitterFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addSiteActionText,addAgenceActionText,addVenteActionText,addRechercherActionText, addQuitterActionText;
    Boolean isAllFabsVisible;
    SharedPreferences sh = null;
    SharedPreferences.Editor she;
    Double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agence);

        text_rechercher = findViewById(R.id.text_rechercher);
        btn_rechercher = findViewById(R.id.btn_rechercher);
        icon_next = findViewById(R.id.icon_next);
        scrollView = findViewById(R.id.scrollview);
        recyclerView = findViewById(R.id.recycler_liste);
        lin_progress = findViewById(R.id.lin_progress);
        page = findViewById(R.id.page);

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

        sh = getSharedPreferences(getApplicationContext().getPackageName(),MODE_PRIVATE);

        btn_rechercher.setBackgroundColor(ContextCompat.getColor(agence.this,R.color.silver));

        icon_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(agence.this,MainActivity.class));
            }
        });

        btn_rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Allezsurinternet();
            }
        });

        String url = getIntent().getStringExtra("weburl");
        longitude = getIntent().getDoubleExtra("longitude", 0.00);
        latitude = getIntent().getDoubleExtra("latitude", 0.00);


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
                        Intent i = new Intent(agence.this, MapsAllAgence.class);
                        i.putExtra("longitude",longitude);
                        i.putExtra("latitude",latitude);
                        startActivity(i);
                    }
                });
        mAddVenteFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(agence.this, MapsAllVente.class);
                        in.putExtra("longitude",longitude);
                        in.putExtra("latitude",latitude);
                        startActivity(in);
                    }
                });

        mAddRechercherFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(agence.this, "Vous etes déjà sur cette page", Toast.LENGTH_SHORT).show();
                    }
                });

        mAddSiteFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        she = sh.edit();
                        she.putString("isClick","true").commit();
                        Intent inte = new Intent(agence.this, MainActivity.class);
                        inte.putExtra("urlsend", url);
                        startActivity(inte);
                    }
                });



        lin_progress.setVisibility(VISIBLE);
        scrollView.setVisibility(View.GONE);

        // RecyclerView Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(agence.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(null);

        CompleteRv();

        scrollView.setVisibility(View.VISIBLE);
        lin_progress.setVisibility(View.GONE);
    }

    public void CompleteRv() {

        OkHttpClient client = new OkHttpClient();
        String url = "https://sourcedusuccesinternational.com/mapsapi/getallagence.php?datassi=ssiapp";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Erreur" , e.toString());
                //e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponses = response.body().string();

                    agence.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject entries = null;
                            try {
                                entries = new JSONObject(myResponses);
                                if ((entries.getString("error")).equals("false")) {
                                    JSONArray donne = entries.getJSONArray("allagences");
                                    if(donne.length() == 0){
                                        if (alertDialog != null) {
                                            alertDialog.dismiss();
                                            alertDialog = null;
                                        }

                                        alertDialog = new androidx.appcompat.app.AlertDialog.Builder(agence.this).create();
                                        alertDialog.setTitle("Alerte");
                                        alertDialog.setMessage("Aucune agence n'est disponible");
                                        alertDialog.setIcon(R.drawable.icon_info);
                                        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                    else{
                                        Populate_RV(entries);
                                        she = sh.edit();
                                        she.putString("agenceExist","true").commit();
                                    }

                                } else {

                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                        alertDialog = null;
                                    }

                                    alertDialog = new androidx.appcompat.app.AlertDialog.Builder(agence.this).create();
                                    alertDialog.setTitle("Alerte");
                                    alertDialog.setMessage("Un problème est survenu. Veuillez rééssayez plus tard.");
                                    alertDialog.setIcon(R.drawable.icon_info);
                                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();


                                    // Aucun client trouvé

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void Populate_RV(JSONObject entries) {
        donnee_array = new ArrayList<listeAgence_Data>();
        donnee_array.clear();
        recyclerView.setAdapter(null);

        // progress_abon.setVisibility(View.GONE);

        try {
            //datassi = ssiapp
            recyclerView.setVisibility(VISIBLE);
            JSONArray hasil = entries.getJSONArray("allagences");
            for (int i= 0; i < hasil.length(); i++) {
                JSONObject rg = hasil.getJSONObject(i);
                String get_image = rg.getString("images");
                String get_nom = rg.getString("name");
                String get_id = rg.getString("id");
                String get_ad = rg.getString("adresse");
                String get_tel = rg.getString("phone");
                String get_lon = rg.getString("longitude");
                String get_lat = rg.getString("latitude");
                String get_desc = rg.getString("description");
                donnee_array.add(new listeAgence_Data(get_image, get_nom, get_id, get_desc, get_tel, get_ad, get_lon, get_lat));
            }

            listeAgence_Adapter adapter = new listeAgence_Adapter( agence.this, donnee_array);
            recyclerView.setAdapter(adapter);

        }
        catch(Exception e ) {
            Log.e("pfff", e.toString());
        }
    }

    public void recuperation_info(String id,String nom,String tel,String lon,String lat,String adr,String desc,String image){
        Intent i = new Intent(agence.this,detail_agence.class);
        i.putExtra("id",id);
        i.putExtra("lon",lon);
        i.putExtra("nom",nom);
        i.putExtra("tel",tel);
        i.putExtra("lat",lat);
        i.putExtra("adr",adr);
        i.putExtra("image",image);
        i.putExtra("desc",desc);
        i.putExtra("longitude",longitude);
        i.putExtra("latitude",latitude);
        startActivity(i);
    }

    private void Allezsurinternet(){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://sourcedusuccesinternational.com/mapsapi/searchssi.php?datassi=ssiapp&cle="+text_rechercher.getText().toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Erreur : ",e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if ( response.isSuccessful() ) {
                    final String resultat = response.body().string();
                    Log.e("erreur", resultat);
                    agence.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Convzertir enJSON
                            JSONObject retour = null;
                            try {
                                retour = new JSONObject(resultat);
                                    if ((retour.getString("error")).equals("false")) {
                                        // verifie si le tableau est vide
                                        JSONArray donnee = retour.getJSONArray("allagences");
                                        if(donnee.length() == 0){
                                            if (alertDialog != null) {
                                                alertDialog.dismiss();
                                                alertDialog = null;
                                            }

                                            alertDialog = new androidx.appcompat.app.AlertDialog.Builder(agence.this).create();
                                            alertDialog.setTitle("Alerte");
                                            alertDialog.setMessage("Aucun produit de ce nom n'est disponible");
                                            alertDialog.setIcon(R.drawable.icon_info);
                                            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            alertDialog.show();

                                        }
                                        else{
                                            Populate_RV(retour);
                                        }

                                    } else {

                                        if (alertDialog != null) {
                                            alertDialog.dismiss();
                                            alertDialog = null;
                                        }

                                        alertDialog = new androidx.appcompat.app.AlertDialog.Builder(agence.this).create();
                                        alertDialog.setTitle("Alerte");
                                        alertDialog.setMessage("Un problème est survenu lors de la recherche. Veuillez rééssayez plus tard.");
                                        alertDialog.setIcon(R.drawable.icon_info);
                                        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.show();


                                        // Aucun client trouvé

                                    }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    // Un probleme est survenu
                }

            }
        });

    }


    public void Appeler(String num){
        numero = num;
        String number = "tel:"+ num;
        if (ContextCompat.checkSelfPermission(agence.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(agence.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(number)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Appeler(numero);
            }else{
                Toast.makeText(agence.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}