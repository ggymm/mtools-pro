package com.ggymm.mtools.controller;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.scene.control.Skin;

/**
 * @author gongym
 * @version 创建时间: 2022-01-19 22:34
 */
public class CustomTab extends Tab {

    public CustomTab(Workbench workbench) {
        super(workbench);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CustomTabSkin(this);
    }
}
