package zti_project.Model.Models.authorization;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class LoginData
{
    private String username;
    private String password;
    @JsonbCreator
    public LoginData(@JsonbProperty("username")  String username,
                     @JsonbProperty("password")  String password)
    {
        this.username = username;
        this.password = password;
    }
    public LoginData() {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
