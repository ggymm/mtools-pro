package com.ggymm.mtools.modules.encode;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-01-25 22:09
 */
public class EncodeModule extends WorkbenchModule {

    private final Stage stage;

    public EncodeModule(Stage stage) {
        super("编解码", new Image("assets/icon/encode.png"));
        this.stage = stage;
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
        System.out.println(stage);
    }

    @Override
    public void deactivate(){

    }
}
