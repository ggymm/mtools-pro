package com.ggymm.mtools.modules.encode;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.encode.controller.EncodeController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-01-25 22:09
 */
public class EncodeModule extends WorkbenchModule {

    public EncodeModule() {
        super("编解码", new Image("assets/icon/icons8-product-management-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return EncodeController.getView();
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
    public void deactivate(){
    }
}
