<%-- commun --%>
<%@page import="user.*"%>
<%@page import="bean.TypeObjet"%>
<%@page import="affichage.*"%>
<%@page import="caisse.Caisse"%>

<%-- update vente --%>
<%@page import="utils.ConstanteStation"%>
<%@page import="caisse.MvtCaisse"%>
<%@ page import="ristourne.Ristourne" %>
<%@ page import="vente.Vente" %>
<%@ page import="utilitaire.Utilitaire" %>
<%@ page import="vente.VenteLib" %>

<%-- insert paiement --%>
<%@page import="vente.InsertionVente"%>
<%@page import="vente.VenteDetails"%>
<%@ page import="bean.*" %>
<%@page import="utilitaire.*"%>
<%@page import="vente.*"%>

<%
    try {
        String lien = (String) session.getValue("lien");
        // Suppose que le calcul a déjà été fait et affiché
        // Partie paiement caisse
        UserEJB user = (UserEJB) session.getValue("u");
        MvtCaisse mouvement = new MvtCaisse();
        PageInsert pageInsert = new PageInsert(mouvement, request, user);
        pageInsert.setLien(lien);
        String montant = request.getParameter("montant_paye");
        String idOrigine = request.getParameter("idOrigine");
        String devise = request.getParameter("devise");
        String tiers = request.getParameter("tiers");
        String tau = request.getParameter("taux");
        String id = "";
        VenteLib v = null;
        if(request.getParameter("id")!=null && request.getParameter("id")!="" && request.getParameter("id").startsWith("RIS")){
            id = request.getParameter("id");
            Ristourne r = new Ristourne();
            r.setId(id);
            v = r.getVente(null);
        }

        affichage.Champ[] liste = new affichage.Champ[1];
        liste[0] = new Liste("idModePaiement",new TypeObjet("MODEPAIEMENT"),"val","id");
        pageInsert.getFormu().changerEnChamp(liste);
        pageInsert.getFormu().getChamp("designation").setDefaut("Paiement de la facture : "+idOrigine);
        pageInsert.getFormu().getChamp("idCaisse").setVisible(false);
        pageInsert.getFormu().getChamp("idDevise").setLibelle("Devise");
        pageInsert.getFormu().getChamp("daty").setLibelle("Date");
        pageInsert.getFormu().getChamp("idDevise").setDefaut(devise);
        pageInsert.getFormu().getChamp("idDevise").setAutre("readonly");
        pageInsert.getFormu().getChamp("taux").setDefaut(tau);
        pageInsert.getFormu().getChamp("idVirement").setVisible(false);
        pageInsert.getFormu().getChamp("idVenteDetail").setVisible(false);
        pageInsert.getFormu().getChamp("idOp").setVisible(false);
        pageInsert.getFormu().getChamp("idOrigine").setVisible(false);
        // La valeur par défaut de credit sera mise à jour dynamiquement en JS
        pageInsert.getFormu().getChamp("credit").setDefaut("");
        pageInsert.getFormu().getChamp("credit").setLibelle("Cr&eacute;dit");
        pageInsert.getFormu().getChamp("designation").setLibelle("d&eacute;signation");
        pageInsert.getFormu().getChamp("idtraite").setVisible(false);
        pageInsert.getFormu().getChamp("etatversement").setVisible(false);
        pageInsert.getFormu().getChamp("etat").setVisible(false);
        pageInsert.getFormu().getChamp("idOrigine").setDefaut(idOrigine);
        pageInsert.getFormu().getChamp("idOrigine").setVisible(false);
        pageInsert.getFormu().getChamp("debit").setVisible(false);
        pageInsert.getFormu().getChamp("idTiers").setDefaut(tiers);
        pageInsert.getFormu().getChamp("idTiers").setVisible(false);
        pageInsert.getFormu().getChamp("idPrevision").setLibelle("Pr&eacute;vision");
        pageInsert.getFormu().getChamp("idModePaiement").setLibelle("Mode de paiement");
        pageInsert.getFormu().getChamp("idPrevision").setPageAppelComplete("prevision.Prevision", "id", "PREVISION");
        pageInsert.getFormu().getChamp("compte").setLibelle("Compte de regroupement");
        if(v!=null)
        {
            pageInsert.getFormu().getChamp("designation").setDefaut("Paiement du ristourne "+id);
            pageInsert.getFormu().getChamp("idTiers").setDefaut(v.getTiers());
            pageInsert.getFormu().getChamp("credit").setDefaut(v.getMontantttc()+"");
            pageInsert.getFormu().getChamp("idOrigine").setDefaut(v.getId());
            pageInsert.getFormu().getChamp("idDevise").setDefaut("AR");
            pageInsert.getFormu().getChamp("taux").setDefaut(v.getTauxdechange()+"");
        }
        String classe = "caisse.MvtCaisse";
        String nomTable = "MOUVEMENTCAISSE";
        String butApresPost = "caisse/mvt/mvtCaisse-fiche.jsp";
        String[] order_form = {"daty","designation","idModePaiement","credit","idDevise","taux","compte","idPrevision","idVirement","idVenteDetail","idOp","idOrigine","debit","idTiers","etat"};
        pageInsert.getFormu().setOrdre(order_form);
        pageInsert.preparerDataFormu();
        pageInsert.getFormu().makeHtmlInsertTabIndex();

        // Partie modification facture (vente) avec variables préfixées
        UserEJB vente_u = user;
        UpdateVente vente_mere = new UpdateVente();
        VenteDetails vente_fille = new VenteDetails();
        vente_fille.setNomTable("VENTE_DETAILS_VIDE");
        int vente_nombreLigne = 10;
        VenteDetailsLib vente_filleLib = new VenteDetailsLib();
        vente_filleLib.setNomTable("VENTE_DETAILS_VIDE");
        VenteDetailsLib[] vente_filles = (VenteDetailsLib[])CGenUtil.rechercher(vente_filleLib,null,null, " and idVente='"+request.getParameter("id")+"'");
        PageUpdateMultiple vente_pi = new PageUpdateMultiple(vente_mere, vente_fille, vente_filles, request, vente_u, vente_filles.length);
        vente_pi.setLien(lien);
        Liste[] vente_liste = new Liste[3];
        vente_liste[0] = new Liste("idDevise",new caisse.Devise(),"val","id");
        vente_liste[1] = new Liste("idMagasin",new magasin.Magasin(),"val","id");
        vente_liste[2] = new Liste("estPrevu");
        vente_liste[2].makeListeOuiNon();
        vente_pi.getFormu().changerEnChamp(vente_liste);
        affichage.Champ.setPageAppelCompleteAWhere(vente_pi.getFormufle().getChampFille("idProduit"),"produits.IngredientVente","id","AS_INGREDIENT_VENTE_LIB","prixunitaire;compte_vente;libelle;idunite","pu;compte;designation;unite","");
        vente_pi.getFormufle().getChamp("idProduit_0").setPageAppel("Produit");
        vente_pi.getFormufle().getChamp("tva_0").setLibelle("TVA (En %)");
        vente_pi.getFormufle().getChamp("remise_0").setLibelle("Remise (En %)");
        vente_pi.getFormufle().getChamp("idOrigine_0").setLibelle("Origine");
        vente_pi.getFormufle().getChamp("qte_0").setLibelle("Quantit&eacute;");
        vente_pi.getFormufle().getChamp("pu_0").setLibelle("PU Brut");
        vente_pi.getFormufle().getChamp("idDevise_0").setLibelle("Devise");
        vente_pi.getFormufle().getChamp("designation_0").setLibelle("D&eacute;signation");
        affichage.Champ.setPageAppelComplete(vente_pi.getFormufle().getChampFille("compte"),"mg.cnaps.compta.ComptaCompte","compte","compta_compte","","");
        vente_pi.getFormufle().getChamp("compte_0").setLibelle("Compte");
        vente_pi.getFormufle().getChamp("tauxDeChange_0").setLibelle("Taux de change");
        vente_pi.getFormufle().getChamp("idProduit_0").setLibelle("Produit");
        vente_pi.getFormufle().getChampMulitple("idVente").setVisible(false);
        vente_pi.getFormufle().getChampMulitple("id").setVisible(false);
        vente_pi.getFormufle().getChampMulitple("IdOrigine").setVisible(false);
        vente_pi.getFormufle().getChampMulitple("puAchat").setVisible(false);
        vente_pi.getFormufle().getChampMulitple("puVente").setVisible(false);
        vente_pi.getFormufle().getChampMulitple("unite").setVisible(false);
        vente_pi.getFormufle().getChampMulitple("compte").setVisible(false);
        vente_pi.preparerDataFormu();
        for(int i=0;i<vente_pi.getNombreLigne();i++){
            vente_pi.getFormufle().getChamp("qte_"+i).setAutre("onChange='calculerMontant("+i+")'");
            vente_pi.getFormufle().getChamp("remise_"+i).setAutre("onChange='calculerMontant("+i+")'");
            vente_pi.getFormufle().getChamp("tva_"+i).setAutre("onChange='calculerMontant("+i+")'");
            vente_pi.getFormufle().getChamp("qte_"+i).setDefaut("0");
            vente_pi.getFormufle().getChamp("idDevise_"+i).setDefaut("AR");
            vente_pi.getFormufle().getChamp("idDevise_"+i).setAutre("readonly");
            vente_pi.getFormufle().getChamp("tauxDeChange_"+i).setAutre("readonly");
            vente_pi.getFormufle().getChamp("compte_"+i).setAutre("readonly");
            vente_pi.getFormufle().getChamp("pu_"+i).setAutre("readonly");
        }
        String vente_classeMere = "vente.UpdateVente";
        String vente_classeFille = "vente.VenteDetails";
        String vente_butApresPost = "vente/vente-fiche.jsp";
        String vente_colonneMere = "idVente";
        String[] vente_ordreFille ={"idProduit","designation","pu","qte","remise","tva","idDevise","tauxDeChange"};
        vente_pi.getFormufle().setColOrdre(vente_ordreFille);
        vente_pi.getFormu().makeHtmlInsertTabIndex();
        vente_pi.getFormufle().makeHtmlInsertTableauIndex();
%>

<div class="content-wrapper">
    <h1 align="center">Paiement et modification facture</h1>
    <form action="apresHtarif.jsp" method="post" data-parsley-validate>
        <%-- Partie paiement caisse --%>

        <h2>Paiement</h2>
        <%
            out.println(pageInsert.getFormu().getHtmlInsert());
        %>
        <input name="acte" type="hidden" value="insert">
        <input name="bute" type="hidden" value="<%= butApresPost %>">
        <input name="classe" type="hidden" value="<%= classe %>">
        <input name="nomtable" type="hidden" value="<%= nomTable %>">

        <%-- Partie modification facture (vente) --%>
        <h2>Modification de la facture client</h2>
        <input name="vente_id" type="hidden" value="<%=request.getParameter("id")%>">
        <h3>Total &agrave; payer : <span id="montanttotal">0</span>Ar</h3>
        <h4>Reste &agrave; payer : <span id="restepayer">0</span>Ar</h4>
        <%
            out.println(vente_pi.getFormufle().getHtmlTableauInsert());
        %>
        <input name="vente_acte" type="hidden" value="updateFille">
        <input name="vente_bute" type="hidden" value="<%= vente_butApresPost %>">
        <input name="vente_classe" type="hidden" value="<%= vente_classeMere %>">
        <input name="vente_classefille" type="hidden" value="<%= vente_classeFille %>">
        <input name="vente_nombreLigne" type="hidden" value="<%= vente_pi.getNombreLigne() %>">
        <input name="vente_colonneMere" type="hidden" value="<%= vente_colonneMere %>">
    </form>
</div>
<jsp:include page='taux.jsp'/>
<script>
    // Récupère le montant payé côté serveur
    var montantPaye = parseFloat('<%= montant != null ? montant.replace(",", ".") : "0" %>') || 0;

    document.addEventListener('DOMContentLoaded', function() {
        var val = 0;
        $('input[id^="qte_"]').each(function() {
            var indice = $(this).attr('id').replace('qte_', '');
            calculerMontant(indice);
        });
    });

    function formatNumber(number) {
        return number.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, " ");
    }

    function calculerMontant(indice) {
        var pu = parseFloat(document.getElementById('pu_' + indice).value.replace(/\s/g, '')) || 0;
        var qte = parseFloat(document.getElementById('qte_' + indice).value.replace(/\s/g, '')) || 0;
        var remise = parseFloat(document.getElementById('remise_' + indice).value.replace(/\s/g, '')) || 0;
        var tva = parseFloat(document.getElementById('tva_' + indice).value.replace(/\s/g, '')) || 0;
        var montantLigne = pu * qte * (1 - remise / 100) * (1 + tva / 100);
        document.getElementById('pu_' + indice).value = formatNumber(pu);
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

        // Calcul du reste à payer
        var reste = val - montantPaye;
        $("#restepayer").html(Intl.NumberFormat('fr-FR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(reste));

        // Mettre à jour le champ credit avec le reste à payer
        var creditInput = document.querySelector('input[name="credit"]');
        if (creditInput) {
            creditInput.value = reste.toFixed(2);
        }
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