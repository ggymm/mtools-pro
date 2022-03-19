package com.ggymm.mtools.modules.linux.controller;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-03-16 17:39
 */
public class LinuxController implements Initializable {

    public Button test;

    public static Node getView() throws IOException {
        final URL url = LinuxController.class.getResource("/fxml/linux.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {

    }

    private void initEvent() {
        this.test.setOnMouseClicked((event) -> {
            // 创建stage
            final Stage stage = new Stage();

            // 创建webview
            final WebView webView = new WebView();
            final Scene scene = new Scene(webView, 700, 700);
            stage.setTitle("选择表");
            stage.setScene(scene);

            // 渲染页面
            final WebEngine engine = webView.getEngine();
            final URL markdown = getClass().getResource("/commands/linux/html/ab.md.html");
            if (markdown != null) {
                engine.load(markdown.toExternalForm());
            }
            engine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    stage.show();
                }
            });
        });
    }
}
