package com.ggymm.mtools.utils;

import org.junit.Test;

/**
 * @author gongym
 * @version 创建时间: 2022-03-08 11:07
 */
public class TestIfElse {
    @Test
    public void test() {
        boolean a = true;
        boolean b = true;

        if (a && b) {
            System.out.println("a和b都正确");
        } else if (a) {
            System.out.println("a正确");
        } else if (b) {
            System.out.println("b正确");
        } else {
            System.out.println("都错误");
        }
    }
}
