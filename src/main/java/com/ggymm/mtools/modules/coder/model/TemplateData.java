package com.ggymm.mtools.modules.coder.model;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ggymm.mtools.utils.model.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.CaseFormat.*;

/**
 * @author gongym
 * @version 创建时间: 2022-03-05 21:07
 */
@Getter
public class TemplateData {
    private final String author;
    private final String now;
    /**
     * 配置信息
     */
    @Setter
    private String basePackageName;
    @Setter
    private Boolean useParent;
    private String parentClass;
    private String parentClassName;
    @Setter
    private Boolean useOrigin;
    @Setter
    private Boolean useTableAsPackage;
    @Setter
    private String excludeColumn;
    private String[] excludeColumns;
    @Setter
    private Boolean autoFill;
    @Setter
    private String autoFillColumn;
    private String[] autoFillColumns;
    @Setter
    private Boolean formatDate;
    @Setter
    private Boolean tableLogic;

    private String tableName;
    private List<TableField> fieldList;
    private List<TableField> vueFieldList;
    /**
     * 根据配置信息自动生成的信息
     */
    private String packageName;
    private String className;
    private String lowerCamelTableName;
    private Boolean hasId = false;
    private String idType;
    private Boolean hasTableLogic = false;
    private Boolean hasFormatDate = false;

    public TemplateData() {
        this.author = "gongym";
        this.now = DateUtil.now();
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
        if (StrUtil.isNotBlank(parentClass)) {
            this.parentClassName = StrUtil.subAfter(parentClass, ".", true);
        }
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        if (useOrigin) {
            this.packageName = tableName;
            this.className = tableName;
        } else {
            this.packageName = LOWER_UNDERSCORE.to(LOWER_CAMEL, tableName);
            this.className = LOWER_UNDERSCORE.to(UPPER_CAMEL, tableName);
        }

        this.lowerCamelTableName = LOWER_UNDERSCORE.to(LOWER_CAMEL, tableName);
    }

    public void setFieldList(List<TableField> fieldList) {
        if (this.useParent && StrUtil.isNotBlank(this.basePackageName)
                && StrUtil.isNotBlank(this.excludeColumn)) {
            this.excludeColumns = this.excludeColumn.split(";");
        } else {
            this.useParent = false;
        }

        if (this.autoFill && StrUtil.isNotBlank(autoFillColumn)) {
            this.autoFillColumns = this.autoFillColumn.split(";");
        } else {
            this.autoFill = false;
        }

        for (TableField field : fieldList) {
            if (!this.hasId && field.getIsKey()) {
                this.hasId = true;
                this.idType = formatJavaType(field.getDataType());
            }

            if (this.formatDate) {
                if ("datetime".equals(field.getDataType())) {
                    field.setFormatDate(true);
                    this.hasFormatDate = true;
                }
            }

            if (this.tableLogic) {
                if ("del_flag".equals(field.getColumnName())) {
                    field.setTableLogic(true);
                    this.hasTableLogic = true;
                }
            }

            if (this.useParent) {
                for (String column : this.excludeColumns) {
                    if (field.getColumnName().equals(column)) {
                        field.setExclude(true);
                    }
                }
            } else {
                if (this.autoFill) {
                    for (String column : this.autoFillColumns) {
                        if (field.getColumnName().equals(column)) {
                            field.setAutoFill(true);
                            if (column.contains("update")) {
                                field.setAutoFillType("INSERT_UPDATE");
                            } else {
                                field.setAutoFillType("INSERT");
                            }
                        }
                    }
                }
            }

            field.setJavaColumnName(LOWER_UNDERSCORE.to(LOWER_CAMEL, field.getColumnName()));
            field.setJavaDataType(formatJavaType(field.getDataType()));
        }

        this.fieldList = fieldList;
    }

    public void setVueFieldList(List<TableField> fieldList) {
        final List<TableField> vueFieldList = new ArrayList<>();
        for (TableField field : fieldList) {
            if (this.useParent) {
                boolean isExclude = false;
                for (String column : this.excludeColumns) {
                    if (field.getColumnName().equals(column)) {
                        isExclude = true;
                        break;
                    }
                }
                if (!isExclude) {
                    vueFieldList.add(field);
                }
            } else {
                if (this.autoFill) {
                    boolean isAutoFill = false;
                    for (String column : this.autoFillColumns) {
                        if (field.getColumnName().equals(column)) {
                            isAutoFill = true;
                            break;
                        }
                    }
                    if (!isAutoFill) {
                        vueFieldList.add(field);
                    }
                } else {
                    vueFieldList.add(field);
                }
            }
        }

        this.vueFieldList = vueFieldList;
    }

    private String formatJavaType(String mysqlType) {
        final Map<String, String> typeMap = new HashMap<String, String>() {
            {
                put("int", "Integer");
                put("tinyint", "Byte");
                put("smallint", "Short");
                put("mediumint", "Integer");
                put("bigint", "Long");
                put("bit", "Boolean");
                put("double", "Double");
                put("float", "Float");
                put("number", "java.math.BigDecimal");
                put("decimal", "java.math.BigDecimal");
                put("char", "String");
                put("varchar", "String");
                put("date", "java.util.Date");
                put("time", "java.util.Date");
                put("year", "java.util.Date");
                put("timestamp", "java.util.Date");
                put("datetime", "java.util.Date");
                put("tinyblob", "Byte[]");
                put("blob", "Byte[]");
                put("mediumblob", "Byte[]");
                put("longblob", "Byte[]");
                put("tinytext", "String");
                put("text", "String");
                put("mediumtext", "String");
                put("longtext", "String");
                put("binary", "Byte[]");
                put("varbinary", "Byte[]");
            }
        };

        return typeMap.get(mysqlType);
    }
}
