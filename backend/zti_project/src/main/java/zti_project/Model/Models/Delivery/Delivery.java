package zti_project.Model.Models.Delivery;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import zti_project.Model.Models.product.Product;
import zti_project.Model.Models.storedProduct.Warehouse;

public class Delivery
{
    private Integer deliveryId;
    private Warehouse warehouseFrom;
    private Warehouse warehouseTo;
    private Product product;
    private Integer quantity;
    private DeliveryStatus deliveryStatus;

    @JsonbCreator
    public Delivery(@JsonbProperty("delivery_id") Integer deliveryId,
                    @JsonbProperty("warehouseFrom") Warehouse warehouseFrom,
                    @JsonbProperty("warehouseTo") Warehouse warehouseTo,
                    @JsonbProperty("product") Product product,
                    @JsonbProperty("quantity") Integer quantity,
                    @JsonbProperty("deliveryStatus") DeliveryStatus deliveryStatus)
    {
        this.deliveryId = deliveryId;
        this.warehouseFrom = warehouseFrom;
        this.warehouseTo = warehouseTo;
        this.product = product;
        this.quantity = quantity;
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Warehouse getWarehouseFrom() {
        return warehouseFrom;
    }

    public void setWarehouseFrom(Warehouse warehouseFrom) {
        this.warehouseFrom = warehouseFrom;
    }

    public Warehouse getWarehouseTo() {
        return warehouseTo;
    }

    public void setWarehouseTo(Warehouse warehouseTo) {
        this.warehouseTo = warehouseTo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
