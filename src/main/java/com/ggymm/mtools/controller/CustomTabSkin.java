package com.ggymm.mtools.controller;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * @author gongym
 * @version 创建时间: 2022-01-19 22:34
 */
public class CustomTabSkin extends SkinBase<CustomTab> {

    private final ReadOnlyStringProperty nameProp;

    protected CustomTabSkin(CustomTab control) {
        super(control);

        nameProp = control.nameProperty();

        initLayout();
    }

    private void initLayout() {
        final HBox controlBox = new HBox();
        controlBox.getStyleClass().add("tab-box");

        // 标题
        final Label name = new Label();
        name.getStyleClass().add("tab-name-lbl");
        name.textProperty().bind(this.nameProp);

        // 关闭按钮
        final StackPane closeIcon = new StackPane();
        closeIcon.getStyleClass().add("shape");
        final Button closeBtn = new Button("", closeIcon);
        closeBtn.getStyleClass().addAll("icon", "close-icon");
        closeBtn.setOnAction(e -> getSkinnable().close());

        controlBox.getChildren().addAll(name, closeBtn);
        getChildren().add(controlBox);
    }
}
