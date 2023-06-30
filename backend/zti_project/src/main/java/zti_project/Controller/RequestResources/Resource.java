package zti_project.Controller.RequestResources;

import zti_project.Utils.TokenGenerator;
import zti_project.Model.Database.UserDatabase;
import zti_project.Model.Models.authorization.AuthToken;

public class Resource
{
    public Boolean isTokenValid(AuthToken authToken)
    {
        UserDatabase userDatabase = new UserDatabase();
        TokenGenerator tokenGenerator = new TokenGenerator();
        return tokenGenerator.isAuthTokenValid(userDatabase, authToken);
    }
}
