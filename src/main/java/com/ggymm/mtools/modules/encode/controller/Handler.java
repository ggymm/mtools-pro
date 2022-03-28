package com.ggymm.mtools.modules.encode.controller;

public interface Handler {
    String encode(String input, String charType);
    String decode(String input, String charType);
}
