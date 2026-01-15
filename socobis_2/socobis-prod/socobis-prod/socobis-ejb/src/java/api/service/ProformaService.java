// package api.service;

// import java.sql.Connection;
// import java.util.Arrays;
// import java.util.Enumeration;
// import java.util.HashMap;
// import java.util.Map;
// import org.springframework.mock.web.MockHttpServletRequest;
// import javax.ws.rs.Consumes;
// import javax.ws.rs.GET;
// import javax.ws.rs.POST;
// import javax.ws.rs.Path;
// import javax.ws.rs.PathParam;
// import javax.ws.rs.Produces;
// import javax.ws.rs.core.MediaType;
// import javax.ws.rs.core.Response;
// import affichage.PageInsert;
// import affichage.PageInsertMultiple;
// import bean.CGenUtil;
// import bean.ClassMAPTable;
// import bean.TypeObjet;
// import caisse.MvtCaisse;
// import caisse.MvtCaisseCaution;
// import caution.Caution;
// import caution.CautionLib;
// import proforma.Proforma;
// import proforma.ProformaDetails;
// import proforma.ProformaLib;
// import reservation.Check;
// import reservation.CheckInLib;
// import reservation.Reservation;
// import reservation.ReservationDetailsCheck;
// import reservation.ReservationLib;
// import api.dto.*;
// import api.utils.*;
// import user.UserEJB;
// import user.UserEJBClient;
// import utilitaire.UtilDB;
// import utilitaire.Utilitaire;
// import utils.ConstanteLocation;

// import javax.json.Json;
// import javax.json.JsonObject;

// @Path("/services")
// public class ProformaService {

//     @GET
//     @Path("/proformas")
//     @Produces(MediaType.APPLICATION_JSON)
//     public Response listeProforma() throws Exception {
//         ProformaListeDTO [] listes = ProformaTransformer.getProformasAfter2026();
//         return Response.ok(listes).build();
//     }


//     @GET
//     @Path("/payer/{idProforma}")
//     @Produces(MediaType.APPLICATION_JSON)
//     public Response insertMvtCaisseEntree(@PathParam("idProforma") String idProforma) throws Exception {   
//         try {
//             UserEJB u = FonctionUtilitaire.login();

//             MvtCaisse mvtCaisse = new MvtCaisse();
//             ProformaLib pr = new ProformaLib();
//             pr.setId(idProforma);
//             mvtCaisse =  pr.genererMvtCaisseEntree(null);
//             HashMap <String,String> json = mvtCaisse.toHashMap();
//             json.put("nomtable", "MOUVEMENTCAISSE");
//             json.put("classe", "caisse.MvtCaisse");
//             json.put("idDevise","AR");
//             json.put("idCaisse","CAI000284");

//             MockHttpServletRequest req = FonctionUtilitaire.transformeHashMap(json);


//             PageInsert p = new PageInsert((ClassMAPTable) new MvtCaisse(),req);
//             ClassMAPTable f = p.getObjectAvecValeur();
//             f.setNomTable((String)req.getAttribute("nomtable"));
//             u.createObject(f);

//             Map<String, Object> successResponse = new HashMap<>();
//             successResponse.put("success", true);
             
//             return Response.status(Response.Status.CREATED)
//                         .entity(successResponse)
//                         .build();

//         } catch (Exception e) {
//             e.printStackTrace();
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("success", false);
//             errorResponse.put("error", "Erreur création Proforma : " + e.getMessage());

//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                         .entity(errorResponse)
//                         .build();
//         }
//    }


//     @GET
//     @Path("/livrer/{idReservation}")
//     @Produces(MediaType.APPLICATION_JSON)
//     public Response livrer(@PathParam("idReservation") String idReservation) throws Exception {   
//         try {
//             UserEJB u = FonctionUtilitaire.login();

//             ReservationDetailsCheck[] res = null;

//             Reservation mere = new Reservation();
//             mere.setId(idReservation);

//             res = mere.getListeSansCheckIn("RESTSANSCIGROUPLIB_SANS",null);            
        
//             CheckDTO [] array = CheckDTO.convertToCheckDTOArray(res,idReservation);

//             HashMap <String,String> json = CheckDTO.convertToHashMap(array);
//             json.put("indexMultiple", String.valueOf(array.length));
//             json.put("nbrLigne", String.valueOf(array.length));
//             json.put("nombreLigne", String.valueOf(array.length));

//             MockHttpServletRequest request = FonctionUtilitaire.transformeHashMap(json);
            
//             String [] tId =  FonctionUtilitaire.generateIndexArray(array.length);
//             PageInsertMultiple p = new PageInsertMultiple((ClassMAPTable) new Reservation(), (ClassMAPTable) new Check(), request, array.length, tId);
//             ClassMAPTable[] cfille = p.getObjectFilleAvecValeur();
//             for (int i = 0; i < cfille.length; i++) {
//                 cfille[i].setNomTable("CHECKIN");
//             }
//             Reservation reservation = new Reservation();
//             reservation.createObjectFilleMultipleSansMere(u,cfille);
            
//             Map<String, Object> successResponse = new HashMap<>();
//             successResponse.put("success", true);
             
//             return Response.status(Response.Status.CREATED)
//                         .entity(successResponse)
//                         .build();
//         } catch (Exception e) {
//             e.printStackTrace();
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("success", false);
//             errorResponse.put("error", "Erreur livraison : " + e.getMessage());

//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                         .entity(errorResponse)
//                         .build();
//         }
//    }


//    @GET
//    @Path("/reservations")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getReservations() throws Exception {
//        ReservationDTO[] reservations = ReservationTransformer.getReservations();
//        return Response.ok(reservations).build();
//    }

   
//     // @POST
//     // @Path("/proformas")
//     // @Consumes(MediaType.APPLICATION_JSON)
//     // @Produces(MediaType.APPLICATION_JSON)
//     // public Response createProforma(String jsonData) throws Exception {
        
//     //     try {
//     //         UserEJB u = FonctionUtilitaire.login();

//     //         HashMap<String, String> json = JsonParserSimple.jsonToHashMap(jsonData);
//     //         MockHttpServletRequest req = FonctionUtilitaire.transformeHashMap(json);
//     //         HashMap<String, String> mere = JsonParserSimple.filtrer(json, ProformaDTO.class);

//     //         String[] ids = req.getParameterValues("ids");

//     //         for (String i : ids) {
//     //             System.out.println(i);
//     //         }

//     //         int nbLigne = Utilitaire.stringToInt(req.getParameter("nbrLigne"));

//     //         PageInsertMultiple pum = new PageInsertMultiple(
//     //             (ClassMAPTable) new Proforma(), 
//     //             (ClassMAPTable) new ProformaDetails(), 
//     //             req, nbLigne, ids
//     //         );

//     //         ClassMAPTable cmere = pum.getObjectAvecValeur(mere);
//     //         ClassMAPTable[] cfille = pum.getObjectFilleAvecValeur();

//     //         for (ClassMAPTable f : cfille) {
//     //             f.setNomTable("PROFORMADETAILS_CPLIMAGE");
//     //         }

//     //         u.createObjectMultiple(cmere, "idProforma", cfille);
                        
//     //         Map<String, Object> successResponse = new HashMap<>();
//     //         successResponse.put("success", true);

//     //         return Response.status(Response.Status.CREATED)
//     //                     .entity(successResponse)
//     //                     .build();

//     //         } catch (Exception e) {
//     //             e.printStackTrace();
//     //             Map<String, Object> errorResponse = new HashMap<>();
//     //             errorResponse.put("success", false);
//     //             errorResponse.put("error", "Erreur création Proforma : " + e.getMessage());

//     //             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//     //                         .entity(errorResponse)
//     //                         .build();
//     //         }
        
//     // }
// @POST
// @Path("/proformas")
// @Consumes(MediaType.APPLICATION_JSON)
// @Produces(MediaType.APPLICATION_JSON)
// public Response createProforma(String jsonData) throws Exception {
    
//     try {
//         System.out.println("=== DEBUT CREATION PROFORMA ===");
//         System.out.println("JSON reçu: " + jsonData);
        
//         UserEJB u = FonctionUtilitaire.login();
//         System.out.println("Utilisateur connecté: " + (u != null));

//         HashMap<String, String> json = JsonParserSimple.jsonToHashMap(jsonData);
//         System.out.println("HashMap après parsing: " + json);
        
//         MockHttpServletRequest req = FonctionUtilitaire.transformeHashMap(json);
//         System.out.println("Requête Mock créée"+ req.getParameter("nombre_0") + req.getParameter("qte_0"));
        
//         HashMap<String, String> mere = JsonParserSimple.filtrer(json, ProformaDTO.class);
//         System.out.println("Données filtrées (mere): " + mere);

//         String[] ids = req.getParameterValues("ids");
//         System.out.println("IDs reçus: " + Arrays.toString(ids));

//         int nbLigne = Utilitaire.stringToInt(req.getParameter("nbrLigne"));
//         System.out.println("Nombre de lignes: " + nbLigne);

//         // AFFICHEZ TOUS LES PARAMÈTRES POUR DEBUG
//         System.out.println("=== TOUS LES PARAMÈTRES ===");
//         Enumeration<String> paramNames = req.getParameterNames();
//         while (paramNames.hasMoreElements()) {
//             String paramName = paramNames.nextElement();
//             String[] values = req.getParameterValues(paramName);
//             System.out.println(paramName + ": " + Arrays.toString(values));
//         }
//         System.out.println("=== FIN PARAMÈTRES ===");

//         PageInsertMultiple pum = new PageInsertMultiple(
//             (ClassMAPTable) new Proforma(), 
//             (ClassMAPTable) new ProformaDetails(), 
//             req, nbLigne, ids
//         );
//         System.out.println("PageInsertMultiple créée");

//         ClassMAPTable cmere = pum.getObjectAvecValeur(mere);
//         System.out.println("Objet mère créé: " + cmere);
        
//         // AFFICHEZ TOUTES LES VALEURS DE L'OBJET MERE
//         System.out.println("=== VALEURS OBJET MERE ===");
//         //Map<String, Object> valeursMere = cmere.getValeur(); // Méthode dépend de votre implémentation
//         //for (Map.Entry<String, Object> entry : valeursMere.entrySet()) {
//         //    System.out.println(entry.getKey() + ": " + entry.getValue());
//         //}
        
//         ClassMAPTable[] cfille = pum.getObjectFilleAvecValeur();
//         System.out.println("Nombre d'objets fille: " + cfille.length);

//         for (int i = 0; i < cfille.length; i++) {
//             cfille[i].setNomTable("PROFORMADETAILS_CPLIMAGE");
//             System.out.println("Objet fille " + i + ": " + cfille[i]);
//             // AFFICHEZ LES VALEURS DE CHAQUE OBJET FILLE
//           //  Map<String, Object> valeursFille = cfille[i].getValeur();
//           //  for (Map.Entry<String, Object> entry : valeursFille.entrySet()) {
//           //      System.out.println("  " + entry.getKey() + ": " + entry.getValue());
//           //  }
//         }

//         System.out.println("Appel de createObjectMultiple...");
//         u.createObjectMultiple(cmere, "idProforma", cfille);
//         System.out.println("=== PROFORMA CREEE AVEC SUCCES ===");
                    
//         Map<String, Object> successResponse = new HashMap<>();
//         successResponse.put("success", true);

//         return Response.status(Response.Status.CREATED)
//                     .entity(successResponse)
//                     .build();

//     } catch (Exception e) {
//         System.err.println("=== ERREUR DANS CREATE PROFORMA ===");
//         System.err.println("Type d'erreur: " + e.getClass().getName());
//         System.err.println("Message: " + e.getMessage());
//         System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
//         e.printStackTrace();
//         System.err.println("=== FIN ERREUR ===");
        
//         Map<String, Object> errorResponse = new HashMap<>();
//         errorResponse.put("success", false);
//         errorResponse.put("error", "Erreur création Proforma : " + e.getMessage());
//         if (e.getCause() != null) {
//             errorResponse.put("cause", e.getCause().getMessage());
//         }

//         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                     .entity(errorResponse)
//                     .build();
//     }
// }
//     @GET 
//     @Path("/cautions/{idReservation}")
//     @Produces(MediaType.APPLICATION_JSON)
//     public Response retourCaution (@PathParam ("idReservation") String idReservation) throws Exception 
//     {
//         try {
//             UserEJB u = FonctionUtilitaire.login();
//             ReservationLib lib = new ReservationLib();
//             lib.setId(idReservation);
//             CautionLib c = lib.getCautions();

//             CautionLib cau = new CautionLib();
//             cau.setId(c.getId());
//             cau.setIdreservation(idReservation);
//             cau = (CautionLib) new CautionLib().getById(c.getId(), "CAUTIONLIB", null);

//             ReservationLib res = cau.getReservationAvecVerif(null);
//             double montantRetenue = res.getMontantRetenue(cau);
//             double credit = montantRetenue;
//             double debit = cau.getMontantgrp()-montantRetenue;

//             MockHttpServletRequest request = new MockHttpServletRequest();
//             request.setParameter("daty", utilitaire.Utilitaire.dateDuJour());
//             request.setParameter("designation", "Paiement du " + utilitaire.Utilitaire.dateDuJour());
//             request.setParameter("idCaisse", "CAI000284");
//             request.setParameter("debit", String.valueOf(debit));
//             request.setParameter("credit", String.valueOf(credit));
//             request.setParameter("idTiers", res.getIdclient());
//             request.setParameter("idTierslibelle", res.getIdclient());
//             request.setParameter("idOrigine", cau.getId());
//             request.setParameter("taux", "1");
//             request.setParameter("idDevise", "AR");
//             request.setParameter("etat", "0");


//             PageInsert p = new PageInsert(new MvtCaisseCaution(), request);
//             MvtCaisseCaution f = (MvtCaisseCaution) p.getObjectAvecValeur();
//             f.setNomTable("MOUVEMENTCAISSE");

//             Connection con = null;

//             if(con ==null)
//             {
//                 con=new UtilDB().GetConn();
//             }


//             Caution caution = new Caution();
//             caution.setId(f.getIdOrigine());
//             CautionLib cl =  (CautionLib) new CautionLib().getById(f.getIdOrigine(), "CautionLib", null);
            
        
//             MvtCaisseCaution remb = f.clone();
//             remb.setDesignation("Paiement remboursement du caution "+f.getIdOrigine());
//             remb.setType_mvt(ConstanteLocation.type_remboursement);
//             remb.setDebit(cl.getMontantgrp());
//             remb.setCredit(0);
//             remb.setIdOrigine(cl.getId());
//             remb.createObject(u.getUser().getTuppleID(),con).getTuppleID();

//             MvtCaisseCaution retenue = f.clone();
//             retenue.setDesignation("Paiement retenue du caution "+f.getIdOrigine());
//             retenue.setType_mvt(ConstanteLocation.type_retenue);
//             retenue.setIdOrigine(cl.getId());

//             ReservationLib res1 = cl.getReservationAvecVerif(null);
//             double montantRetenue1 = res1.getMontantRetenue(cl);
//             double credit1 = montantRetenue1;
            
//             retenue.setDebit(0);
//             if(credit1>0){
//                 retenue.setCredit(credit1);
//                 retenue.createObject(u.getUser().getTuppleID(),con).getTuppleID();
//             }
            
//                 Map<String, Object> successResponse = new HashMap<>();
//                 successResponse.put("success", true);

//         return Response.status(Response.Status.CREATED)
//                     .entity(successResponse)
//                     .build();

//         } catch (Exception e) {
//             e.printStackTrace();
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("success", false);
//             errorResponse.put("error", "Erreur création Proforma : " + e.getMessage());

//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                         .entity(errorResponse)
//                         .build();
//         }
//     }



// }
