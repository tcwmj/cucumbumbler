package com.lombardrisk.utils;

import java.util.List;


/**
 * Create by Leo Tu on Jun 26, 2015
 */
public class DBQuery{
	static String connectedDB = "ar";
    static String DBType = PropHelper.getProperty("db.type").trim();
    
    static String serverName = PropHelper.getProperty("db.sqlserverName").trim();
    static String ip = PropHelper.getProperty("db.ip").trim();
    static String sid = PropHelper.getProperty("db.sid").trim();
    static String user = PropHelper.getProperty("db.DBName").trim().toUpperCase();
    static String ToolsetDB = "";
    static String password = "password";

    public static String queryRecord(String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("oracle", ip, sid, user);
            else
                dh = new DBHelper("oracle", ip, sid, ToolsetDB);
        } else {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("mssql", serverName, user);
            else
                dh = new DBHelper("mssql", serverName, ToolsetDB);
        }
        dh.connect();
        String rst = dh.query(sql);
        dh.close();
        return rst;
    }

    public static String queryRecordSpecDB(String dbName, String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            dh = new DBHelper("oracle", ip, sid, dbName);
        } else {
            dh = new DBHelper("mssql", serverName, dbName);
        }
        dh.connect();
        String rst = dh.query(sql);
        dh.close();
        return rst;
    }

    public static List<String> queryRecords(String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("oracle", ip, sid, user);
            else
                dh = new DBHelper("oracle", ip, sid, ToolsetDB);
        } else {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("mssql", serverName, user);
            else
                dh = new DBHelper("mssql", serverName, ToolsetDB);
        }
        dh.connect();
        List<String> rst = dh.queryRecords(sql);
        dh.close();
        return rst;
    }


    public static int update(String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("oracle", ip, sid, user);
            else
                dh = new DBHelper("oracle", ip, sid, ToolsetDB);
        } else {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("mssql", serverName, user);
            else
                dh = new DBHelper("mssql", serverName, ToolsetDB);
        }
        dh.connect();
        int rst = dh.update(sql);
        return rst;
    }


    

}
   