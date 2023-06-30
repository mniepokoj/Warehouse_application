package zti_project.Model.Models.storedProduct;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class StoredProductIdData
{
    Integer product_id;
    Integer quantity;

    @JsonbCreator
    public StoredProductIdData(@JsonbProperty("product_id") Integer product_id,
                               @JsonbProperty("quantity") Integer quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }



    public Integer getProductId() {
        return product_id;
    }

    public void setProductId(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
