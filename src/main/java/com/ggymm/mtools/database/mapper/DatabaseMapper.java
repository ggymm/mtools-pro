package com.ggymm.mtools.database.mapper;

import com.ggymm.mtools.database.DataSource;
import com.ggymm.mtools.database.model.Database;
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
public class DatabaseMapper {

    public static List<Database> getList() {
        List<Database> databaseList = new ArrayList<>();

        // 获取数据库连接查询数据库
        Connection connection = DataSource.getConnection();
        if (connection != null) {
            final QueryRunner queryRunner = new QueryRunner();
            final String sql = "select * from database";
            try {
                databaseList = queryRunner.query(connection, sql, new BeanListHandler<>(Database.class));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return databaseList;
    }
}
