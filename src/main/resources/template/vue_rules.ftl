[#ftl]
${lowerCamelTableName}Rules: {
    [#list vueFieldList as field]
        [#if !field.isKey]
            [#if !field_has_next]
    ${field.javaColumnName}: [{required: true, message: '请填写${field.columnComment}', trigger: 'blur'}]
            [#else]
    ${field.javaColumnName}: [{required: true, message: '请填写${field.columnComment}', trigger: 'blur'}],
            [/#if]
        [/#if]
    [/#list]
}