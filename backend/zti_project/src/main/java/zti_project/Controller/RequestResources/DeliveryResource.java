package zti_project.Controller.RequestResources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import zti_project.Aspect.UseDatabase;
import zti_project.Controller.Websocket.WebsocketProductResource;
import zti_project.Model.Database.*;
import zti_project.Model.Models.Delivery.*;
import zti_project.Model.Models.authorization.AuthToken;
import zti_project.Model.Models.order.OrderedProduct;
import zti_project.Model.Models.storedProduct.StoredProductIdData;
import zti_project.Model.Models.storedProduct.StoredProductUpdateData;
import zti_project.Model.Models.storedProduct.Warehouse;

import java.util.List;

@Path("/delivery")
public class DeliveryResource extends Resource
{
    @GET
    @Path("/DeliveryList")
    @Produces(MediaType.APPLICATION_JSON)
    @UseDatabase
    public List<Delivery> getDeliveryList(@HeaderParam("AuthToken") String token)
    {
        DeliveryDatabase deliveryDatabase = new DeliveryDatabase();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return null;
        Warehouse warehouse = warehouseDatabase.getWarehouse(authToken.getUsername());
        return deliveryDatabase.getDeliveryList(warehouse.getId());
    }


    @Path("/addDeliveryProduct/{orderId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response AddDeliveryProduct(@HeaderParam("AuthToken") String token,
                                       @PathParam("orderId") Integer orderId)
    {
        OrderDatabase orderDatabase = new OrderDatabase();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();
        DeliveryDatabase deliveryDatabase = new DeliveryDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        OrderedProduct op = orderDatabase.getOrderedProduct(orderId);
        Warehouse warehouseFrom = warehouseDatabase.getWarehouse(authToken.getUsername());
        Integer quantityInWarehouse =  warehouseDatabase.getQuantity(warehouseFrom.getId(),
                op.getProduct().getId());
        Boolean isTheSameWarehouse = op.getWarehouseId() == warehouseFrom.getId();
        if( quantityInWarehouse < op.getProductQuantity() || isTheSameWarehouse )
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        Integer newQuantityInWarehouse = quantityInWarehouse - op.getProductQuantity();
        StoredProductUpdateData storedProductUpdateData =
                new StoredProductUpdateData(op.getProduct().getId(), newQuantityInWarehouse);
        warehouseDatabase.updateStoredProduct(warehouseFrom.getId(), storedProductUpdateData);
        orderDatabase.deleteOrderedProduct(orderId);
        deliveryDatabase.createDelivery(warehouseFrom.getId(), op.getWarehouseId(), op.getProduct().getId(),
                op.getProductQuantity());

        WebsocketProductResource.notifyStoredProductChange(authToken.getUsername());
        WebsocketProductResource.notifyOrderProductChange(authToken.getUsername());
        WebsocketProductResource.notifyDeliverProductChange(authToken.getUsername());

        return Response.status(Response.Status.OK).build();
    }

    @Path("/cancellDelivery/{deliveryId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response Cancel(@HeaderParam("AuthToken") String token,
                           @PathParam("deliveryId") Integer deliveryId)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();
        DeliveryDatabase deliveryDatabase = new DeliveryDatabase();
        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Delivery delivery = deliveryDatabase.getDelivery(deliveryId);
        if(warehouseDatabase.getWarehouse(authToken.getUsername()).getId() != delivery.getWarehouseFrom().getId())
            return Response.status(Response.Status.BAD_REQUEST).build();

        DeliveryStatusDescription deliveryStatusDescription  = DeliveryStatusDescription.Send;
        deliveryStatusDescription.setValue(delivery.getDeliveryStatus().getStatusId());

        if(deliveryStatusDescription != DeliveryStatusDescription.Send)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Warehouse warehouseFrom = delivery.getWarehouseFrom();
        Integer currentProductQuantity = warehouseDatabase.getQuantity(warehouseFrom.getId(),
                delivery.getProduct().getId());
        StoredProductUpdateData storedProductUpdateData = new StoredProductUpdateData(
                delivery.getProduct().getId(), delivery.getQuantity() + currentProductQuantity);
        warehouseDatabase.updateStoredProduct(warehouseFrom.getId(), storedProductUpdateData);
        deliveryDatabase.setDeliveryStatus(delivery.getDeliveryId(), DeliveryStatusDescription.Cancelled);

        WebsocketProductResource.notifyStoredProductChange(authToken.getUsername());
        WebsocketProductResource.notifyDeliverProductChange(authToken.getUsername());

        return Response.status(Response.Status.OK).build();
    }


    @Path("/receiveDelivery/{deliveryId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response ReceiveDelivery(@HeaderParam("AuthToken") String token,
                                    @PathParam("deliveryId") Integer deliveryId)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();
        DeliveryDatabase deliveryDatabase = new DeliveryDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Delivery delivery = deliveryDatabase.getDelivery(deliveryId);
        if(warehouseDatabase.getWarehouse(authToken.getUsername()).getId() != delivery.getWarehouseTo().getId())
            return Response.status(Response.Status.BAD_REQUEST).build();

        if(delivery.getDeliveryStatus().getStatusId() != DeliveryStatusDescription.Send.getValue())
            return Response.status(Response.Status.BAD_REQUEST).build();

        Integer warehouseToId = delivery.getWarehouseTo().getId();
        Boolean isProductInWarehouse = warehouseDatabase.isProductInWarehouse(delivery.getProduct().getId(),
                warehouseToId);

        if(isProductInWarehouse == false)
        {
            StoredProductIdData storedProductIdData = new StoredProductIdData(delivery.getProduct().getId(),
                    delivery.getQuantity());
            warehouseDatabase.createStoredProduct(warehouseToId, storedProductIdData );
        }
        else
        {
            Integer quantity = warehouseDatabase.getQuantity(warehouseToId, delivery.getProduct().getId());
            quantity += delivery.getQuantity();
            StoredProductUpdateData storedProductUpdateData =
                    new StoredProductUpdateData(delivery.getProduct().getId(), quantity);
            warehouseDatabase.updateStoredProduct(warehouseToId, storedProductUpdateData );
        }
        deliveryDatabase.setDeliveryStatus(delivery.getDeliveryId(), DeliveryStatusDescription.Received);

        WebsocketProductResource.notifyStoredProductChange(authToken.getUsername());
        WebsocketProductResource.notifyDeliverProductChange(authToken.getUsername());

        return Response.status(Response.Status.OK).build();
    }


    @Path("/confirmReceiveDelivery/{deliveryId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response confirmReceiveDelivery(@HeaderParam("AuthToken") String token,
                                           @PathParam("deliveryId") Integer deliveryId)
    {
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();
        DeliveryDatabase deliveryDatabase = new DeliveryDatabase();
        AuthToken authToken = new AuthToken(token);

        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Delivery delivery = deliveryDatabase.getDelivery(deliveryId);
        if(warehouseDatabase.getWarehouse(authToken.getUsername()).getId() != delivery.getWarehouseFrom().getId())
            return Response.status(Response.Status.BAD_REQUEST).build();

        if(delivery.getDeliveryStatus().getStatusId() != DeliveryStatusDescription.Received.getValue())
            return Response.status(Response.Status.BAD_REQUEST).build();

        deliveryDatabase.setDeliveryStatus(delivery.getDeliveryId(), DeliveryStatusDescription.Finished);

        WebsocketProductResource.notifyDeliverProductChange(authToken.getUsername());

        return Response.status(Response.Status.OK).build();
    }

    @Path("/confirmCancellDelivery/{deliveryId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response confirmCancellDelivery(@HeaderParam("AuthToken") String token,
                                           @PathParam("deliveryId") Integer deliveryId)
    {
        System.out.println();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();
        DeliveryDatabase deliveryDatabase = new DeliveryDatabase();

        AuthToken authToken = new AuthToken(token);
        if (!isTokenValid(authToken))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        Delivery delivery = deliveryDatabase.getDelivery(deliveryId);

        if(warehouseDatabase.getWarehouse(authToken.getUsername()).getId() != delivery.getWarehouseTo().getId())
            return Response.status(Response.Status.BAD_REQUEST).build();

        if(delivery.getDeliveryStatus().getStatusId() != DeliveryStatusDescription.Cancelled.getValue())
            return Response.status(Response.Status.BAD_REQUEST).build();

        deliveryDatabase.setDeliveryStatus(delivery.getDeliveryId(), DeliveryStatusDescription.Finished);

        WebsocketProductResource.notifyDeliverProductChange(authToken.getUsername());

        return Response.status(Response.Status.OK).build();
    }
}