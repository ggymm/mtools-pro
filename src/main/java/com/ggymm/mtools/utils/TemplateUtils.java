package com.ggymm.mtools.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-03-03 19:42
 */
public class TemplateUtils {

    public static Template getTemplate(String name) {
        try {
            final Configuration config = new Configuration(Configuration.VERSION_2_3_31);
            config.setClassForTemplateLoading(TemplateUtils.class, "/template");
            config.setDefaultEncoding("UTF-8");
            return config.getTemplate(name + ".ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
