[#ftl]
[#if useTableAsPackage]
package ${basePackageName}.${packageName}.entity;
[#else]
package ${basePackageName}.entity;
[/#if]

[#if hasId && hasTableLogic]
import com.baomidou.mybatisplus.annotation.*;
[#elseif hasId]
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
[#elseif hasTableLogic]
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
[#else]
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
[/#if]
[#if hasFormatDate]
import com.fasterxml.jackson.annotation.JsonFormat;
[/#if]
[#if useParent]
import ${parentClass};
[/#if]
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
[#if hasFormatDate]
import org.springframework.format.annotation.DateTimeFormat;
[/#if]

/**
 * @author ${author}
 * @version 创建时间: ${now}
 */
@Setter
@Getter
@TableName("${tableName}")
@Accessors(chain = true)
public class ${className} [#if useParent]extends ${parentClassName} [/#if]{
[#list fieldList as field]
    [#if field.isKey]

    /**
     * ${field.columnComment}
     */
        [#if field.isAuto]
    @TableId(value = "${field.columnName}", type = IdType.AUTO)
        [#else]
    @TableId(value = "${field.columnName}")
        [/#if]
    private ${field.javaDataType} [#if useOrigin]${field.columnName}[#else]${field.javaColumnName}[/#if];
    [/#if]
    [#if useParent && !field.exclude && !field.isKey]

    /**
     * ${field.columnComment}
     */
        [#if field.tableLogic]
    @TableLogic
        [/#if]
    @TableField(value = "${field.columnName}")
        [#if field.formatDate]
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        [/#if]
    private ${field.javaDataType} [#if useOrigin]${field.columnName}[#else]${field.javaColumnName}[/#if];
    [#elseif !useParent && !field.isKey]

    /**
     * ${field.columnComment}
     */
        [#if field.tableLogic]
    @TableLogic
        [/#if]
        [#if field.autoFill]
    @TableField(value = "${field.columnName}", fill = FieldFill.${field.autoFillType})
        [#else]
    @TableField(value = "${field.columnName}")
        [/#if]
        [#if field.formatDate]
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        [/#if]
    private ${field.javaDataType} [#if useOrigin]${field.columnName}[#else]${field.javaColumnName}[/#if];
    [/#if]
[/#list]
}