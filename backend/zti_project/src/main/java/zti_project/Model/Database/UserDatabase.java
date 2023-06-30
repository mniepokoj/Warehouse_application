package zti_project.Model.Database;

import zti_project.Model.Models.authorization.AuthToken;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase extends Database
{
    public Integer getUserId(String username, String password)
    {
        try
        {
            prestmt = conn.prepareStatement("SELECT employee_id FROM warehouse.Employee WHERE  " +
                    "username = ? AND password = ?  ");
            prestmt.setString(1, username);
            prestmt.setString(2, password);
            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                return rset.getInt(1);
            }
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    public void addAuthToken(Integer id, AuthToken token)
    {
        try
        {
            String sqlQuery = "INSERT INTO Warehouse.AuthToken (employee_id, expiration_time, token_value) " +
                    "VALUES (?, ?, ?);";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setInt(1, id);
            prestmt.setTimestamp(2, token.getExpirationTime());
            prestmt.setString(3, token.getTokenValue());
            prestmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }


    public Integer isTokenValueValid(AuthToken token)
    {
        try
        {
            String sqlQuery = "SELECT at.employee_id FROM Warehouse.AuthToken at " +
                    "JOIN warehouse.employee E ON at.employee_id = e.employee_id "+
                    "WHERE e.username = ? AND at.expiration_time = ? AND at.token_value = ?";
            prestmt = conn.prepareStatement(sqlQuery);
            prestmt.setString(1, token.getUsername());
            prestmt.setTimestamp(2, token.getExpirationTime());
            prestmt.setString(3, token.getTokenValue());
            ResultSet rset = prestmt.executeQuery();
            if(rset.next())
            {
                return rset.getInt(1);
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }



    public Boolean isUsernameAlreadyTaken(String username)
    {
        try
        {
            prestmt = conn.prepareStatement("SELECT Employee.employee_id " +
                    "FROM warehouse.Employee WHERE username = ?");
            prestmt.setString(1, username);
            ResultSet rset = prestmt.executeQuery();
            return rset.next();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
