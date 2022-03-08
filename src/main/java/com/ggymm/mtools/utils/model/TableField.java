package com.ggymm.mtools.utils.model;

import lombok.Data;

/**
 * @author gongym
 * @version 创建时间: 2022-03-05 21:08
 */
@Data
public class TableField {
    private String columnName;
    private String columnDefault;
    private String dataType;
    private String columnComment;
    private Boolean isKey;
    private String numericPrecision;
    private String numericScale;
    private String characterMaximumLength;
    private String columnType;
    private Boolean isAuto;

    private Boolean exclude = false;
    private Boolean autoFill = false;
    private String autoFillType;
    private Boolean tableLogic = false;
    private Boolean formatDate = false;
    private String javaColumnName;
    private String javaDataType;
}
