package mdp.dste.lassi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class detail_agence extends AppCompatActivity {
    Button btn_maps;
    TextView description,latitude,longitude,adresse,nom,telephone,idagence;
    ImageView image;
    private String idAgence, nomAgence, descAgence, adrAgence, lonAgence, latAgence,telAgence, imageAgence;
    Double lonpos, latpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_agence);

        btn_maps = findViewById(R.id.btn_maps);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        adresse = findViewById(R.id.adresse);
        nom = findViewById(R.id.nom);
        telephone = findViewById(R.id.telephone);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        idagence = findViewById(R.id.idagence);

        btn_maps.setBackgroundColor(ContextCompat.getColor(detail_agence.this,R.color.mon_bleu));

         idAgence = getIntent().getStringExtra("id");
         nomAgence = getIntent().getStringExtra("nom");
         descAgence = getIntent().getStringExtra("desc");
         adrAgence = getIntent().getStringExtra("adr");
         imageAgence = getIntent().getStringExtra("image");
         lonAgence = getIntent().getStringExtra("lon");
         latAgence = getIntent().getStringExtra("lat");
         telAgence = getIntent().getStringExtra("tel");
         latpos = getIntent().getDoubleExtra("latitude", 0.00);
         lonpos = getIntent().getDoubleExtra("longitude", 0.00);

         nom.setText(nomAgence);
         description.setText(descAgence);
         telephone.setText(telAgence);
         idagence.setText(idAgence);
         adresse.setText(adrAgence);
         longitude.setText(lonAgence);
         latitude.setText(latAgence);
        Picasso.get().load("https://sourcedusuccesinternational.com/mapsapi/images/" + imageAgence).into(image);

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detail_agence.this, MapsActivity.class);
                intent.putExtra("id",idAgence);
                intent.putExtra("image",imageAgence);
                intent.putExtra("titre",nomAgence);
                intent.putExtra("description",descAgence);
                intent.putExtra("longitudeAgence",lonAgence);
                intent.putExtra("latitudeAgence",latAgence);
                intent.putExtra("lonpos",lonpos);
                intent.putExtra("latpos",latpos);

                startActivity(intent);
            }
        });
    }
}