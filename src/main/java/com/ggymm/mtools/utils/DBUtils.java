package com.ggymm.mtools.utils;

import com.ggymm.mtools.database.model.Database;
import com.ggymm.mtools.utils.model.Table;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gongym
 * @version 创建时间: 2022-01-14 09:49
 */
public class DBUtils {

    public static final String QUERY_TABLE_LIST_SQL = "SELECT DISTINCT TABLE_NAME, TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?";

    public static Connection createConnection(Database db) {
        Connection connection = null;
        try {
            String url = "";
            if ("mysql".equals(db.getDriver())) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                url = "jdbc:mysql://" +
                        db.getHost() + ":" +
                        db.getPort() + "/" +
                        db.getName() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            }
            connection = DriverManager.getConnection(url, db.getUsername(), db.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static List<Table> tableList(Database db) {
        List<Table> tableList = new ArrayList<>();

        Connection connection = createConnection(db);
        QueryRunner queryRunner = new QueryRunner();
        try {
            List<Map<String, Object>> tableMapList = new ArrayList<>();;
            if ("mysql".equals(db.getDriver())) {
                tableMapList  = queryRunner.query(connection, QUERY_TABLE_LIST_SQL, new MapListHandler(), db.getName());
            }

            for (Map<String, Object> tableMap : tableMapList) {
                Table table = new Table();
                table.setTableName(tableMap.get("TABLE_NAME").toString());
                table.setTableComment(tableMap.get("TABLE_COMMENT").toString());

                tableList.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }
}
