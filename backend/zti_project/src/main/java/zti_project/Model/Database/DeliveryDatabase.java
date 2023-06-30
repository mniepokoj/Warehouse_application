package zti_project.Model.Database;

import zti_project.Model.Models.Delivery.Delivery;
import zti_project.Model.Models.Delivery.DeliveryStatus;
import zti_project.Model.Models.Delivery.DeliveryStatusDescription;
import zti_project.Model.Models.product.Product;
import zti_project.Model.Models.storedProduct.Warehouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDatabase extends Database
{
    public List<Delivery> getDeliveryList(Integer warehouse_id)
    {
        try
        {
            ArrayList<Delivery> deliveryList = new ArrayList<>();
            String sqlQuery = "SELECT d.quantity, d.delivery_id, " +
                    "w_from.warehouse_id as warehouse_id_from, w_from.name as name_from, " +
                    "w_to.warehouse_id as warehouse_id_to, w_to.name as name_to, " +
                    "ds.status_id, ds.name as delivery_status_name, " +
                    "p.name as product_name, p.product_id, p.description as product_description " +
                    "FROM warehouse.Delivery d " +
                    "JOIN warehouse.delivery_status ds ON d.status_id = ds.status_id " +
                    "JOIN warehouse.Warehouse w_from ON w_from.warehouse_id = d.from_warehouse_id " +
                    "JOIN warehouse.Warehouse w_to ON w_to.warehouse_id = d.to_warehouse_id " +
                    "JOIN warehouse.Product p ON p.product_id = d.product_id " +
                    "WHERE (w_from.warehouse_id = ? OR w_to.warehouse_id = ?) " +
                    "AND d.status_id != ?";

            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, warehouse_id);
            prestmt.setInt(2, warehouse_id);
            prestmt.setInt(3, DeliveryStatusDescription.Finished.getValue());
            ResultSet rset = prestmt.executeQuery();
            while(rset.next())
            {
                Integer deliveryId = rset.getInt("delivery_id");
                Integer quantity = rset.getInt("quantity");
                Integer id_from = rset.getInt("warehouse_id_from");
                String name_from = rset.getString("name_from");
                Integer id_to = rset.getInt("warehouse_id_to");
                String name_to = rset.getString("name_to");
                Integer statusId = rset.getInt("status_id");
                String productName = rset.getString("product_name");
                Integer productId = rset.getInt("product_id");
                String productDescription = rset.getString("product_description");

                Warehouse warehouseFrom = new Warehouse(id_from, name_from);
                Warehouse warehouseTo = new Warehouse(id_to, name_to);
                Product product = new Product(productId, productName, productDescription);
                DeliveryStatus deliveryStatus = new DeliveryStatus(statusId);
                Delivery delivery = new Delivery(deliveryId, warehouseFrom, warehouseTo, product, quantity, deliveryStatus);
                deliveryList.add(delivery);
            }
            return deliveryList;
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void createDelivery(Integer fromWarehouseId, Integer toWarehouseId, Integer productId, Integer quantity)
    {
        try
        {
            String productQuery = "INSERT INTO warehouse.Delivery(from_warehouse_id, to_warehouse_id, " +
                    "product_id, quantity, status_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            prestmt = conn.prepareStatement(productQuery);
            prestmt.setInt(1, fromWarehouseId);
            prestmt.setInt(2, toWarehouseId);
            prestmt.setInt(3, productId);
            prestmt.setInt(4, quantity);
            prestmt.setInt(5, DeliveryStatusDescription.Send.getValue());
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void setDeliveryState(Integer deliveryId, Integer statusId)
    {
        try
        {
            String productQuery = "UPDATE warehouse.Delivery SET statusId = ? " +
                    "WHERE delivery_id = ?";

            prestmt = conn.prepareStatement(productQuery, Statement.RETURN_GENERATED_KEYS);
            prestmt.setInt(1, statusId);
            prestmt.setInt(2, deliveryId);
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public Boolean haveWarehouseFromAccessTo(Integer from_warehouse_id, Integer deliveryId)
    {
        try
        {
            prestmt = conn.prepareStatement("SELECT delivery_id FROM warehouse.Delivery " +
                    "WHERE from_warehouse_id = ? AND delivery_id = ?");
            prestmt.setInt(1, from_warehouse_id);
            prestmt.setInt(2, deliveryId);

            ResultSet rset = prestmt.executeQuery();
            return rset.next();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public Delivery getDelivery(Integer deliveryId)
    {
        try
        {
            String sqlQuery = "SELECT d.quantity, " +
                    "w_from.warehouse_id as warehouse_id_from, w_from.name as name_from, " +
                    "w_to.warehouse_id as warehouse_id_to, w_to.name as name_to, " +
                    "ds.status_id, ds.name as delivery_status_name, " +
                    "p.name as product_name, p.product_id, p.description as product_description " +
                    "FROM warehouse.Delivery d " +
                    "JOIN warehouse.delivery_status ds ON d.status_id = ds.status_id " +
                    "JOIN warehouse.Warehouse w_from ON w_from.warehouse_id = d.from_warehouse_id " +
                    "JOIN warehouse.Warehouse w_to ON w_to.warehouse_id = d.to_warehouse_id " +
                    "JOIN warehouse.Product p ON p.product_id = d.product_id " +
                    "WHERE d.delivery_id = ?";

            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, deliveryId);
            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                Integer quantity = rset.getInt("quantity");
                Integer id_from = rset.getInt("warehouse_id_from");
                String name_from = rset.getString("name_from");
                Integer id_to = rset.getInt("warehouse_id_to");
                String name_to = rset.getString("name_to");
                Integer statusId = rset.getInt("status_id");
                String statusName = rset.getString("delivery_status_name");
                String productName = rset.getString("product_name");
                Integer productId = rset.getInt("product_id");
                String productDescription = rset.getString("product_description");

                Warehouse warehouseFrom = new Warehouse(id_from, name_from);
                Warehouse warehouseTo = new Warehouse(id_to, name_to);
                Product product = new Product(productId, productName, productDescription);
                DeliveryStatus deliveryStatus = new DeliveryStatus(statusId);
                Delivery delivery = new Delivery(deliveryId, warehouseFrom, warehouseTo, product, quantity, deliveryStatus);
                return delivery;
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void setDeliveryStatus(Integer deliveryId, DeliveryStatusDescription deliveryStatusDescription)
    {
        try
        {
            String sqlQuery = "UPDATE warehouse.delivery SET status_id = ? WHERE delivery_id = ?";

            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, deliveryStatusDescription.getValue());
            prestmt.setInt(2, deliveryId);
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteDelivery(Integer deliveryId)
    {
        try
        {
            String sqlQuery = "DELETE from warehouse.delivery WHERE delivery_id = ?";

            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, deliveryId);
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}