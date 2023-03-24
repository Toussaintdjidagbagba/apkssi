package mdp.dste.lassi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class MyInfoWindow implements GoogleMap.InfoWindowAdapter {

    private String titre ;
    private String description;
    private Context context ;
    private HashMap<String,String> markers ;

    /**
     * Construcuteur
     * @param context
     */
    public MyInfoWindow(Context context, HashMap<String,String> markers) {
        this.context  = context;
        this.markers = markers;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //Récupération du layout correspondant à la fenêtre d'info
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.windowlayout, null);

        //Récupération des éléments du layout
        TextView mTitle = (TextView) v.findViewById(R.id.title);
        TextView mDesc = (TextView) v.findViewById(R.id.description);
        ImageView mImage = (ImageView) v.findViewById(R.id.imageView);

        //Personnalisation des éléments (on récupère les infos du marqueur)
        mTitle.setText(marker.getTitle());
        mDesc.setText(marker.getSnippet());


        if(markers.get(marker.getId()).equals("PEDIATRIE")){
            mImage.setImageResource(R.drawable.logo);
        }else if(markers.get(marker.getId()).equals("licorne")){
            //mImage.setImageResource(R.drawable.licorne);
        }


        // Returning the view containing InfoWindow contents
        return v;
    }
}

