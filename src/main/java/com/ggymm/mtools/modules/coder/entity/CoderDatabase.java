package com.ggymm.mtools.modules.coder.entity;

import lombok.Data;

/**
 * @author gongym
 * @version 创建时间: 2022-01-12 18:00
 */
@Data
public class CoderDatabase {
    private Long id;
    private String showName;
    private String driver;
    private String host;
    private String port;
    private String name;
    private String username;
    private String password;
}
