package zti_project.Model.Models.storedProduct;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class StoredProductUpdateData {
    private Integer productId;
    private Integer quantity;

    @JsonbCreator
    public StoredProductUpdateData(@JsonbProperty("productId")Integer productId, @JsonbProperty("quantity")Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and setters

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}