package com.ggymm.mtools.modules.test;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * @author gongym
 * @version 创建时间: 2022-01-04 23:25
 */
public class TestView extends BorderPane {

    public TestView() {
        getStyleClass().add("module-background");
        setCenter(new Label("My first workbench module."));
    }
}