package zti_project.Model.Models.product;

public class Product
{
    public Product(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Integer id;
    private String name;
    private String description;

    public Product(){}
    public Product(String name, String description)
    {
        this.id = -1;
        this.name = name;
        this.description = description;
    }
}
