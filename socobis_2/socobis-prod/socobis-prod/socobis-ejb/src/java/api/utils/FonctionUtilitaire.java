package api.utils;

import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import org.springframework.mock.web.MockHttpServletRequest;

import bean.CGenUtil;
import bean.TypeObjet;
import user.UserEJB;
import user.UserEJBClient;

public class FonctionUtilitaire {

    public static String[] generateIndexArray(int lignes) {
        if (lignes <= 0) {
            return new String[0]; // renvoie un tableau vide si lignes <= 0
        }

        String[] result = new String[lignes];

        for (int i = 0; i < lignes; i++) {
            result[i] = String.valueOf(i);
        }

        return result;
    }


    public static MockHttpServletRequest transformeHashMap (HashMap <String,String> valeurs) {
        MockHttpServletRequest req = new MockHttpServletRequest();
        valeurs.forEach((k, v) -> {
            if (v.contains(",")) {
                req.addParameter(k, v.split(","));
            } else {
                req.addParameter(k, v);
            }
        });
        return req;
    }


    public static Response retournerHashMapJson(HashMap<String, String> map) {
        JsonObject jsonObject = map.entrySet().stream()
            .collect(Json::createObjectBuilder,
                    (builder, entry) -> builder.add(entry.getKey(), entry.getValue()),
                    (b1, b2) -> {})
            .build();
        
        return Response.ok(jsonObject).build();
    }


    public static UserEJB login () throws Exception {
        UserEJB u = null;
        String interim = null;
        String service = null;
        historique.MapUtilisateur ut = null;

        u = UserEJBClient.lookupUserEJBBeanLocal();
        u.testLogin("admin", "test", interim, service);
    
        ut = u.getUser();
        TypeObjet crd = new TypeObjet();
        crd.setNomTable("LOG_DIRECTION");
        crd.setId(ut.getAdruser());
        TypeObjet[] ret = (TypeObjet[]) CGenUtil.rechercher(crd, null, null, "");
        
        if (ret.length > 0) {
            u.setIdDirection(ret[0].getVal());
        }
        return u;
    }
    
}
