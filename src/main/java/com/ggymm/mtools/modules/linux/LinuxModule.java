package com.ggymm.mtools.modules.linux;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.linux.controller.LinuxController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-03-16 14:50
 */
public class LinuxModule extends WorkbenchModule {

    public LinuxModule() {
        super("Linux命令查询", new Image("assets/icon/icons8-linux-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return LinuxController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
