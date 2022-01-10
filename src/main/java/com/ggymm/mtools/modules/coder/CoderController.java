package com.ggymm.mtools.modules.coder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-01-05 17:58
 */
public class CoderController extends CoderView {

    public static Node getView() throws IOException {
        URL url = CoderController.class.getResource("/fxml/coder.fxml");
        FXMLLoader fXMLLoader = new FXMLLoader(url);
        return fXMLLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {

    }

    private void initEvent() {

    }
}
