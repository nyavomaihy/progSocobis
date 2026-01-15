package api.dto;

import java.sql.Date;

public class OrdreFabricationDetailDTO {
    String idIngredients;
    String remarque, libelle, idunite, libIngredients, idBcFille, operateur, idFab;
    double qte;
    Date datyBesoin;
    double equivalence = 1;
    public String getIdIngredients() {
        return idIngredients;
    }
    public void setIdIngredients(String idIngredients) {
        this.idIngredients = idIngredients;
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
    public String getIdunite() {
        return idunite;
    }
    public void setIdunite(String idunite) {
        this.idunite = idunite;
    }
    public String getLibIngredients() {
        return libIngredients;
    }
    public void setLibIngredients(String libIngredients) {
        this.libIngredients = libIngredients;
    }
    public String getIdBcFille() {
        return idBcFille;
    }
    public void setIdBcFille(String idBcFille) {
        this.idBcFille = idBcFille;
    }
    public String getOperateur() {
        return operateur;
    }
    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
    public String getIdFab() {
        return idFab;
    }
    public void setIdFab(String idFab) {
        this.idFab = idFab;
    }
    public double getQte() {
        return qte;
    }
    public void setQte(double qte) {
        this.qte = qte;
    }
    public Date getDatyBesoin() {
        return datyBesoin;
    }
    public void setDatyBesoin(Date datyBesoin) {
        this.datyBesoin = datyBesoin;
    }
    public double getEquivalence() {
        return equivalence;
    }
    public void setEquivalence(double equivalence) {
        this.equivalence = equivalence;
    }
}
