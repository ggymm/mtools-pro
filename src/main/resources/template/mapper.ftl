[#ftl]
[#if useTableAsPackage]
package ${basePackageName}.${packageName}.mapper;
[#else]
package ${basePackageName}.mapper;
[/#if]

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
[#if useTableAsPackage]
import ${basePackageName}.${packageName}.entity.${className};
[#else]
import ${basePackageName}.entity.${className};
[/#if]
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ${author}
 * @version 创建时间: ${now}
 */
@Mapper
public interface ${className}Mapper extends BaseMapper<${className}> {
}
