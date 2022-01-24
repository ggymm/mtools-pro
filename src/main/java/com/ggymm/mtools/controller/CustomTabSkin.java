package com.ggymm.mtools.controller;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * @author gongym
 * @version 创建时间: 2022-01-19 22:34
 */
public class CustomTabSkin extends SkinBase<CustomTab> {

    private final ReadOnlyStringProperty nameProp;
    private final ReadOnlyObjectProperty<Node> iconProp;
    private HBox controlBox;

    protected CustomTabSkin(CustomTab control) {
        super(control);

        nameProp = control.nameProperty();
        iconProp = control.iconProperty();

        initLayout();
        updateIcon();

        setupValueChangedListeners();
    }

    private void initLayout() {
        controlBox = new HBox();
        controlBox.getStyleClass().add("tab-box");

        // 图标
        Label icon = new Label();

        // 标题
        Label name = new Label();
        name.getStyleClass().add("tab-name-lbl");
        name.textProperty().bind(this.nameProp);

        // 关闭按钮
        StackPane closeIcon = new StackPane();
        closeIcon.getStyleClass().add("shape");
        Button closeBtn = new Button("", closeIcon);
        closeBtn.getStyleClass().addAll("icon", "close-icon");
        closeBtn.setOnAction(e -> getSkinnable().close());

        controlBox.getChildren().addAll(icon, name, closeBtn);
        getChildren().add(controlBox);
    }

    private void updateIcon() {
        Node iconNode = this.iconProp.get();
        ObservableList<Node> children = controlBox.getChildren();
        children.remove(0);
        children.add(0, iconNode);
        iconNode.getStyleClass().add("tab-icon");

        if (iconNode instanceof ImageView) {
            ImageView icon = (ImageView) iconNode;
            icon.setFitWidth(16);
            icon.setFitHeight(16);
        }
    }

    private void setupValueChangedListeners() {
        this.iconProp.addListener((observable, oldIcon, newIcon) -> {
            if (oldIcon != newIcon) {
                updateIcon();
            }
        });
    }
}
