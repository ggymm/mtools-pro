[#ftl]
[#if useTableAsPackage]
package ${basePackageName}.${packageName}.controller;

import ${basePackageName}.${packageName}.entity.${className};
import ${basePackageName}.${packageName}.service.${className}Service;
[#else]
package ${basePackageName}.controller;

import ${basePackageName}.entity.${className};
import ${basePackageName}.service.${className}Service;
[/#if]
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ${author}
 * @version 创建时间: ${now}
 */
@RestController
@RequestMapping("${lowerCamelTableName}")
public class ${className}Controller {

    @Resource
    final ${className}Service ${lowerCamelTableName}Service;

    @GetMapping(value = "getPage", produces = "application/json;charset=UTF-8")
    public String getPage(HttpServletRequest request, ${className} ${lowerCamelTableName},
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ${lowerCamelTableName}Service.getPage(${lowerCamelTableName}, request.getParameterMap(), page, size);
    }

    @PostMapping(value = "create", produces = "application/json;charset=UTF-8")
    public String create(@RequestBody ${className} ${lowerCamelTableName}) {
        return ${lowerCamelTableName}Service.create(${lowerCamelTableName});
    }

[#if hasId]
    @GetMapping(value = "get", produces = "application/json;charset=UTF-8")
    public String get(@RequestParam(name = "id") ${idType} id) {
        return ${lowerCamelTableName}Service.get(id);
    }

    @PostMapping(value = "update", produces = "application/json;charset=UTF-8")
    public String update(@RequestBody ${className} ${lowerCamelTableName}) {
        return ${lowerCamelTableName}Service.update(${lowerCamelTableName});
    }

    @PostMapping(value = "delete", produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody ${idType} id) {
        return ${lowerCamelTableName}Service.delete(id);
    }
[/#if]
}
