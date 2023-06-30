package zti_project.Model.Models.storedProduct;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class Warehouse
{
    Integer id;
    String name;

    @JsonbCreator
    public Warehouse(@JsonbProperty("warehouse_id")Integer id,
                     @JsonbProperty("name")String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
