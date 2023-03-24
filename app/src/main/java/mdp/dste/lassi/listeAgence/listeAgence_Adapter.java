package mdp.dste.lassi.listeAgence;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mdp.dste.lassi.R;
import mdp.dste.lassi.agence;

public class listeAgence_Adapter extends RecyclerView.Adapter<mdp.dste.lassi.listeAgence.listeAgence_Adapter.ItemViewHolder>{
    private Context mContext;
    private final ArrayList<listeAgence_Data> arrayList;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView item_desc,item_adr,item_lat,item_lon,item_id,item_nom,item_tel;
        LinearLayout item_liste;
        ImageView item_call;
        RoundedImageView item_image;
        public ItemViewHolder(View itemView) {
            super(itemView);
            item_liste = itemView.findViewById(R.id.item_liste);
            item_image = itemView.findViewById(R.id.item_image);
            item_desc = itemView.findViewById(R.id.item_desc);
            item_call = itemView.findViewById(R.id.call);
            item_tel = itemView.findViewById(R.id.item_tel);
            item_adr = itemView.findViewById(R.id.item_adr);
            item_lon = itemView.findViewById(R.id.item_lon);
            item_lat = itemView.findViewById(R.id.item_lat);
            item_id = itemView.findViewById(R.id.item_id);
            item_nom = itemView.findViewById(R.id.item_nom);
        }
    }

    public listeAgence_Adapter(Context c, ArrayList<listeAgence_Data> arrayList) {
        mContext = c;
        this.arrayList = arrayList;
    }

    @Override
    public mdp.dste.lassi.listeAgence.listeAgence_Adapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agence, parent, false);
        mdp.dste.lassi.listeAgence.listeAgence_Adapter.ItemViewHolder itemViewHolder = new mdp.dste.lassi.listeAgence.listeAgence_Adapter.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(mdp.dste.lassi.listeAgence.listeAgence_Adapter.ItemViewHolder holder, final int position) {

        final listeAgence_Data Data = arrayList.get(position);
        holder.item_adr.setText(Data.adr_agence);
        holder.item_lat.setText(Data.lat_agence);
        holder.item_lon.setText(Data.lon_agence);
        holder.item_desc.setText(Data.desc_agence);
        holder.item_id.setText(Data.id_agence);
        holder.item_nom.setText(Data.nom_agence);
        holder.item_tel.setText(Data.tel_agence);
        if(!Data.image_agence.equals("")) {
            Picasso.get().load("https://sourcedusuccesinternational.com/mapsapi/images/" + Data.image_agence).into(holder.item_image);
        }

        holder.item_liste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((agence)mContext).recuperation_info(Data.id_agence,Data.nom_agence,Data.tel_agence,Data.lon_agence,Data.lat_agence,Data.adr_agence,Data.desc_agence,Data.image_agence);
            }
        });

        holder.item_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((agence)mContext).Appeler(Data.tel_agence);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
