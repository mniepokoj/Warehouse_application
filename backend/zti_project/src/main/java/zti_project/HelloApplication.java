package zti_project;

import zti_project.Controller.RequestResources.DeliveryResource;
import zti_project.Controller.RequestResources.OrderResource;
import zti_project.Controller.RequestResources.UserResource;
import zti_project.Controller.RequestResources.WarehouseResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import zti_project.Controller.Websocket.WebsocketProductResource;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {

    public Set<Class<?>> getClasses()
    {
        HashSet<Class<?>> set = new HashSet<Class<?>>();
        set.add(WarehouseResource.class);
        set.add(UserResource.class);
        set.add(OrderResource.class);
        set.add(DeliveryResource.class);
        set.add(WebsocketProductResource.class);
        return set;
    }
    @Override
    public Set<Object> getSingletons()
    {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new CORSFilter());
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        singletons.add(jsonProvider);
        return singletons;
    }
}