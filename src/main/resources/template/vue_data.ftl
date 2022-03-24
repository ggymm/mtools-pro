[#ftl]
${lowerCamelTableName}: {
    [#list vueFieldList as field]
        [#if !field_has_next]
    ${field.javaColumnName}: null
        [#else]
    ${field.javaColumnName}: null,
        [/#if]
    [/#list]
}