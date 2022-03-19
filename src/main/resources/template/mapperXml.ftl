[#ftl]
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
[#if useTableAsPackage]
<mapper namespace="${basePackageName}.${packageName}.mapper.${className}Mapper">
</mapper>
[#else]
<mapper namespace="${basePackageName}.mapper.${className}Mapper">
</mapper>
[/#if]
