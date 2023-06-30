package zti_project.Model.Database;

import zti_project.Model.Models.order.OrderedProduct;
import zti_project.Model.Models.order.OrderedProductAddData;
import zti_project.Model.Models.product.Product;
import zti_project.Model.Models.product.ProductIdAndQuantity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDatabase extends Database
{
    public List<OrderedProduct> getOrderedProductList()
    {
        try
        {
            ArrayList<OrderedProduct> productList = new ArrayList<>();
            String sqlQuery = "SELECT w.warehouse_id, w.name AS warehouseName," +
                    " op.orderedProduct_id, op.quantity, " +
                    "p.product_id, p.name as productName, p.description " +
                    "from warehouse.OrderedProduct op " +
                    "JOIN warehouse.Warehouse w on w.warehouse_id = op.warehouse_id " +
                    "JOIN warehouse.Product p on p.product_id = op.product_id";

            prestmt = conn.prepareStatement(sqlQuery);
            ResultSet rset = prestmt.executeQuery();
            while(rset.next())
            {
                Integer warehouseId = rset.getInt("warehouse_id");
                String warehouseName = rset.getString("warehouseName");
                Integer orderedProductId = rset.getInt("orderedProduct_id");
                Integer quantity = rset.getInt("quantity");
                Integer productId = rset.getInt("product_id");
                String productName  = rset.getString("productName");
                String productDescription = rset.getString("description");

                Product p = new Product(productId, productName, productDescription);
                OrderedProduct op = new OrderedProduct(orderedProductId, warehouseName, p, quantity, warehouseId);
                productList.add(op);
            }
            return productList;
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void addOrderedProduct(OrderedProductAddData data, Integer warehouseId)
    {
        try
        {
            String productQuery = "INSERT INTO warehouse.OrderedProduct(warehouse_id, product_id, quantity) " +
                    "VALUES (?, ?, ?)";
            prestmt = conn.prepareStatement(productQuery);
            prestmt.setInt(1, warehouseId);
            prestmt.setInt(2, data.getProductId());
            prestmt.setInt(3, data.getQuantity());
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public Boolean canDeleteOrderedProduct(Integer orderedProductId, Integer warehouseId)
    {
        try
        {
            prestmt = conn.prepareStatement("SELECT orderedProduct_id FROM warehouse.OrderedProduct " +
                    "WHERE orderedProduct_id = ? AND product_id = ?");
            prestmt.setInt(1, orderedProductId);
            prestmt.setInt(2, warehouseId);

            ResultSet rset = prestmt.executeQuery();
            return rset.next();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    public void deleteOrderedProduct(Integer orderedProductId)
    {
        try
        {
            String sqlQuery = "DELETE FROM warehouse.OrderedProduct WHERE orderedProduct_id = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, orderedProductId);
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public ProductIdAndQuantity getProductIdAndQuantity(Integer orderedProductId)
    {
        try
        {
            String sqlQuery = "SELECT product_id, quantity FROM warehouse.OrderedProduct WHERE orderedProduct_id = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, orderedProductId);
            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                Integer product_id = rset.getInt("product_id");
                Integer quantity = rset.getInt("quantity");
                return new ProductIdAndQuantity(product_id, quantity);
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public OrderedProduct getOrderedProduct(Integer orderedProductId)
    {
        try
        {
            ArrayList<OrderedProduct> productList = new ArrayList<>();
            String sqlQuery = "SELECT w.warehouse_id, w.name AS warehouseName," +
                    " op.orderedProduct_id, op.quantity, " +
                    "p.product_id, p.name as productName, p.description " +
                    "from warehouse.OrderedProduct op " +
                    "JOIN warehouse.Warehouse w on w.warehouse_id = op.warehouse_id " +
                    "JOIN warehouse.Product p on p.product_id = op.product_id " +
                    "WHERE op.orderedProduct_id = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, orderedProductId);

            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                Integer warehouseId = rset.getInt("warehouse_id");
                String warehouseName = rset.getString("warehouseName");
                Integer quantity = rset.getInt("quantity");
                Integer productId = rset.getInt("product_id");
                String productName  = rset.getString("productName");
                String productDescription = rset.getString("description");
                Product product = new Product(productId, productName, productDescription);
                OrderedProduct op = new OrderedProduct(orderedProductId, warehouseName, product, quantity, warehouseId);
                return op;
            }
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}