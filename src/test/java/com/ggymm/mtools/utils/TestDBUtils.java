package com.ggymm.mtools.utils;

import com.ggymm.mtools.database.model.Database;
import com.ggymm.mtools.utils.model.Table;
import org.junit.Test;

import java.util.List;

/**
 * @author gongym
 * @version 创建时间: 2022-01-15 19:52
 */
public class TestDBUtils {


    @Test
    public void testTableList() {
        final Database database = new Database();
        database.setId(1L);
        database.setShowName("office_platform(本地)");
        database.setDriver("mysql");
        database.setHost("localhost");
        database.setPort("3306");
        database.setName("office_platform");
        database.setUsername("root");
        database.setPassword("root");

        List<Table> tableList = DatabaseUtils.tableList(database);
        assert tableList.size() > 0;

        for (Table table : tableList) {
            System.out.println(table);
        }
    }
}
