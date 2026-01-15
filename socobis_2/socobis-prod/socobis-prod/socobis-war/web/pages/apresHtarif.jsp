<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="user.*" %>
<%@ page import="utilitaire.*" %>
<%@ page import="bean.*" %>
<%@ page import="affichage.*" %>
<%@ page import="caisse.MvtCaisse" %>
<%@ page import="vente.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="produits.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
    try {
        // --- Partie paiement caisse ---
        String nomtable = request.getParameter("nomtable");
        String lien = (String) session.getValue("lien");
        UserEJB u = (UserEJB) session.getAttribute("u");
        String acte = request.getParameter("acte");
        String bute = request.getParameter("bute");
        String classe = request.getParameter("classe");
        String idCaisse = "";

        if (acte != null && acte.compareToIgnoreCase("insert") == 0) {
            ClassMAPTable t = (ClassMAPTable) (Class.forName(classe).newInstance());
            PageInsert p = new PageInsert(t, request);
            ClassMAPTable f = p.getObjectAvecValeur();
            f.setNomTable(nomtable);
            ClassMAPTable o = (ClassMAPTable) u.createObject(f);
            if (o != null) {
                idCaisse = o.getTuppleID();
            }
        }

        // --- Partie modification facture (vente) ---
        String vente_acte = request.getParameter("vente_acte");
        String vente_bute = request.getParameter("vente_bute");
        String vente_classe = request.getParameter("vente_classe");
        String vente_classefille = request.getParameter("vente_classefille");
        String vente_colonneMere = request.getParameter("vente_colonneMere");
        String vente_nombreLigneS = request.getParameter("vente_nombreLigne");
        int vente_nbLine = Utilitaire.stringToInt(vente_nombreLigneS);
        String vente_id = request.getParameter("vente_id");

        if (vente_acte != null && vente_acte.compareToIgnoreCase("updateFille") == 0) {
            ClassMAPTable mere = (ClassMAPTable) (Class.forName(vente_classe).newInstance());
            ClassMAPTable fille = (ClassMAPTable) (Class.forName(vente_classefille).newInstance());
            PageUpdateMultiple p = new PageUpdateMultiple(mere, fille, request, vente_nbLine, null);
            
            // Récupérer les idProduit depuis les champs hidden
            ArrayList<String> idProduits = new ArrayList<String>();
            for(int i = 0; i < vente_nbLine; i++) {
                String idProduit = request.getParameter("vente_idProduitHidden_" + i);
                if(idProduit != null && !idProduit.trim().isEmpty()) {
                    idProduits.add(idProduit);
                    System.out.println("idProduit récupéré vente_idProduitHidden_" + i + " = " + idProduit);
                }
            }

            // Vérification si l'ingrédient est changeable - utiliser le premier idProduit
            if(!idProduits.isEmpty()) {
                String premierIdProduit = idProduits.get(0);
                produits.Ingredients ingredients = new produits.Ingredients();
                ingredients.setId(premierIdProduit);
                ingredients = (produits.Ingredients) ingredients.getById(premierIdProduit, "AS_INGREDIENTS", null);

            // Calcul du montant avant modification
            vente.Vente venteAvant = new vente.Vente();
            venteAvant.setId(vente_id);
            double montantAvant = 0;
            try {
                vente.Vente venteComplete = (vente.Vente) venteAvant.getById(vente_id, "VENTE_CPL", null);
                if (venteComplete != null) {
                    montantAvant = venteComplete.getMontantttc();
                }
            } catch (Exception e) {
                montantAvant = 0;
            }

            // Récupérer et traiter uniquement les filles
            ClassMAPTable[] cfille = p.getObjectFilleAvecValeur();

            // Calcul du montant après modification
            double montantApres = 0;
            for (int i = 0; i < cfille.length; i++) {
                try {
                    double pu = 0, qte = 0, remise = 0, tva = 0;
                    if (cfille[i] instanceof vente.VenteDetailsLib) {
                        vente.VenteDetailsLib detail = (vente.VenteDetailsLib) cfille[i];
                        pu = detail.getPu();
                        qte = detail.getQte();
                        remise = detail.getRemise();
                        tva = detail.getTva();
                    } else if (cfille[i] instanceof vente.UpdateVenteDetails) {
                        vente.UpdateVenteDetails detail = (vente.UpdateVenteDetails) cfille[i];
                        pu = detail.getPu();
                        qte = detail.getQte();
                        remise = detail.getRemise();
                        tva = detail.getTva();
                    } else if (cfille[i] instanceof vente.VenteDetails) {
                        vente.VenteDetails detail = (vente.VenteDetails) cfille[i];
                        pu = detail.getPu();
                        qte = detail.getQte();
                        remise = detail.getRemise();
                        tva = detail.getTva();
                    }
                    double montantLigne = pu * qte * (1 - remise/100) * (1 + tva/100);
                    montantApres += montantLigne;
                } catch (Exception e) {
                    // Ignorer cette ligne en cas d'erreur
                }
            }

            // Vérifier que le nouveau montant >= ancien montant
            if(!idProduits.isEmpty()) 
            {
                String premierIdProduit = idProduits.get(0);
                produits.Ingredients ingredients = new produits.Ingredients();
                ingredients.setId(premierIdProduit);
                ingredients = (produits.Ingredients) ingredients.getById(premierIdProduit, "AS_INGREDIENTS", null);
               
                if (ingredients.getEstchangeable() == 0) 
                {
                    %>
                        <script>alert("Erreur : il n'est pas changeable"); history.back();</script>
                    <%
                    return;
                }
                else if(ingredients.getEstchangeable()==1)
                {
                    String awhere = " and IDVENTE = '"+idmere+"'";
                    VenteDetails[] vdetail = (VenteDetails[]) bean.CGenUtil.rechercher(new VenteDetails(), null, null, new UtilDB().GetConn(), awhere);                    // montantAvant=0;
                    double rep=0;
                    for(int i=0; i<vdetail.length ; i++)
                    {
                        for(int j=0 ; j<cfille.length ; j++)
                        {
                            if(cfille[j].getTuppleID()!=null &&
                            vdetail[i].getTuppleID()!=null)
                            {
                                if(cfille[j].getTuppleID().equalsIgnoreCase(vdetail[i].getTuppleID()))
                                {
                                    double mult_tva=(100+vdetail[i].getTva())/100;
                                    double montant_init=vdetail[i].getPu()*vdetail[i].getQte();
                                    double remise_init=(montant_init/100)*vdetail[i].getRemise();
                                    double end=montant_init-remise_init;
                                    rep=rep+(end*mult_tva);
                                }
                            }
                        }
                            //
                            //rep=vdetail[i].getMontantTTC();
                    }
                    montantAvant=rep;
                    if (montantApres < montantAvant) 
                    {
                        %>
                            <script language="JavaScript">
                                alert("Erreur : Le nouveau montant total (<%=String.format("%.2f", montantApres)%> Ar) ne peut pas être inférieur à l'ancien montant (<%=String.format("%.2f", rep)%> Ar)");
                                history.back();
                            </script>
                        <%
                        return;
                    }
                }
            }

            // Séparer les nouvelles lignes des lignes existantes
            ArrayList<ClassMAPTable> nouvelles = new ArrayList<ClassMAPTable>();
            ArrayList<ClassMAPTable> existantes = new ArrayList<ClassMAPTable>();

            for (int i = 0; i < cfille.length; i++) {
                cfille[i].setNomTable("VENTE_DETAILS");
                cfille[i].setValChamp(vente_colonneMere, vente_id);
                cfille[i].setValChamp("idDevise", "AR");
                cfille[i].setValChamp("tauxDeChange", "1");

                if (cfille[i].getTuppleID() != null && !cfille[i].getTuppleID().isEmpty()) {
                    existantes.add(cfille[i]);
                } else {
                    nouvelles.add(cfille[i]);
                }
            }

            // Update des lignes existantes
            for (ClassMAPTable fille_existante : existantes) {
                u.updateObject(fille_existante);
            }

            // Insert des nouvelles lignes sans créer de nouvelle facture
            if (nouvelles.size() > 0) {
                ClassMAPTable[] nouvellesArray = nouvelles.toArray(new ClassMAPTable[0]);
                u.createObjectFilleMultiple(vente_id, vente_colonneMere, nouvellesArray);
            }
        }
%>
        <script language="JavaScript">
            document.location.replace("<%=lien%>?but=caisse/mvt/mvtCaisse-fiche.jsp&id=<%=idCaisse%>");
        </script>
<%
    } catch (Exception e) {
        e.printStackTrace();
%>
    <script language="JavaScript">
        alert("<%=e.getMessage()%>");
        history.back();
    </script>
<%
    }
%>
</html>