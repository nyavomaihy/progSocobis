package api.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bean.*;
import fabrication.Of;
import fabrication.OfFille;
import magasin.Magasin;
import produits.IngredientsLib;

@Path("/content")
public class ContentRessource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello depuis WildFly + Ant !";
    }

    @GET
    @Path("/magasin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIngredients() {
        try {
            Magasin magasin = new Magasin();
            Magasin[] magasins = (Magasin[]) CGenUtil.rechercher(magasin,
                    null, null, "");

            return Response.ok(magasins).build();
        } catch (Exception e) {
            return null;
        }
    }

    // @GET
    // @Path("/ofFille")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getIngredients() {
    // try {
    // String query = "SELECT *FROM AS_INGREDIENTS_LIB";
    // return Response.ok(OfFilles).build();
    // } catch (Exception e) {
    // return null;
    // }
    // }// NOUVEAU SERVICE pour OfFille
        @GET
        @Path("/ofFille")   
        @Produces(MediaType.APPLICATION_JSON)
        public Response getOfFilles() {
            try {
                IngredientsLib IngredientsLib = new IngredientsLib();
                IngredientsLib[] IngredientsLibs = (IngredientsLib[]) CGenUtil.rechercher(IngredientsLib, null, null, "");
                
                // Créer une liste simple pour éviter les problèmes de sérialisation
                List<Map<String, Object>> result = new ArrayList<>();
                
                for (IngredientsLib f : IngredientsLibs) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", f.getId());
                    item.put("idIngredients", f.getId());
                    item.put("libelle", f.getLibelle());
                    item.put("qte", f.getQuantiteParPack());
                    item.put("idunite", f.getUnite());
                    item.put("remarque", f.getLibelleVente());
                    // N'ajoutez PAS les getters qui font des appels DB complexes
                    
                    result.add(item);
                }
                
                return Response.ok(result)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET")
                        .build();
                
            } catch (Exception e) {
                e.printStackTrace(); // Important pour voir l'erreur dans les logs
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\":\"Erreur ofFille: " + e.getMessage() + "\"}")
                            .header("Access-Control-Allow-Origin", "*")
                            .build();
            }
        }
@GET
        @Path("/off")   
        @Produces(MediaType.APPLICATION_JSON)
        public Response getOf() {
            try {
                Of Of = new Of();
                Of[] Ofs = (Of[]) CGenUtil.rechercher(Of, null, null, "");
                
                // Créer une liste simple pour éviter les problèmes de sérialisation
                List<Map<String, Object>> result = new ArrayList<>();
                
                for (Of f : Ofs) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", f.getId());
                    item.put("idOf", f.getId());
                    item.put("libelle", f.getLibelle());
                    // N'ajoutez PAS les getters qui font des appels DB complexes
                    
                    result.add(item);
                }
                
                return Response.ok(result)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET")
                        .build();
                
            } catch (Exception e) {
                e.printStackTrace(); // Important pour voir l'erreur dans les logs
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\":\"Erreur ofFille: " + e.getMessage() + "\"}")
                            .header("Access-Control-Allow-Origin", "*")
                            .build();
            }
        }
        
@GET
        @Path("/bc")   
        @Produces(MediaType.APPLICATION_JSON)
        public Response getOf() {
            try {
                Of Of = new Of();
                Of[] Ofs = (Of[]) CGenUtil.rechercher(Of, null, null, "");
                
                // Créer une liste simple pour éviter les problèmes de sérialisation
                List<Map<String, Object>> result = new ArrayList<>();
                
                for (Of f : Ofs) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", f.getId());
                    item.put("idOf", f.getId());
                    item.put("libelle", f.getLibelle());
                    // N'ajoutez PAS les getters qui font des appels DB complexes
                    
                    result.add(item);
                }
                
                return Response.ok(result)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET")
                        .build();
                
            } catch (Exception e) {
                e.printStackTrace(); // Important pour voir l'erreur dans les logs
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\":\"Erreur ofFille: " + e.getMessage() + "\"}")
                            .header("Access-Control-Allow-Origin", "*")
                            .build();
            }
        }
    }
