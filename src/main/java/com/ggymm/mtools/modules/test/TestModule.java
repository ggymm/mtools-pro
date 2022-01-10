package com.ggymm.mtools.modules.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

/**
 * @author gongym
 * @version 创建时间: 2022-01-04 23:25
 */
public class TestModule extends WorkbenchModule {

    public TestModule() {
        super("测试", MaterialDesignIcon.HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return new TestView();
    }
}
