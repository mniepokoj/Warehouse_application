package zti_project.Controller.RequestResources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import zti_project.Aspect.UseDatabase;
import zti_project.Controller.Websocket.WebsocketProductResource;
import zti_project.Utils.TokenGenerator;
import zti_project.Model.Database.UserDatabase;
import zti_project.Model.Database.WarehouseDatabase;
import zti_project.Model.Models.authorization.AuthToken;
import zti_project.Model.Models.product.Product;
import zti_project.Model.Models.storedProduct.StoredProduct;
import zti_project.Model.Models.storedProduct.StoredProductIdData;
import zti_project.Model.Models.storedProduct.StoredProductUpdateData;
import zti_project.Model.Models.storedProduct.Warehouse;

import java.util.List;

@Path("/warehouse")
public class WarehouseResource extends Resource
{
    @GET
    @Path("/storedProductList")
    @Produces(MediaType.APPLICATION_JSON)
    @UseDatabase
    public List<StoredProduct> getStoredProducts(@HeaderParam("AuthToken") String token) throws Exception
    {
        AuthToken authToken = new AuthToken(token);

        if (!isTokenValid(authToken))
            return null;
        String username = authToken.getUsername();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();
        Warehouse warehouse = warehouseDatabase.getWarehouse(username);
        List<StoredProduct> products = warehouseDatabase.getStoredProducts(warehouse.getId());
        return products;
    }

    @Path("/addProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response AddProduct(@HeaderParam("AuthToken") String token,
                               Product product)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        warehouseDatabase.createProduct(product);
        WebsocketProductResource.notifyProductChange(authToken.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @Path("/addStoredProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response AddProduct(@HeaderParam("AuthToken") String token,
                                  StoredProductIdData data)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Warehouse warehouse = warehouseDatabase.getWarehouse(authToken.getUsername());
        Boolean isProductInWarehouse = warehouseDatabase.isProductInWarehouse(data.getProductId(),
                warehouse.getId());

        if(isProductInWarehouse == false)
        {
            StoredProductIdData storedProduct = new StoredProductIdData(data.getProductId(), data.getQuantity());
            warehouseDatabase.createStoredProduct(warehouse.getId(), storedProduct );
        }
        else
        {
            Integer quantity = warehouseDatabase.getQuantity(warehouse.getId(), data.getProductId());
            quantity += data.getQuantity();
            StoredProductUpdateData storedProductUpdateData =
                    new StoredProductUpdateData(data.getProductId(), quantity);
            warehouseDatabase.updateStoredProduct(warehouse.getId(), storedProductUpdateData );
        }
        WebsocketProductResource.notifyStoredProductChange(authToken.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @Path("/DeleteStoredProduct/{productId}")
    @DELETE
    @UseDatabase
    public Response DeleteProduct(
            @HeaderParam("AuthToken") String token,
            @PathParam("productId") String productIdString)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        String username = authToken.getUsername();
        Warehouse warehouse = warehouseDatabase.getWarehouse(username);
        Integer productId = Integer.parseInt(productIdString);
        Boolean haveUserAccess = warehouseDatabase.isProductInWarehouse(productId, warehouse.getId());
        if(!haveUserAccess)
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        warehouseDatabase.deleteStoredProduct(productId, warehouse.getId());
        WebsocketProductResource.notifyStoredProductChange(authToken.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @Path("/UpdateStoredProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response UpdateStoredProduct(@HeaderParam("AuthToken") String token,
                                  StoredProductUpdateData data)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        String username = authToken.getUsername();
        Warehouse warehouse = warehouseDatabase.getWarehouse(username);
        if(!warehouseDatabase.isProductInWarehouse(data.getProductId(), warehouse.getId()))
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        warehouseDatabase.updateStoredProduct(warehouse.getId(), data);
        warehouseDatabase.getQuantity(warehouse.getId(), data.getProductId());
        if(warehouseDatabase.getQuantity(warehouse.getId(), data.getProductId()) <= 0)
            warehouseDatabase.deleteStoredProduct(data.getProductId(), warehouse.getId());
        WebsocketProductResource.notifyStoredProductChange(authToken.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @Path("/getProductList")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UseDatabase
    public List<Product> GetProductList(@HeaderParam("AuthToken") String token)
    {
        UserDatabase userDb = new UserDatabase();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        TokenGenerator tokenGenerator = new TokenGenerator();
        AuthToken authToken = new AuthToken(token);

        if (!tokenGenerator.isAuthTokenValid(userDb, authToken))
            return null;

        return warehouseDatabase.getProductList();
    }

}
