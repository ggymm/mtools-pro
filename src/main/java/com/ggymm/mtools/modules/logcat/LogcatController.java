package com.ggymm.mtools.modules.logcat;

import com.ggymm.mtools.modules.coder.CoderController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-02-01 16:28
 */
public class LogcatController implements Initializable {

    public AnchorPane root;

    public ComboBox<String> deviceList;
    public Button adbStart;
    public Button adbStop;

    public Button refreshDevice;
    public ComboBox<String> threadList;

    public Button refreshThread;
    public TextField filter;

    public ListView<String> console;

    public static Node getView() throws IOException {
        URL url = CoderController.class.getResource("/fxml/logcat.fxml");
        FXMLLoader fXMLLoader = new FXMLLoader(url);
        return fXMLLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        root.heightProperty().addListener((observable, oldValue, newValue) -> {
            double otherHeight = 48.0 + 8.0 * 3 + 48.0 + 48.0 + 48.0;
            console.setMinHeight(newValue.doubleValue() - otherHeight);
        });
    }

    private void initEvent() {
        this.refreshThread.setOnMouseClicked((event) -> {
            try {
                Process process = Runtime.getRuntime().exec("C:\\Product\\mtools-pro\\lib\\adb\\adb.exe logcat -v time");
                new Thread(() -> {

                    try (InputStreamReader reader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
                        final BufferedReader bufferedReader = new BufferedReader(reader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            String finalLine = line;
                            Platform.runLater(() -> console.getItems().add(finalLine));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }
}
