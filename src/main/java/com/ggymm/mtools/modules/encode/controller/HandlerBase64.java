package com.ggymm.mtools.modules.encode.controller;

import cn.hutool.core.codec.Base64;

/**
 * @author gongym
 * @version 创建时间: 2022-03-28 13:59
 */
public class HandlerBase64 implements Handler {

    private HandlerBase64() {
    }

    public static HandlerBase64 getInstance() {
        return Inner.instance;
    }

    @Override
    public String encode(String input, String charType) {
        return Base64.encode(input, charType);
    }

    @Override
    public String decode(String input, String charType) {
        return Base64.decodeStr(input, charType);
    }

    private static class Inner {
        private static final HandlerBase64 instance = new HandlerBase64();
    }
}
