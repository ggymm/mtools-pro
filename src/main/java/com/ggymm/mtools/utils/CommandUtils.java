package com.ggymm.mtools.utils;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-02-17 08:33
 */
public class CommandUtils {

    public static void asyncRun(String... cmdArray) {
        new Thread(() -> {
            try {
                for (String cmd : cmdArray) {
                    Runtime.getRuntime().exec(cmd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
