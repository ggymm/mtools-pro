package com.ggymm.mtools.modules.download.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-03-09 12:54
 */
public class DownloadController implements Initializable {

    public static Node getView() throws IOException {
        final URL url = DownloadController.class.getResource("/fxml/download.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
