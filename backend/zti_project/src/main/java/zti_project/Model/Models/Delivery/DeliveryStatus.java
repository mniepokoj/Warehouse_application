package zti_project.Model.Models.Delivery;

public class DeliveryStatus
{
    DeliveryStatusDescription statusId;
    String name;

    public DeliveryStatus(DeliveryStatusDescription statusId) {
        this.statusId = statusId;
        this.name = this.statusId.getStatusName();
    }
    public DeliveryStatus(Integer statusId) {
        this.statusId = fromValue(statusId);
        this.name = this.statusId.getStatusName();
    }

    public static DeliveryStatusDescription fromValue(int value) {
        for (DeliveryStatusDescription status : DeliveryStatusDescription.values())
        {
            if (status.getValue() == value)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value for DeliveryStatusDescription: " + value);
    }

    public Integer getStatusId()
    {
        return statusId.getValue();
    }

    public void setStatusId(DeliveryStatusDescription statusId) {
        this.statusId = statusId;
    }

    public String getName()
    {
        return name;
    }
}
