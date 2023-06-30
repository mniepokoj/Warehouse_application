package zti_project.Model.Models.order;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class OrderedProductAddData
{
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

    Integer productId;
    Integer quantity;

    @JsonbCreator
    public OrderedProductAddData(@JsonbProperty("product_id")Integer productId,
                                 @JsonbProperty("quantity")Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
