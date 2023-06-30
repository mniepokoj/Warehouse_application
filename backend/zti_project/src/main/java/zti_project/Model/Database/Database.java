package zti_project.Model.Database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class Database {
    static protected Connection conn = null;
    static protected PreparedStatement prestmt = null;
    protected Statement stmt = null;
    protected ResultSet rset = null;

    public Database()
    {
    }

    public static void createConnection()
    {
        try
        {
            Context context = new InitialContext();
            DataSource ds1 = (DataSource) context.lookup("java:comp/env/jdbc/postgres");
            conn = ds1.getConnection();
        }
        catch (NamingException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static void closeConnection()
    {
        try
        {
            if (prestmt != null)
            {
                prestmt.close();
            }
            if (conn != null && !conn.isClosed())
            {
                conn.close();
            }
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}