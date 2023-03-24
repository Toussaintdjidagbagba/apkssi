package mdp.dste.lassi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsAllAgence extends AppCompatActivity {

    androidx.appcompat.app.AlertDialog alertDialog = null;
    String data;
    Double lon, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_all_agence);
        
        lon = getIntent().getDoubleExtra("longitude",0.00);
        lat = getIntent().getDoubleExtra("latitude",0.00);

        Allezsurinternet(lat,lon);

    }

    public Context getContext(){
        return MapsAllAgence.this;
    }

    public void Allezsurinternet(Double lat, Double lon) {

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

                    MapsAllAgence.this.runOnUiThread(new Runnable() {
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
                                        alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MapsAllAgence.this).create();
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
                                        data = myResponses;
                                        Toast.makeText(MapsAllAgence.this, "Affichage en cours..  ", Toast.LENGTH_SHORT).show();
                                        //Fragment fragment = new MapsFragment();
                                        //Fragment fragment = MapsFragment.newInstance(data, 2.451517917215824, 6.368867669448247);
                                        Fragment fragment = MapsFragment.newInstance(data, lat, lon);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.frame_layout, fragment)
                                                .commit();
                                    }
                                } else {

                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                        alertDialog = null;
                                    }

                                    alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MapsAllAgence.this).create();
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
}