/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prevision;

import bean.CGenUtil;
import caisse.EtatCaisse;
import java.sql.Connection;
import java.sql.Date;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Estcepoire
 */
public class AdminPrevision {

    Prevision[] listePrev;
    Prevision minimum;
    double[] somme;

    public Prevision[] getListePrev() {
        return listePrev;
    }

    public void setListePrev(Prevision[] listePrev) {
        this.listePrev = listePrev;
    }

    public Prevision getMinimum() {
        return minimum;
    }

    public void setMinimum(Prevision minimum) {
        this.minimum = minimum;
    }

    public double[] getSomme() {
        return somme;
    }

    public void setSomme(double[] somme) {
        this.somme = somme;
    }

    public double getSoldeInitiale(Connection c, String daty) throws Exception{
        Date d = Utilitaire.string_date("dd/MM/yyyy", daty);
        d=Utilitaire.ajoutJourDate(d, -1);
        String requette = "SELECT SUM(reste) as reste from ( "+ (new EtatCaisse()).generateQueryCore(d,d)+" ) ";
        //System.out.println(requette);
        EtatCaisse[] e = (EtatCaisse[])CGenUtil.rechercher(new EtatCaisse(), requette);
        if (e.length > 0) {
            return e[0].getReste();
        }
        return 0;
    }
    public String getRequete(String datyFiltre, String deb, String fin,String grouper) throws Exception {
        // String requette = " SELECT daty, debit, credit,semaine,mois,annee "
        //             + "   FROM resultatPrevEffectifTous "
        //             + "   WHERE DATY >= TO_DATE('" + deb + "','DD/MM/YYYY') AND DATY < TO_DATE('" + datyFiltre + "','DD/MM/YYYY') "
        //             + "   UNION ALL ( SELECT daty, debit, credit,semaine,mois,annee "
        //             + "                FROM RESULTATPREVISIONNELTOUSMVT "
        //             + "                     WHERE DATY >= TO_DATE('" + datyFiltre + "','DD/MM/YYYY') and DATY <= TO_DATE('" + fin + "','DD/MM/YYYY') ) ";
        String requette = " SELECT daty, debit, credit,semaine,mois,annee,idprevision,iddevise "
                    + "   FROM RESULTATPREVTOUSMVTHAS "
                    + "   WHERE DATY >= TO_DATE('" + deb + "','DD/MM/YYYY') AND DATY < TO_DATE('" + datyFiltre + "','DD/MM/YYYY') "
                    + "   UNION ALL ( SELECT daty, debit, credit,semaine,mois,annee,idprevision,iddevise "
                    + "                FROM RESULTATPREVDETAILHAS "
                    + "                     WHERE DATY >= TO_DATE('" + datyFiltre + "','DD/MM/YYYY') and DATY <= TO_DATE('" + fin + "','DD/MM/YYYY') ) ";
             
        // if(grouper!=null && grouper.compareToIgnoreCase("semaine")==0)
        // {
        //     requette = "select min(daty) as daty, sum(debit) as debit, sum(credit) as credit from ("+requette+") group by semaine,mois,annee" ;   
        // }
        // if(grouper!=null && grouper.compareToIgnoreCase("mois")==0)
        // {
        //     requette = "select min(daty) as daty, sum(debit) as debit, sum(credit) as credit from ("+requette+") group by mois,annee" ;   
        // }
            
        return requette + "    order by DATY ASC ";
    }

    public void getPrevision(String datyFiltre, String deb, String fin,String grouper) throws Exception {
        boolean canClose = false;
        Connection c = null;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                canClose = true;
            }
            String requette=getRequete(datyFiltre,deb,fin,grouper);
            //ato misy select maina
            PrevisionHas[] previsionshas = (PrevisionHas[]) CGenUtil.rechercher(new PrevisionHas(), requette);
            for(int i=0 ; i<previsionshas.length ; i++)
            {
                for(int j=i+1 ; j<previsionshas.length ; j++)
                {
                    if(previsionshas[j].getIdPrevision()!=null && previsionshas[i].getIdPrevision()!=null)
                    {
                        if(previsionshas[j].getIdPrevision().compareToIgnoreCase(previsionshas[i].getIdPrevision())==0)
                        {
                            double p_debit=previsionshas[j].getDebit()-previsionshas[i].getDebit();
                            double p_credit=previsionshas[j].getCredit()-previsionshas[i].getCredit();
                            previsionshas[j].setDebit(p_debit);
                            previsionshas[j].setCredit(p_credit);
                        }
                    }
                }
            }
            List<PrevisionHas> rep=new ArrayList<PrevisionHas>();
            PrevisionHas rephas=previsionshas[0];
            rep.add(rephas);
            for(int i=1 ; i<previsionshas.length ; i++)
            {
                if(previsionshas[i].getDaty().compareTo(previsionshas[i-1].getDaty())==0)
                {
                    rephas.setCredit(rephas.getCredit()+previsionshas[i].getCredit());
                    rephas.setDebit(rephas.getDebit()+previsionshas[i].getDebit());
                }
                else
                {
                    rep.add(rephas);
                    rephas=new PrevisionHas();
                    rephas=previsionshas[i];
                }
            }
            PrevisionHas[] previsionhasfinal = rep.toArray(new PrevisionHas[0]);
            Prevision[] previsions=new Prevision[previsionhasfinal.length];
            for(int i=0 ; i<previsions.length ; i++)
            {
                previsions[i]=previsionhasfinal[i];
            }

            if(grouper!=null && grouper.compareToIgnoreCase("semaine")==0)
            {
                List <PrevisionHas> repSemaine=new ArrayList<PrevisionHas>();
                PrevisionHas rephasS=previsionshas[0];
                repSemaine.add(rephasS);
                for(int i=1 ; i<previsionshas.length ; i++)
                {
                    if(previsionshas[i].getSemaine()==previsionshas[i-1].getSemaine()
                       && previsionshas[i].getMois()==previsionshas[i-1].getMois()
                       && previsionshas[i].getAnnee()==previsionshas[i-1].getAnnee())
                    {
                        rephasS.setCredit(rephasS.getCredit()+previsionshas[i].getCredit());
                        rephasS.setDebit(rephasS.getDebit()+previsionshas[i].getDebit());
                    }
                    else
                    {
                        repSemaine.add(rephasS);
                        rephasS=new PrevisionHas();
                        rephasS=previsionshas[i];
                    }
                }
                PrevisionHas[] previsionhasfinalsemaine = repSemaine.toArray(new PrevisionHas[0]);
                previsions=new Prevision[previsionhasfinalsemaine.length];
                for(int i=0 ; i<previsions.length ; i++)
                {
                    previsions[i]=previsionhasfinalsemaine[i];
                }

            }
            if(grouper!=null && grouper.compareToIgnoreCase("mois")==0)
            {
                List <PrevisionHas> repMois=new ArrayList<PrevisionHas>();
                PrevisionHas rephasM=previsionshas[0];
                repMois.add(rephasM);
                for(int i=1 ; i<previsionshas.length ; i++)
                {
                    if(previsionshas[i].getMois()==previsionshas[i-1].getMois()
                       && previsionshas[i].getAnnee()==previsionshas[i-1].getAnnee())
                    {
                        rephasM.setCredit(rephasM.getCredit()+previsionshas[i].getCredit());
                        rephasM.setDebit(rephasM.getDebit()+previsionshas[i].getDebit());
                    }
                    else
                    {
                        repMois.add(rephasM);
                        rephasM=new PrevisionHas();
                        rephasM=previsionshas[i];
                    }
                }
                PrevisionHas[] previsionhasfinalmois = repMois.toArray(new PrevisionHas[0]);
                previsions=new Prevision[previsionhasfinalmois.length];
                for(int i=0 ; i<previsions.length ; i++)
                {
                    previsions[i]=previsionhasfinalmois[i];
                }
            }
            
            this.setListePrev(previsions);
            this.setMinimum(listePrev[0]);
            listePrev[0].setSoldeInitial(this.getSoldeInitiale(c, deb)); 
            listePrev[0].calculerSoldeFinale();
            for (int i = 1; i < this.getListePrev().length; i++) {
                listePrev[i].setSoldeInitial(listePrev[i-1].getSoldeFinale());
                listePrev[i].calculerSoldeFinale();
                if(minimum.getSoldeFinale()>listePrev[i].getSoldeFinale()) this.setMinimum(listePrev[i]);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (canClose) {
                c.close();
            }
        }
    }
}
