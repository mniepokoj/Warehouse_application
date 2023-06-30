package zti_project.Controller.RequestResources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import zti_project.Aspect.UseDatabase;
import zti_project.Controller.Websocket.WebsocketProductResource;
import zti_project.Utils.TokenGenerator;
import zti_project.Model.Database.Database;
import zti_project.Model.Database.OrderDatabase;
import zti_project.Model.Database.UserDatabase;
import zti_project.Model.Database.WarehouseDatabase;
import zti_project.Model.Models.authorization.AuthToken;
import zti_project.Model.Models.order.OrderedProduct;
import zti_project.Model.Models.order.OrderedProductAddData;
import zti_project.Model.Models.storedProduct.Warehouse;

import java.util.List;

@Path("/order")
public class OrderResource extends Resource
{
    @GET
    @Path("/OrderedProductList")
    @Produces(MediaType.APPLICATION_JSON)
    @UseDatabase
    public List<OrderedProduct> getStoredProducts(@HeaderParam("AuthToken") String token)
    {
        UserDatabase userDatabase = new UserDatabase();
        OrderDatabase orderDatabase = new OrderDatabase();
        AuthToken authToken = new AuthToken(token);
        TokenGenerator tokenGenerator = new TokenGenerator();
        Boolean isAuthTokenValid = tokenGenerator.isAuthTokenValid(userDatabase, authToken);
        if (!isAuthTokenValid)
            return null;
        return orderDatabase.getOrderedProductList();
    }

    @Path("/addOrderedProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response AddProduct(@HeaderParam("AuthToken") String token,
                               OrderedProductAddData data)
    {
        OrderDatabase orderDatabase = new OrderDatabase();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        String username = authToken.getUsername();
        Warehouse warehouse = warehouseDatabase.getWarehouse(username);
        orderDatabase.addOrderedProduct(data, warehouse.getId());
        WebsocketProductResource.notifyOrderProductChange(authToken.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @Path("/deleteOrderedProduct/{orderedProductId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response AddProduct(@HeaderParam("AuthToken") String token,
                               @PathParam("orderedProductId") Integer orderedProductId)
    {
        OrderDatabase orderDatabase = new OrderDatabase();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Warehouse warehouse = warehouseDatabase.getWarehouse(authToken.getUsername());
        if(orderDatabase.canDeleteOrderedProduct(orderedProductId, warehouse.getId()))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        orderDatabase.deleteOrderedProduct( orderedProductId);
        WebsocketProductResource.notifyOrderProductChange(authToken.getUsername());
        return Response.status(Response.Status.OK).build();
    }
}