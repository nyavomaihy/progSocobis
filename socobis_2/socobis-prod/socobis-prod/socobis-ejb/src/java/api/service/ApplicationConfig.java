package api.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import api.filter.CorsFilter;

@ApplicationPath("/api")  // Changé de "/asynclocation/api" à "/api"
public class ApplicationConfig extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // IMPORTANT : Enregistrer vos ressources ici
        classes.add(ContentRessource.class);
        classes.add(OfService.class); // Si vous l'avez
        classes.add(api.filter.CorsFilter.class);

        return classes;
    }
}