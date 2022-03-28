package com.ggymm.mtools.modules.coder.mapper;

import com.ggymm.mtools.modules.common.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author gongym
 * @version 创建时间: 2022-03-24 10:22
 */
public class CoderOutputPathMapper {

    public static void update(String path) {
        final String lastOutputPath = lastOutputPath();
        if (lastOutputPath.equals(path)) {
            return;
        }
        save(path);
    }

    public static void save(String path) {
        // 获取数据库连接查询数据库
        Connection connection = DataSource.getConnection();
        if (connection != null) {
            final QueryRunner queryRunner = new QueryRunner();
            final String sql = "INSERT INTO coder_output_path VALUES (NULL, ?)";
            try {
                queryRunner.update(connection, sql, path);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String lastOutputPath() {
        String lastOutputPath = "";

        // 获取数据库连接查询数据库
        Connection connection = DataSource.getConnection();
        if (connection != null) {
            final QueryRunner queryRunner = new QueryRunner();
            final String sql = "SELECT `path` FROM coder_output_path ORDER BY id DESC LIMIT 1";
            try {
                final Map<String, Object> lastData = queryRunner.query(connection, sql, new MapHandler());
                if (lastData != null) {
                    lastOutputPath = String.valueOf(lastData.get("path"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lastOutputPath;
    }
}
