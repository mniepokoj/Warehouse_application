package zti_project.Model.Models.Delivery;

public enum DeliveryStatusDescription
{
    Send(0),
    Received(1),
    Cancelled(2),
    Finished(3),
    CancellationConfirmation(4),
    ReceivedConfirmation(5);

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value;

    DeliveryStatusDescription(int value)
    {
        this.value = value;
    }

    public String getStatusName()
    {
        return String.valueOf(value);
    }
}