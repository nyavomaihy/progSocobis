<%-- 
    Document   : vente-saisie
    Created on : 22 mars 2024, 14:37:44
    Author     : Angela
--%>


<%@page import="caisse.Caisse"%>
<%@page import="vente.InsertionVente"%>
<%@page import="vente.VenteDetails"%>
<%@page import="bean.TypeObjet"%>
<%@page import="user.*"%> 
<%@ page import="bean.*" %>
<%@page import="affichage.*"%>
<%@page import="utilitaire.*"%>
<%@page import="vente.*"%>
<%
    try {
        UserEJB u = null;
        u = (UserEJB) session.getValue("u");
        UpdateVente mere = new UpdateVente();
        VenteDetails fille = new VenteDetails();
        fille.setNomTable("VENTE_DETAILS_VIDE");
        int nombreLigne = 10;
        VenteDetailsLib filleLib = new VenteDetailsLib();
        filleLib.setNomTable("VENTE_DETAILS_VIDE");
        VenteDetailsLib[] filles = (VenteDetailsLib[])CGenUtil.rechercher(filleLib,null,null, " and idVente='"+request.getParameter("id")+"'");
        PageUpdateMultiple pi = new PageUpdateMultiple(mere, fille, filles, request, u,filles.length);
        pi.setLien((String) session.getValue("lien"));
        Liste[] liste = new Liste[3];
        liste[0] = new Liste("idDevise",new caisse.Devise(),"val","id");
        liste[1] = new Liste("idMagasin",new magasin.Magasin(),"val","id");
        liste[2] = new Liste("estPrevu");
        liste[2].makeListeOuiNon();
        pi.getFormu().changerEnChamp(liste);
        
        affichage.Champ.setPageAppelCompleteAWhere(pi.getFormufle().getChampFille("idProduit"),"produits.IngredientVente","id","AS_INGREDIENT_VENTE_LIB","prixunitaire;compte_vente;libelle;idunite","pu;compte;designation;unite","");
        pi.getFormufle().getChamp("idProduit_0").setPageAppel("Produit");
        pi.getFormufle().getChamp("tva_0").setLibelle("TVA (En %)");
        pi.getFormufle().getChamp("remise_0").setLibelle("Remise (En %)");
        pi.getFormufle().getChamp("idOrigine_0").setLibelle("Origine");
        pi.getFormufle().getChamp("qte_0").setLibelle("Quantit&eacute;");
        pi.getFormufle().getChamp("pu_0").setLibelle("PU Brut");
        pi.getFormufle().getChamp("idDevise_0").setLibelle("Devise");
        pi.getFormufle().getChamp("designation_0").setLibelle("D&eacute;signation");
        affichage.Champ.setPageAppelComplete(pi.getFormufle().getChampFille("compte"),"mg.cnaps.compta.ComptaCompte","compte","compta_compte","","");
        pi.getFormufle().getChamp("compte_0").setLibelle("Compte");
        pi.getFormufle().getChamp("tauxDeChange_0").setLibelle("Taux de change");
        pi.getFormufle().getChamp("idProduit_0").setLibelle("Produit");
        pi.getFormufle().getChampMulitple("idVente").setVisible(false);
        pi.getFormufle().getChampMulitple("id").setVisible(false);
        pi.getFormufle().getChampMulitple("IdOrigine").setVisible(false);
        pi.getFormufle().getChampMulitple("puAchat").setVisible(false);
        pi.getFormufle().getChampMulitple("puVente").setVisible(false);
        pi.getFormufle().getChampMulitple("unite").setVisible(false);
        pi.getFormufle().getChampMulitple("compte").setVisible(false);
        
        pi.preparerDataFormu();
        for(int i=0;i<pi.getNombreLigne();i++){
            pi.getFormufle().getChamp("qte_"+i).setAutre("onChange='calculerMontant("+i+")'");
            pi.getFormufle().getChamp("remise_"+i).setAutre("onChange='calculerMontant("+i+")'");
            pi.getFormufle().getChamp("tva_"+i).setAutre("onChange='calculerMontant("+i+")'");
            pi.getFormufle().getChamp("idProduit_"+i).setAutre("onChange='mettreAJourIdProduitHidden("+i+")'");
            pi.getFormufle().getChamp("qte_"+i).setDefaut("0");
            pi.getFormufle().getChamp("idDevise_"+i).setDefaut("AR");
            pi.getFormufle().getChamp("idDevise_"+i).setAutre("readonly");
            pi.getFormufle().getChamp("tauxDeChange_"+i).setAutre("readonly");
            pi.getFormufle().getChamp("compte_"+i).setAutre("readonly");
            pi.getFormufle().getChamp("pu_"+i).setAutre("readonly");
        }
        //Variables de navigation
        String classeMere = "vente.UpdateVente";
        String classeFille = "vente.VenteDetails";
        String butApresPost = "vente/vente-fiche.jsp";
        String colonneMere = "idVente";
        //Preparer les affichages
        String[] ordreFille ={"idProduit","designation","pu","qte","remise","tva","idDevise","tauxDeChange"};
        pi.getFormufle().setColOrdre(ordreFille);
        pi.getFormu().makeHtmlInsertTabIndex();
        pi.getFormufle().makeHtmlInsertTableauIndex();

%>
<div class="content-wrapper">
   <div class="row">
        <div class="col-md-12">
            <div class="box-fiche">
                <div class="box">
                    <div class="box-title with-border">
                        <h1>Modification de la facture client</h1>
                    </div>
                    <div class="box-body">
                        <form class='container' action="<%=pi.getLien()%>?but=apresH.jsp&id=<%=request.getParameter("id")%>" method="post" >
                            <input name="id" type="hidden" value="<%=request.getParameter("id")%>">
                            
                            <h3>Total &agrave; payer : <span id="montanttotal">0</span>Ar</h3>
                            <%
                                // Ajouter les champs hidden pour idProduit avant le tableau
                                for(int i=0; i<pi.getNombreLigne(); i++){
                                    out.println("<input type='hidden' name='idProduitHidden_" + i + "' id='idProduitHidden_" + i + "' value=''>");
                                }
                                
                                out.println(pi.getFormufle().getHtmlTableauInsert());
                            %>

                            <input name="acte" type="hidden" id="nature" value="updateFille">
                            <input name="bute" type="hidden" id="bute" value="<%= butApresPost %>">
                            <input name="classe" type="hidden" id="classe" value="<%= classeMere %>">
                            <input name="classefille" type="hidden" id="classefille" value="<%= classeFille %>">
                            <input name="nombreLigne" type="hidden" id="nombreLigne" value="<%= pi.getNombreLigne() %>">
                            <input name="colonneMere" type="hidden" id="colonneMere" value="<%= colonneMere %>">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page='taux.jsp'/>
<script>
    // Fonction pour mettre à jour le champ hidden idProduit
    function mettreAJourIdProduitHidden(indice) {
        var idProduit = document.getElementById('idProduit_' + indice).value;
        document.getElementById('idProduitHidden_' + indice).value = idProduit;
        console.log('idProduitHidden_' + indice + ' mis à jour avec: ' + idProduit);
    }

    document.addEventListener('DOMContentLoaded', function() {
        var val = 0;
        
        // Initialiser les champs hidden idProduit au chargement
        $('select[id^="idProduit_"]').each(function() {
            var indice = $(this).attr('id').replace('idProduit_', '');
            mettreAJourIdProduitHidden(indice);
        });
        
        $('input[id^="qte_"]').each(function() {
            var indice = $(this).attr('id').replace('qte_', '');
            calculerMontant(indice);
        });
    });

    function formatNumber(number) {
        return number.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, " ");
    }

    function calculerMontant(indice) {
        // Mettre à jour le champ hidden idProduit avant le calcul
        mettreAJourIdProduitHidden(indice);
        
        // Récupérer les valeurs des champs
        var pu = parseFloat(document.getElementById('pu_' + indice).value.replace(/\s/g, '')) || 0;
        var qte = parseFloat(document.getElementById('qte_' + indice).value.replace(/\s/g, '')) || 0;
        var remise = parseFloat(document.getElementById('remise_' + indice).value.replace(/\s/g, '')) || 0;
        var tva = parseFloat(document.getElementById('tva_' + indice).value.replace(/\s/g, '')) || 0;

        // Calculer le montant total de cette ligne
        var montantLigne = pu * qte * (1 - remise / 100) * (1 + tva / 100);

        // Formatter le prix unitaire
        document.getElementById('pu_' + indice).value = formatNumber(pu);

        // Calculer et afficher le total général
        var val = 0;
        $('input[id^="qte_"]').each(function() {
            var idx = $(this).attr('id').replace('qte_', '');
            var puVal = parseFloat(document.getElementById('pu_' + idx).value.replace(/\s/g, '')) || 0;
            var qteVal = parseFloat($(this).val().replace(/\s/g, '')) || 0;
            var remiseVal = parseFloat(document.getElementById('remise_' + idx).value.replace(/\s/g, '')) || 0;
            var tvaVal = parseFloat(document.getElementById('tva_' + idx).value.replace(/\s/g, '')) || 0;
            
            if(!isNaN(puVal) && !isNaN(qteVal)){
                var montant = puVal * qteVal * (1 - remiseVal / 100) * (1 + tvaVal / 100);
                val += montant;
            }
        });
        
        $("#montanttotal").html(Intl.NumberFormat('fr-FR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(val));
    }
</script>
<%
	} catch (Exception e) {
		e.printStackTrace();
%>
    <script language="JavaScript">
        alert('<%=e.getMessage()%>');
        history.back();
    </script>
<% }%>