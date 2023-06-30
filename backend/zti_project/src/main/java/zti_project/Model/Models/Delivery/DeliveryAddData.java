package zti_project.Model.Models.Delivery;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class DeliveryAddData
{
    Integer orderedProductId;

    @JsonbCreator
    public DeliveryAddData(@JsonbProperty("orderedProduct_id") Integer orderedProductId) {
        this.orderedProductId = orderedProductId;
    }

    public Integer getOrderedProductId() {
        return orderedProductId;
    }

    public void setOrderedProductId(Integer orderedProductId) {
        this.orderedProductId = orderedProductId;
    }
}
