package com.ggymm.mtools.modules.coder;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.coder.controller.CoderController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-01-05 08:40
 */
public class CoderModule extends WorkbenchModule {

    public CoderModule() {
        super("代码生成", new Image("assets/icon/icons8-code-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return CoderController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init(Workbench workbench) {
        super.init(workbench);
    }

    @Override
    public void deactivate() {

    }
}

