package api.service;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.mock.web.MockHttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import affichage.PageInsert;
import affichage.PageInsertMultiple;
import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.TypeObjet;
import caisse.MvtCaisse;
import fabrication.Fabrication;
import fabrication.FabricationFille;
import fabrication.Of;
import fabrication.OfFille;
import proforma.Proforma;
import proforma.ProformaDetails;
import api.dto.*;
import api.utils.*;
import user.UserEJB;
import user.UserEJBClient;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
//import utils.ConstanteLocation;

import javax.json.Json;
import javax.json.JsonObject;

@Path("/services")
public class OfService {
    
@POST
@Path("/of")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response createProforma(String jsonData) throws Exception {
    
    try {
        System.out.println("=== DEBUT CREATION PROFORMA ===");
        System.out.println("JSON reçu: " + jsonData);
        
        UserEJB u = FonctionUtilitaire.login();
        System.out.println("Utilisateur connecté: " + (u != null));

        HashMap<String, String> json = JsonParserSimple.jsonToHashMap(jsonData);
        System.out.println("HashMap après parsing: " + json);
        
        MockHttpServletRequest req = FonctionUtilitaire.transformeHashMap(json);
        System.out.println("Requête Mock créée"+ req.getParameter("nombre_0") + req.getParameter("qte_0"));
        
        HashMap<String, String> mere = JsonParserSimple.filtrer(json, OrdreFabricationDTO.class);
        System.out.println("Données filtrées (mere): " + mere);

        String[] ids = req.getParameterValues("ids");
        System.out.println("IDs reçus: " + Arrays.toString(ids));

        int nbLigne = Utilitaire.stringToInt(req.getParameter("nbrLigne"));
        System.out.println("Nombre de lignes: " + nbLigne);

        // AFFICHEZ TOUS LES PARAMÈTRES POUR DEBUG
        System.out.println("=== TOUS LES PARAMÈTRES ===");
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] values = req.getParameterValues(paramName);
            System.out.println(paramName + ": " + Arrays.toString(values));
        }
        System.out.println("=== FIN PARAMÈTRES ===");

        PageInsertMultiple pum = new PageInsertMultiple(
            (ClassMAPTable) new Of(), 
            (ClassMAPTable) new OfFille(), 
            req, nbLigne, ids
        );
        System.out.println("PageInsertMultiple créée");

        ClassMAPTable cmere = pum.getObjectAvecValeur(mere);
        System.out.println("Objet mère créé: " + cmere);
        
        // AFFICHEZ TOUTES LES VALEURS DE L'OBJET MERE
        System.out.println("=== VALEURS OBJET MERE ===");
        //Map<String, Object> valeursMere = cmere.getValeur(); // Méthode dépend de votre implémentation
        //for (Map.Entry<String, Object> entry : valeursMere.entrySet()) {
        //    System.out.println(entry.getKey() + ": " + entry.getValue());
        //}
        
        ClassMAPTable[] cfille = pum.getObjectFilleAvecValeur();
        System.out.println("Nombre d'objets fille: " + cfille.length);

        for (int i = 0; i < cfille.length; i++) {
            cfille[i].setNomTable("OFFILLE");
            System.out.println("Objet fille " + i + ": " + cfille[i]);
            // AFFICHEZ LES VALEURS DE CHAQUE OBJET FILLE
          //  Map<String, Object> valeursFille = cfille[i].getValeur();
          //  for (Map.Entry<String, Object> entry : valeursFille.entrySet()) {
          //      System.out.println("  " + entry.getKey() + ": " + entry.getValue());
          //  }
        }

        System.out.println("Appel de createObjectMultiple...");
        u.createObjectMultiple(cmere, "idProforma", cfille);
        System.out.println("=== Ordre de fabrication CREEE AVEC SUCCES ===");
                    
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("success", true);

        return Response.status(Response.Status.CREATED)
                    .entity(successResponse)
                    .build();

    } catch (Exception e) {
        System.err.println("=== ERREUR DANS CREATE PROFORMA ===");
        System.err.println("Type d'erreur: " + e.getClass().getName());
        System.err.println("Message: " + e.getMessage());
        System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
        e.printStackTrace();
        System.err.println("=== FIN ERREUR ===");
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", "Erreur création Proforma : " + e.getMessage());
        if (e.getCause() != null) {
            errorResponse.put("cause", e.getCause().getMessage());
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
    }
}

@POST
@Path("/of")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response createFabricaiton(String jsonData) throws Exception {
    
    try {
        System.out.println("=== DEBUT CREATION PROFORMA ===");
        System.out.println("JSON reçu: " + jsonData);
        
        UserEJB u = FonctionUtilitaire.login();
        System.out.println("Utilisateur connecté: " + (u != null));

        HashMap<String, String> json = JsonParserSimple.jsonToHashMap(jsonData);
        System.out.println("HashMap après parsing: " + json);
        
        MockHttpServletRequest req = FonctionUtilitaire.transformeHashMap(json);
        System.out.println("Requête Mock créée"+ req.getParameter("nombre_0") + req.getParameter("qte_0"));
        
        HashMap<String, String> mere = JsonParserSimple.filtrer(json, OrdreFabricationDTO.class);
        System.out.println("Données filtrées (mere): " + mere);

        String[] ids = req.getParameterValues("ids");
        System.out.println("IDs reçus: " + Arrays.toString(ids));

        int nbLigne = Utilitaire.stringToInt(req.getParameter("nbrLigne"));
        System.out.println("Nombre de lignes: " + nbLigne);

        // AFFICHEZ TOUS LES PARAMÈTRES POUR DEBUG
        System.out.println("=== TOUS LES PARAMÈTRES ===");
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] values = req.getParameterValues(paramName);
            System.out.println(paramName + ": " + Arrays.toString(values));
        }
        System.out.println("=== FIN PARAMÈTRES ===");

        PageInsertMultiple pum = new PageInsertMultiple(
            (ClassMAPTable) new Fabrication(), 
            (ClassMAPTable) new FabricationFille(), 
            req, nbLigne, ids
        );
        System.out.println("PageInsertMultiple créée");

        ClassMAPTable cmere = pum.getObjectAvecValeur(mere);
        System.out.println("Objet mère créé: " + cmere);
        
        // AFFICHEZ TOUTES LES VALEURS DE L'OBJET MERE
        System.out.println("=== VALEURS OBJET MERE ===");
        //Map<String, Object> valeursMere = cmere.getValeur(); // Méthode dépend de votre implémentation
        //for (Map.Entry<String, Object> entry : valeursMere.entrySet()) {
        //    System.out.println(entry.getKey() + ": " + entry.getValue());
        //}
        
        ClassMAPTable[] cfille = pum.getObjectFilleAvecValeur();
        System.out.println("Nombre d'objets fille: " + cfille.length);

        for (int i = 0; i < cfille.length; i++) {
            cfille[i].setNomTable("OFFILLE");
            System.out.println("Objet fille " + i + ": " + cfille[i]);
            // AFFICHEZ LES VALEURS DE CHAQUE OBJET FILLE
          //  Map<String, Object> valeursFille = cfille[i].getValeur();
          //  for (Map.Entry<String, Object> entry : valeursFille.entrySet()) {
          //      System.out.println("  " + entry.getKey() + ": " + entry.getValue());
          //  }
        }

        System.out.println("Appel de createObjectMultiple...");
        u.createObjectMultiple(cmere, "idProforma", cfille);
        System.out.println("=== Ordre de fabrication CREEE AVEC SUCCES ===");
                    
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("success", true);

        return Response.status(Response.Status.CREATED)
                    .entity(successResponse)
                    .build();

    } catch (Exception e) {
        System.err.println("=== ERREUR DANS CREATE PROFORMA ===");
        System.err.println("Type d'erreur: " + e.getClass().getName());
        System.err.println("Message: " + e.getMessage());
        System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
        e.printStackTrace();
        System.err.println("=== FIN ERREUR ===");
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", "Erreur création Proforma : " + e.getMessage());
        if (e.getCause() != null) {
            errorResponse.put("cause", e.getCause().getMessage());
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
    }
}

}
