package com.ggymm.mtools.template;

import com.ggymm.mtools.modules.coder.entity.CoderDatabase;
import com.ggymm.mtools.modules.coder.model.TemplateData;
import com.ggymm.mtools.utils.DatabaseUtils;
import com.ggymm.mtools.utils.TemplateUtils;
import com.ggymm.mtools.utils.model.TableField;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * @author gongym
 * @version 创建时间: 2022-03-05 19:24
 */
public class TestTemplate {

    @Test
    public void testGenEntity() throws TemplateException, IOException {
        final CoderDatabase database = new CoderDatabase();
        database.setId(1L);
        database.setShowName("office_platform(本地)");
        database.setDriver("mysql");
        database.setHost("localhost");
        database.setPort("3306");
        database.setName("office_platform");
        database.setUsername("root");
        database.setPassword("root");
        final List<TableField> tableFieldList = DatabaseUtils.tableFieldList(database, "user");

        final TemplateData templateData = new TemplateData();
        templateData.setBasePackageName("com.ninelock.api");

        templateData.setUseParent(false);
        templateData.setParentClass("com.ninelock.core.base.BaseEntity");
        templateData.setExcludeColumn("create_time,create_id,creator,update_time,update_id,del_flag");

        templateData.setAutoFill(true);
        templateData.setAutoFillColumn("create_time,create_id,creator,update_time,update_id,del_flag");

        templateData.setUseOrigin(false);
        templateData.setFormatDate(true);

        templateData.setTableName("user");
        templateData.setFieldList(tableFieldList);

        final Template entity = TemplateUtils.getTemplate("entity");
        assert entity != null;

        StringWriter stringWriter = new StringWriter();
        entity.process(templateData, stringWriter);
        String resultStr = stringWriter.toString();
        System.out.println(resultStr);
    }
}
