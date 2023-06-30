package zti_project.Model.Database;

public class DatabaseCollection
{
    public DeliveryDatabase deliveryDatabase;

    public OrderDatabase orderDatabase;
    public UserDatabase userDatabase;
    public WarehouseDatabase warehouseDatabase;

    public DatabaseCollection()
    {
        this.deliveryDatabase = new DeliveryDatabase();
        this.orderDatabase = new OrderDatabase();
        this.userDatabase = new UserDatabase();
        this.warehouseDatabase = new WarehouseDatabase();
    }
}
