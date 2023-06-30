package zti_project.Utils;

import zti_project.Aspect.UseDatabase;
import zti_project.Model.Database.UserDatabase;
import zti_project.Model.Models.authorization.AuthToken;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

public class TokenGenerator
{
    private static final int TOKEN_LENGTH = 16;
    private static final int TOKEN_EXPIRATION_TIME_IN_HOUR = 24;

    private String generateRandomToken() {
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int tokenLength = TOKEN_LENGTH;

        while (tokenBuilder.length() < tokenLength) {
            int index = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(index);
            tokenBuilder.append(randomChar);
        }

        return tokenBuilder.toString();
    }
    public AuthToken generateAuthToken(String username)
    {
        byte[] bytes = new byte[TOKEN_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        String tokenValue = generateRandomToken();
        LocalDateTime localTime = LocalDateTime.now().plusHours(TOKEN_EXPIRATION_TIME_IN_HOUR);
        Timestamp timestamp = Timestamp.valueOf(localTime);

        AuthToken token = new AuthToken(username, timestamp, tokenValue);

        return token;
    }
    public Boolean isAuthTokenValid(UserDatabase userDatabase, AuthToken token)
    {
        Boolean isTokenNotExpired = !isTokenExpired(token.getExpirationTime());
        Boolean isTokenValid = userDatabase.isTokenValueValid(token) != null;
        return isTokenNotExpired && isTokenValid;
    }

    private boolean isTokenExpired(Timestamp timestamp)
    {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        try
        {
            Boolean expired = timestamp.before(currentTime);
            return expired;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return Boolean.FALSE;
    }
}
