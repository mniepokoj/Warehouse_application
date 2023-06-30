package zti_project;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException
    {
        try
        {
            if (requestContext.getRequest().getMethod().equals("OPTIONS")) {
                // Obsługa żądania preflight OPTIONS
                responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
                responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
                responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, AuthToken");
                responseContext.getHeaders().add("Access-Control-Expose-Headers", "AuthToken");
            }
            else
            {
                responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
                responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
                responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, authtoken");
                responseContext.getHeaders().add("Access-Control-Expose-Headers", "AuthToken");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }


    }
}