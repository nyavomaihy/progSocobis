package vente;

import java.util.ArrayList;
import java.util.List;


    public class Donnee {
        String date;
        int valeur;

        public Donnee(String date, int valeur) {
            this.date = date;
            this.valeur = valeur;
        }

        @Override
        public String toString() {
            return date + " => " + valeur;
        }
    
public static List<Donnee> separer(String texte) {
    List<Donnee> resultat = new ArrayList<>();

    String[] blocs = texte.split(";");
    for (String bloc : blocs) {
        bloc = bloc.trim();
        String[] parties = bloc.split(":");

        if (parties.length >= 1) {
            String date = parties[0];
            resultat.add(new Donnee(date, 0)); // valeur ignorée
        }
    }

    return resultat;
}


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

}