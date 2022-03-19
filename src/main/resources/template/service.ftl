[#ftl]
[#if useTableAsPackage]
package ${basePackageName}.${packageName}.service;
[#else]
package ${basePackageName}.service;
[/#if]

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
[#if useTableAsPackage]
import ${basePackageName}.${packageName}.entity.${className};
import ${basePackageName}.${packageName}.mapper.${className}Mapper;
[#else]
import ${basePackageName}.entity.${className};
import ${basePackageName}.mapper.${className}Mapper;
[/#if]
import com.ninelock.core.response.ResultMsg;
import com.ninelock.core.toolkit.QueryGenerator;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ${author}
 * @version 创建时间: ${now}
 */
@Service
public class ${className}Service extends ServiceImpl<${className}Mapper, ${className}> {
    /**
     * 查询分页列表数据
     * @param ${lowerCamelTableName} 实体类对象，用于传递字段查询条件
     * @param parameterMap 其他请求参数，用于传递其他查询条件（比如LIKE，ORDER等）
     * @param page 页号
     * @param size 页大小
     * @return 统一返回值封装
     */
    public String getPage(${className} ${lowerCamelTableName}, Map<String, String[]> parameterMap, Integer page, Integer size) {
        QueryWrapper<${className}> queryWrapper = QueryGenerator.initQueryWrapper(${lowerCamelTableName}, parameterMap);
        Page<${className}> pageable = new Page<>(page, size);
        return ResultMsg.success(this.page(pageable, queryWrapper));
    }

    /**
     * 保存数据
     * @param ${className} 实体类对象
     * @return 统一返回值封装
     */
    public String create(${className} ${lowerCamelTableName}) {
        boolean save = this.save(${lowerCamelTableName});
        if (save) {
            return ResultMsg.success();
        } else {
            return ResultMsg.fail("保存失败");
        }
    }

[#if hasId]
    /**
     * 根据ID获取数据
     * @param id ID值
     * @return 统一返回值封装
     */
    public String get(${idType} id) {
        return ResultMsg.success(this.getById(id));
    }

    /**
     * 根据ID更新数据
     * @param ${className} 实体类对象
     * @return 统一返回值封装
     */
    public String update(${className} ${lowerCamelTableName}) {
        if (this.updateById(${lowerCamelTableName})) {
            return ResultMsg.success();
        } else {
            return ResultMsg.fail("更新失败");
        }
    }

    /**
     * 根据ID删除数据
     * @param id ID值
     * @return 统一返回值封装
     */
    public String delete(${idType} id) {
        if (this.removeById(id)) {
            return ResultMsg.success();
        } else {
            return ResultMsg.fail("删除失败");
        }
    }
[/#if]
}
