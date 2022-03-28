package com.ggymm.mtools.modules.encode.controller;

import cn.hutool.core.util.URLUtil;

import java.nio.charset.Charset;

/**
 * @author gongym
 * @version 创建时间: 2022-03-28 14:03
 */
public class HandlerURL implements Handler {

    private HandlerURL() {
    }

    public static HandlerURL getInstance() {
        return HandlerURL.Inner.instance;
    }

    @Override
    public String encode(String input, String charType) {
        return URLUtil.encode(input, Charset.forName(charType));
    }

    @Override
    public String decode(String input, String charType) {
        return URLUtil.decode(input, charType);
    }

    private static class Inner {
        private static final HandlerURL instance = new HandlerURL();
    }
}
