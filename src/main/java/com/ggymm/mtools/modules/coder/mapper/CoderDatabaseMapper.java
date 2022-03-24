package com.ggymm.mtools.modules.coder.mapper;

import com.ggymm.mtools.modules.common.DataSource;
import com.ggymm.mtools.modules.coder.entity.CoderDatabase;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongym
 * @version 创建时间: 2022-01-13 21:33
 */
public class CoderDatabaseMapper {

    public static List<CoderDatabase> getList() {
        List<CoderDatabase> databaseList = new ArrayList<>();

        // 获取数据库连接查询数据库
        Connection connection = DataSource.getConnection();
        if (connection != null) {
            final QueryRunner queryRunner = new QueryRunner();
            final String sql = "SELECT * FROM coder_database";
            try {
                databaseList = queryRunner.query(connection, sql, new BeanListHandler<>(CoderDatabase.class));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return databaseList;
    }
}
