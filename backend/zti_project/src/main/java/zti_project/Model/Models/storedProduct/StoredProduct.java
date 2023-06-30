package zti_project.Model.Models.storedProduct;

import jakarta.json.bind.annotation.JsonbProperty;
import zti_project.Model.Models.product.Product;

public class StoredProduct
{
    Product product;
    Integer quantity;

    public StoredProduct(@JsonbProperty("product_id") Product product, @JsonbProperty("quantity") Integer quantity)
    {
        this.product = product;
        this.quantity = quantity;
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
}
