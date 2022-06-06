package com.ggymm.mtools.modules.devops.controller;

import com.ggymm.mtools.modules.download.controller.DownloadController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-05-12 09:06
 */
public class DevopsController implements Initializable {

    public static Node getView() throws IOException {
        final URL url = DownloadController.class.getResource("/fxml/devops.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
