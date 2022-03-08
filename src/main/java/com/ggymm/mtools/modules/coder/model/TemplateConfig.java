package com.ggymm.mtools.modules.coder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gongym
 * @version 创建时间: 2022-03-05 20:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateConfig {
    private String path;
    private String suffix;
    private String templateName;
}
