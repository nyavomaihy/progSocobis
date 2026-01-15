package api.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParserSimple {
    
    public static HashMap<String, String> jsonToHashMap(String jsonString) {
        HashMap<String, String> map = new HashMap<>();
        
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return map;
        }
        
        // Nettoyer les espaces et les accolades
        String cleanJson = jsonString.trim()
            .replaceAll("[{}\"]", "")
            .replaceAll("\\s+", "");
        
        // Regex pour capturer "clé:valeur"
        Pattern pattern = Pattern.compile("([^:,]+):([^,]+)");
        Matcher matcher = pattern.matcher(cleanJson);
        
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
            map.put(key, value);
        }
        
        return map;
    }


    /**
     * Retourne un HashMap ne contenant que les clés correspondant
     * aux attributs de la classe donnée (avec ou sans suffixe numérique).
     */
    public static HashMap<String, String> filtrer(
            HashMap<String, String> valeurs, Class<?> classeCible) {

        HashMap<String, String> resultat = new HashMap<>();

        // Pour chaque attribut de la classe
        for (Field champ : classeCible.getDeclaredFields()) {
            String nomChamp = champ.getName();

            // On construit un pattern pour trouver toutes les clés commençant par nomChamp + "_<nombre>"
            Pattern pattern = Pattern.compile("^" + Pattern.quote(nomChamp) + "(?:_\\d+)?$");

            boolean trouve = false;

            // On parcourt toutes les entrées du HashMap reçu
            for (String cle : valeurs.keySet()) {
                if (pattern.matcher(cle).matches()) {
                    trouve = true;
                    resultat.put(cle, valeurs.get(cle) != null ? valeurs.get(cle) : "");
                }
            }

            // Si aucune clé ne correspond (ni avec ni sans suffixe)
            if (!trouve) {
                resultat.put(nomChamp, "");
            }
        }

        return resultat;
    }
}