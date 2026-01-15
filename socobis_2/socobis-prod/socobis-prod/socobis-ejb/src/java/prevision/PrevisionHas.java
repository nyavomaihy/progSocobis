package prevision;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.AdminGen;
import caisse.Caisse;
import java.sql.Connection;

import caisse.MvtCaisse;
import caisse.ReportCaisse;
import com.mongodb.util.Util;
import faturefournisseur.FactureFournisseur;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import produits.Recette;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import vente.FactureCF;
import vente.Vente;
import vente.VenteLib;

public class PrevisionHas extends Prevision 
{
    private int mois,annee,semaine;
    
    public PrevisionHas()
    {
        this.setNomTable("RESULTATPREVDETAILHAS");

    }
    public void setMois(int m)
    {
        this.mois=m;
    }
    public void setAnnee(int a)
    {
        this.annee=a;
    }
    public void setSemaine(int s)
    {
        this.semaine=s;        
    }
    public int getMois()
    {
        return this.mois;
    }
    public int getAnnee()
    {
        return this.annee;
    }
    public int getSemaine()
    {
        return this.semaine;
    }

}
