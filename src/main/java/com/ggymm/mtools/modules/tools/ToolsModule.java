package com.ggymm.mtools.modules.tools;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.tools.controller.ToolsController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-03-28 19:46
 */
public class ToolsModule extends WorkbenchModule {

    public ToolsModule() {
        super("常用小工具", new Image("assets/icon/icons8-maintenance-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return ToolsController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
