package com.ggymm.mtools.modules.linux.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author gongym
 * @version 创建时间: 2022-03-20 18:39
 */
public class LinuxCommand {

    public static JSONObject commands;

    static {
        final URL resource = LinuxCommand.class.getResource("/commands/linux/index.json");
        if (resource != null) {
            final String content = FileUtil.readString(resource, StandardCharsets.UTF_8);
            commands = JSONUtil.parseObj(content, true);
        }
    }
}
