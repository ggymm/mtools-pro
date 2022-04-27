package com.ggymm.mtools.modules.emoji.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-04-02 18:30
 */
public class EmojiController implements Initializable {

    public static Node getView() throws IOException {
        final URL url = EmojiController.class.getResource("/fxml/emoji.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
