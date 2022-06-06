package com.ggymm.mtools.modules.devops;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.devops.controller.DevopsController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-05-09 09:26
 */
public class DevopsModule extends WorkbenchModule {

    public DevopsModule() {
        super("运维工具", new Image("assets/icon/icons8-devops-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return DevopsController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
