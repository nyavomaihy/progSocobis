package api.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider   // IMPORTANT
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) {

        response.getHeaders().putSingle(
            "Access-Control-Allow-Origin",
            "http://localhost"
        );

        response.getHeaders().putSingle(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS"
        );

        response.getHeaders().putSingle(
            "Access-Control-Allow-Headers",
            "Content-Type, Authorization"
        );
    }
}
