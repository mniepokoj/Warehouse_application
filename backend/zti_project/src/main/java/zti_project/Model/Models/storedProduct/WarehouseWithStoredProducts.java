package zti_project.Model.Models.storedProduct;

import zti_project.Model.Models.product.Product;

import java.util.List;

public class WarehouseWithStoredProducts
{
    Warehouse warehouse;
    List<Product> products;
    public WarehouseWithStoredProducts()
    {
    }
    public WarehouseWithStoredProducts(Warehouse warehouse, List<Product> products)
    {
        this.warehouse = warehouse;
        this.products = products;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
