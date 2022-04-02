package com.ggymm.mtools.modules.tools.controller;

import com.ggymm.mtools.modules.qrcode.controller.QrCodeController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-03-29 11:14
 */
public class ToolsController implements Initializable {

    public static Node getView() throws IOException {
        final URL url = QrCodeController.class.getResource("/fxml/tools.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
