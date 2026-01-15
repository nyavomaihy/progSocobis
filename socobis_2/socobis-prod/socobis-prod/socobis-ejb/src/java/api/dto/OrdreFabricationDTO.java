package api.dto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import bean.*;
import demande.DemandeTransfert;
import demande.DemandeTransfertFille;
import historique.MapUtilisateur;
import produits.Ingredients;
import produits.Recette;
import stock.*;
import utils.ConstanteProcess;
import utils.ConstanteSocobis;
import fabrication.OfFille;

public class OrdreFabricationDTO implements java.io.Serializable{
    String id,lancePar,cible,remarque,libelle,idBc;
    Date besoin,daty;
    String etatLib;
    OfFille[] ofFilles;

    public OrdreFabricationDTO(String id, String lancePar, String cible, String remarque, String libelle, String idBc,
            Date besoin, Date daty, String etatLib, OfFille[] ofFilles) {
        this.id = id;
        this.lancePar = lancePar;
        this.cible = cible;
        this.remarque = remarque;
        this.libelle = libelle;
        this.idBc = idBc;
        this.besoin = besoin;
        this.daty = daty;
        this.etatLib = etatLib;
        this.ofFilles = ofFilles;
    }

    public boolean getEstIndexable() {
        return true;
    }

    public String getEtatLib() {
        return etatLib;
    }

    public void setEtatLib(String etatLib) {
        this.etatLib = etatLib;
    }


    public  String getNomClasseFille()
    {
        return "fabrication.OfFille";
    }
    public String getLiaisonFille() {
        return "idMere";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLancePar() {
        return lancePar;
    }

    public void setLancePar(String lancePar) {
        this.lancePar = lancePar;
    }

    public String getCible() {
        return cible;
    }

    public void setCible(String cible) {
        this.cible = cible;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getBesoin() {
        return besoin;
    }

    public void setBesoin(Date besoin) {
        this.besoin = besoin;
    }

    public Date getDaty() {
        return daty;
    }

    public String getIdBc() {
        return idBc;
    }

    public void setIdBc(String idBc) {
        this.idBc = idBc;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

}
