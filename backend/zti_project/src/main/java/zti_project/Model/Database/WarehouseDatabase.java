package zti_project.Model.Database;

import zti_project.Model.Models.product.Product;
import zti_project.Model.Models.storedProduct.StoredProduct;
import zti_project.Model.Models.storedProduct.StoredProductIdData;
import zti_project.Model.Models.storedProduct.StoredProductUpdateData;
import zti_project.Model.Models.storedProduct.Warehouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDatabase extends Database
{
    public Warehouse getWarehouse(String username)
    {
        try
        {
            String sqlQuery = "SELECT warehouse.warehouse_id, warehouse.name " +
                    "FROM warehouse.warehouse " +
                    "JOIN warehouse.Employee ON Employee.warehouse_id = warehouse.warehouse_id " +
                    "WHERE Employee.username = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setString(1, username);
            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                Integer warehouseId = Integer.parseInt(rset.getString("warehouse_id"));
                String warehouseName = rset.getString("name");

                return new Warehouse(warehouseId, warehouseName);
            }
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public List<StoredProduct> getStoredProducts(Integer warehouseId)
    {
        try
        {
            ArrayList<StoredProduct> productList = new ArrayList<>();
            String sqlQuery = "SELECT p.*, sp.quantity " +
                    "FROM warehouse.warehouse w " +
                    "JOIN warehouse.StoredProduct sp ON sp.warehouse_id = w.warehouse_id " +
                    "JOIN warehouse.Product p ON p.product_id = sp.product_id " +
                    "WHERE w.warehouse_id = ?;";

            prestmt = conn.prepareStatement(sqlQuery);

            prestmt.setInt(1, warehouseId);
            ResultSet rset = prestmt.executeQuery();
            while(rset.next())
            {
                Integer productId = rset.getInt("product_id");
                String name = rset.getString("name");
                Integer quantity = rset.getInt("quantity");
                String description = rset.getString("description");
                Product p = new Product(productId, name, description);
                StoredProduct sp = new StoredProduct(p, quantity);
                productList.add(sp);
            }
            return productList;
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    public List<Product> getProductList()
    {
        try
        {
            ArrayList<Product> productList = new ArrayList<>();
            String sqlQuery = "SELECT * FROM warehouse.Product";

            prestmt = conn.prepareStatement(sqlQuery);
            ResultSet rset = prestmt.executeQuery();
            while(rset.next())
            {
                Integer id = rset.getInt("product_id");
                String name = rset.getString("name");
                String description = rset.getString("description");
                Product p = new Product(id, name, description );
                productList.add(p);
            }
            return productList;
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void createProduct(Product product)
    {
        try
        {
            String productQuery = "INSERT INTO warehouse.product (name, description) " +
                    "SELECT ?, ? WHERE NOT EXISTS " +
                    "(SELECT * FROM warehouse.product WHERE name = ? AND description = ?)";
            prestmt = conn.prepareStatement(productQuery, Statement.RETURN_GENERATED_KEYS);
            prestmt.setString(1, product.getName());
            prestmt.setString(2, product.getDescription());
            prestmt.setString(3, product.getName());
            prestmt.setString(4, product.getDescription());
            prestmt.executeUpdate();
            ResultSet rs = prestmt.getGeneratedKeys();
            rs.next();

        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void createStoredProduct(Integer warehouseId, StoredProductIdData product)
    {
        try
        {
            String storedProductQuery = "INSERT INTO warehouse.StoredProduct(product_id, warehouse_id, quantity)" +
                    " VALUES (?, ?, ?)";

            prestmt = conn.prepareStatement(storedProductQuery);
            prestmt.setInt(1, product.getProductId());
            prestmt.setInt(2, warehouseId);
            prestmt.setInt(3, product.getQuantity());
            prestmt.executeUpdate();

        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void updateStoredProduct(Integer warehouse_id, StoredProductUpdateData data)
    {
        try
        {
            String productQuery = "UPDATE warehouse.StoredProduct SET quantity = ? " +
                    "WHERE product_id = ? AND warehouse_id = ?";

            prestmt = conn.prepareStatement(productQuery, Statement.RETURN_GENERATED_KEYS);
            prestmt.setInt(1, data.getQuantity());
            prestmt.setInt(2, data.getProductId());
            prestmt.setInt(3, warehouse_id);
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public Boolean isProductInWarehouse(Integer productId, Integer warehouseId)
    {
        try
        {
            String productQuery = "SELECT * FROM warehouse.StoredProduct " +
                    "WHERE product_id = ? AND warehouse_id = ? AND quantity != 0";
            prestmt = conn.prepareStatement(productQuery);
            prestmt.setInt(1, productId);
            prestmt.setInt(2, warehouseId);
            ResultSet rset = prestmt.executeQuery();
            return rset.next();

        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public void deleteStoredProduct(Integer productId, Integer warehouseId)
    {
        try
        {
            String sqlQuery = "DELETE FROM warehouse.StoredProduct WHERE product_id = ? AND warehouse_id = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, productId);
            prestmt.setInt(2, warehouseId);
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public Integer getQuantity(Integer warehouse_id, Integer product_id)
    {
        try
        {
            String sqlQuery = "SELECT quantity FROM warehouse.StoredProduct WHERE warehouse_id = ? AND product_id = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, warehouse_id);
            prestmt.setInt(2, product_id);
            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                return rset.getInt("quantity");
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
