package zti_project.Model.Models.authorization;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AuthToken
{
    private static final String TIMESTAMP_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSSSSSS";
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getExpirationTime()
    {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime)
    {
        this.expirationTime = expirationTime;
    }

    public void setExpirationTime(String expirationTimeString)
    {
        expirationTime = convertStringToTimestamp(expirationTimeString);
        this.expirationTime = expirationTime;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    private String username;
    private Timestamp expirationTime;
    private String tokenValue;
    private static final String tokenDivider = "_";

    private Timestamp convertStringToTimestamp(String timeString)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMATTER);
        LocalDateTime parsedDateTime = LocalDateTime.parse(timeString, formatter);
        Timestamp timestamp = Timestamp.valueOf(parsedDateTime);
        return timestamp;
    }
    public AuthToken(String username, Timestamp expirationTime, String tokenValue) {
        this.username = username;
        this.expirationTime = expirationTime;
        this.tokenValue = tokenValue;
    }

    public AuthToken(String token)
    {
        String username = null;
        Timestamp expirationTime = null;
        String tokenValue = null;
        try
        {
            String[] parts = token.split(tokenDivider);
            username = parts[0];
            expirationTime = convertStringToTimestamp(parts[1]);
            tokenValue = parts[2];
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        this.username = username;
        this.expirationTime = expirationTime;
        this.tokenValue = tokenValue;
    }

    @Override
    public String toString()
    {
        return username + tokenDivider + expirationTime + tokenDivider + tokenValue;
    }
}
