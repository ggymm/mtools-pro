package com.ggymm.mtools.modules;

import com.ggymm.mtools.modules.coder.controller.TableListController;
import com.ggymm.mtools.utils.StyleUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author gongym
 * @version 创建时间: 2022-04-07 11:20
 */
public class GobangTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final URL url = TableListController.class.getResource("/fxml/gobang.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        final Scene scene = new Scene(loader.load());
        StyleUtils.initStyle(scene);

        final Stage stage = new Stage();
        stage.setTitle("五子棋AI");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.showAndWait();
    }
}
