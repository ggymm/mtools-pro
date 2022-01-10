package com.ggymm.mtools.modules.coder;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-01-05 08:40
 */
public class CoderModule extends WorkbenchModule {

    public CoderModule() {
        super("代码生成器", MaterialDesignIcon.HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        try {
            return  CoderController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

