package com.ggymm.mtools.modules.gobang;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.gobang.controller.GobangController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-04-06 09:39
 */
public class GobangModule extends WorkbenchModule {

    public GobangModule() {
        super("五子棋AI", new Image("assets/icon/gobang.png"));
    }

    @Override
    public Node activate() {
        try {
            return GobangController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

