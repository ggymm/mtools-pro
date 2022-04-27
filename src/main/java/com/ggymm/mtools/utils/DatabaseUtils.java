package com.ggymm.mtools.utils;

import com.ggymm.mtools.modules.coder.entity.CoderDatabase;
import com.ggymm.mtools.utils.model.Table;
import com.ggymm.mtools.utils.model.TableField;
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
public class DatabaseUtils {

    public static final String QUERY_TABLE_LIST = "SELECT DISTINCT TABLE_NAME, TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?";
    public static final String QUERY_TABLE_FIELD_LIST = "SELECT COLUMN_NAME, COLUMN_DEFAULT, DATA_TYPE, COLUMN_COMMENT" +
            ", IF(COLUMN_KEY = 'PRI', TRUE, FALSE) AS IS_KEY" +
            ", NUMERIC_PRECISION, NUMERIC_SCALE, CHARACTER_MAXIMUM_LENGTH" +
            ", UPPER(COLUMN_TYPE) AS COLUMN_TYPE" +
            ", IF(EXTRA = 'auto_increment', TRUE, FALSE) AS IS_AUTO" +
            " FROM information_schema.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?";

    public static Connection createConnection(CoderDatabase db) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" +
                    db.getHost() + ":" +
                    db.getPort() + "/" +
                    db.getName() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, db.getUsername(), db.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static List<Table> tableList(CoderDatabase db) {
        final List<Table> tableList = new ArrayList<>();

        final Connection connection = createConnection(db);
        final QueryRunner queryRunner = new QueryRunner();
        try {
            List<Map<String, Object>> tableMapList = queryRunner.query(connection, QUERY_TABLE_LIST, new MapListHandler(), db.getName());
            for (Map<String, Object> tableMap : tableMapList) {
                final Table table = new Table();
                table.setTableName(String.valueOf(tableMap.get("TABLE_NAME")));
                table.setTableComment(String.valueOf(tableMap.get("TABLE_COMMENT")));

                tableList.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }

    public static List<TableField> tableFieldList(CoderDatabase db, String tableName) {
        final List<TableField> tableFieldList = new ArrayList<>();

        final Connection connection = createConnection(db);
        final QueryRunner queryRunner = new QueryRunner();
        try {
            final List<Map<String, Object>> tableFieldMapList = queryRunner.query(connection, QUERY_TABLE_FIELD_LIST, new MapListHandler(), tableName, db.getName());
            for (Map<String, Object> tableMap : tableFieldMapList) {
                final TableField tableField = new TableField();
                tableField.setColumnName(String.valueOf(tableMap.get("COLUMN_NAME")));
                tableField.setColumnDefault(String.valueOf(tableMap.get("COLUMN_DEFAULT")));
                tableField.setDataType(String.valueOf(tableMap.get("DATA_TYPE")));
                tableField.setColumnComment(String.valueOf(tableMap.get("COLUMN_COMMENT")));
                tableField.setIsKey(Integer.parseInt(String.valueOf(tableMap.get("IS_KEY"))) == 1);
                tableField.setNumericPrecision(String.valueOf(tableMap.get("NUMERIC_PRECISION")));
                tableField.setNumericScale(String.valueOf(tableMap.get("NUMERIC_SCALE")));
                tableField.setCharacterMaximumLength(String.valueOf(tableMap.get("CHARACTER_MAXIMUM_LENGTH")));
                tableField.setColumnType(String.valueOf(tableMap.get("COLUMN_TYPE")));
                tableField.setIsAuto(Integer.parseInt(String.valueOf(tableMap.get("IS_AUTO"))) == 1);

                tableFieldList.add(tableField);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableFieldList;
    }
}
