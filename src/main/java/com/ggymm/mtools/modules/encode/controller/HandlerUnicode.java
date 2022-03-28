package com.ggymm.mtools.modules.encode.controller;

import cn.hutool.core.text.UnicodeUtil;

/**
 * @author gongym
 * @version 创建时间: 2022-03-28 14:01
 */
public class HandlerUnicode implements Handler {

    private HandlerUnicode() {
    }

    public static HandlerUnicode getInstance() {
        return HandlerUnicode.Inner.instance;
    }

    @Override
    public String encode(String input, String charType) {
        return UnicodeUtil.toUnicode(input);
    }

    @Override
    public String decode(String input, String charType) {
        return UnicodeUtil.toString(input);
    }

    private static class Inner {
        private static final HandlerUnicode instance = new HandlerUnicode();
    }
}
