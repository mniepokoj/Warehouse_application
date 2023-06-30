package zti_project.Model.Models.order;

import jakarta.json.bind.annotation.JsonbProperty;
import zti_project.Model.Models.product.Product;

public class OrderedProduct
{
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    private Integer warehouseId;
    private Integer orderedProductId;
    private String warehouseName;
    private Product product;
    private Integer productQuantity;

    public OrderedProduct(@JsonbProperty("orderedProductId") Integer orderedProductId,
                          @JsonbProperty("warehouseName") String warehouseName,
                          @JsonbProperty("product") Product product,
                          @JsonbProperty("productQuantity") Integer productQuantity,
                          @JsonbProperty("warehouseId") Integer warehouseId) {
        this.orderedProductId = orderedProductId;
        this.warehouseName = warehouseName;
        this.product = product;
        this.productQuantity = productQuantity;
        this.warehouseId = warehouseId;
    }

    public Integer getOrderedProductId() {
        return orderedProductId;
    }

    public void setOrderedProductId(Integer orderedProductId) {
        this.orderedProductId = orderedProductId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}
