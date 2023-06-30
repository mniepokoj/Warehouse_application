package zti_project.Controller.RequestResources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import zti_project.Aspect.UseDatabase;
import zti_project.Utils.TokenGenerator;
import zti_project.Model.Database.UserDatabase;
import zti_project.Model.Database.WarehouseDatabase;
import zti_project.Model.Models.authorization.AuthToken;
import zti_project.Model.Models.authorization.LoginData;
import zti_project.Model.Models.storedProduct.Warehouse;

@Path("/")
public class UserResource extends Resource
{
    @Path("login")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @UseDatabase
    public Response login(LoginData loginData)
    {
        UserDatabase userDatabase = new UserDatabase();
        WarehouseDatabase warehouseDatabase = new WarehouseDatabase();

        TokenGenerator tokenGenerator = new TokenGenerator();
        Integer userId = userDatabase.getUserId(loginData.getUsername(), loginData.getPassword());
        if (userId != null)
        {
            AuthToken token = tokenGenerator.generateAuthToken(loginData.getUsername());
            userDatabase.addAuthToken(userId, token);

            Warehouse warehouse = warehouseDatabase.getWarehouse(token.getUsername());
            return Response.ok(warehouse).header("AuthToken", token.toString()).build();
        }
        else
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}