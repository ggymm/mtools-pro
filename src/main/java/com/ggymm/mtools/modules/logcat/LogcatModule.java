package com.ggymm.mtools.modules.logcat;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.logcat.controller.LogcatController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-02-01 16:28
 */
public class LogcatModule extends WorkbenchModule {

    public LogcatModule() {
        super("Logcat查看", new Image("assets/icon/icons8-android-os-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return LogcatController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
