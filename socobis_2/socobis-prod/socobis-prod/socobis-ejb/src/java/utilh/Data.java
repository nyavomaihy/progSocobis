package utilh;

import java.sql.*;

public class Data
{
    private Connection c;
    private String sql;

    public Data(Connection co, String s)
    {
        this.c=co;
        this.sql=s;
    }
    public void SetConnection(Connection co)
    {
        this.c=co;
    }
    public Connection GetConnection()
    {
        return this.c;
    }
    public void SetSql(String s)
    {
        this.sql=s;
    }
    public String GetSql()
    {
        return this.sql;
    }


}