<%@ page import="user.*" %>
<%@ page import="utilitaire.*" %>
<%@ page import="bean.*" %>
<%@ page import="vente.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="affichage.*" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
    try {
        UserEJB u = (UserEJB) session.getAttribute("u");
        String acte = request.getParameter("acte");
        String lien = (String) session.getValue("lien");
        String bute = request.getParameter("bute");
        String classe = request.getParameter("classe");
        String classefille = request.getParameter("classefille");
        String colonneMere = request.getParameter("colonneMere");
        String nombreLigneS = request.getParameter("nombreLigne");
        int nbLine = Utilitaire.stringToInt(nombreLigneS);
        String idmere = request.getParameter("id");

        if (acte != null && acte.compareToIgnoreCase("updateFille") == 0) {
            ClassMAPTable mere = (ClassMAPTable) (Class.forName(classe).newInstance());
            ClassMAPTable fille = (ClassMAPTable) (Class.forName(classefille).newInstance());
            PageUpdateMultiple p = new PageUpdateMultiple(mere, fille, request, nbLine, null);

            // Calcul du montant avant modification
            vente.Vente venteAvant = new vente.Vente();
            venteAvant.setId(idmere);
            double montantAvant = 0;
            try {
                vente.Vente venteComplete = (vente.Vente) venteAvant.getById(idmere, "VENTE_CPL", null);
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
            if (montantApres < montantAvant) {
%>
                <script language="JavaScript">
                    alert("Erreur : Le nouveau montant total (<%=String.format("%.2f", montantApres)%> Ar) ne peut pas être inférieur à l'ancien montant (<%=String.format("%.2f", montantAvant)%> Ar)");
                    history.back();
                </script>
<%
                return;
            }

            // Séparer les nouvelles lignes des lignes existantes
            ArrayList<ClassMAPTable> nouvelles = new ArrayList<ClassMAPTable>();
            ArrayList<ClassMAPTable> existantes = new ArrayList<ClassMAPTable>();

            for (int i = 0; i < cfille.length; i++) {
                cfille[i].setNomTable("VENTE_DETAILS");
                cfille[i].setValChamp(colonneMere, idmere);
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
xClassMAPTable[] nouvellesArray = nouvelles.toArray(new ClassMAPTable[0]);
                u.createObjectFilleMultiple(idmere, colonneMere, nouvellesArray);
            }
%>
            <script language="JavaScript"> document.location.replace("<%=lien%>?but=vente/vente-fiche.jsp&id=<%=idmere%>");</script>
<%
        }
    } catch (Exception ex) {
        ex.printStackTrace();
%>
    <script type="text/javascript">alert("<%=ex.getMessage()%>"); history.back();</script>
<%
    }
%>
</html>