package mdp.dste.lassi.listeAgence;

public class listeAgence_Data {
    public String image_agence,nom_agence,id_agence,desc_agence,tel_agence, adr_agence,lon_agence, lat_agence;

    public listeAgence_Data(String image,  String nom, String id, String desc, String tel, String adr, String lon, String lat) {
        this.image_agence = image;
        this.nom_agence = nom;
        this.id_agence = id;
        this.desc_agence = desc;
        this.tel_agence = tel;
        this.adr_agence = adr;
        this.lon_agence = lon;
        this.lat_agence = lat;
    }
}
