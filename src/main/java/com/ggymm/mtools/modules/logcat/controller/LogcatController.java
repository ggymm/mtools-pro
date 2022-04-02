package com.ggymm.mtools.modules.logcat.controller;

import cn.hutool.core.util.StrUtil;
import com.ggymm.mtools.modules.coder.controller.CoderController;
import com.ggymm.mtools.utils.CommandUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-02-01 16:28
 */
public class LogcatController implements Initializable {

    public AnchorPane root;

    public ComboBox<String> deviceList;
    public Button refreshDevice;
    public Button adbRestart;
    public Button saveConsole;
    public Button cleanConsole;

    public ComboBox<String> levels;

    public Button startOutput;
    public Button stopOutput;

    public TextField filter;

    public ListView<String> console;

    private String currentDevice;
    private String currentLevel;
    private Process showLogProcess;

    public static Node getView() throws IOException {
        final URL url = CoderController.class.getResource("/fxml/logcat.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        this.root.heightProperty().addListener((observable, oldValue, newValue) -> {
            final double otherHeight = 48.0 + 24.0 * 2 + 32.0 * 2;
            this.console.setMinHeight(newValue.doubleValue() - otherHeight);
        });
    }

    private void initEvent() {
        final String adbPath = "lib/adb/adb.exe ";

        // 监听设备选择
        this.deviceList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.currentDevice = " " + newValue + " ");
        // 监听进程选择
        this.levels.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.currentLevel = newValue);

        // 刷新设备列表
        this.refreshDevice.setOnMouseClicked(event -> {
            this.deviceList.getItems().clear();
            new Thread(() -> {
                final List<String> result = new ArrayList<>();
                try {
                    final Process process = Runtime.getRuntime().exec(adbPath + "devices");
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if ("List of devices attached".equals(line) || StrUtil.isBlank(line)) {
                            continue;
                        }
                        final String newLine = StrUtil.replace(line, "\tdevice", "", true);
                        result.add(newLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> this.deviceList.getItems().addAll(result));
            }).start();
        });

        // 重启adb服务
        this.adbRestart.setOnMouseClicked(event -> {
            // 需要清空设备列表
            this.deviceList.getItems().clear();
            CommandUtils.asyncRun(adbPath + "kill-server", adbPath + "start-server");
        });

        // 启动抓取日志
        this.startOutput.setOnMouseClicked(event -> {
            if (showLogProcess != null && showLogProcess.isAlive()) {
                showLogProcess.destroyForcibly();
            }
            final String filter = this.filter.getText();
            new Thread(() -> {
                try {
                    final StringBuilder command = new StringBuilder(adbPath);

                    // 选择设备
                    if (StrUtil.isNotBlank(currentDevice)) {
                        Runtime.getRuntime().exec(adbPath + "-s" + currentDevice + "logcat -c");
                        command.append("-s").append(currentDevice).append("logcat -v time");
                    } else {
                        Runtime.getRuntime().exec(adbPath + "logcat -c");
                        command.append("logcat -v time");
                    }

                    // 设置日志输出级别
                    if (StrUtil.isNotBlank(this.currentLevel)) {
                        final char level = this.currentLevel.toUpperCase().charAt(0);
                        command.append(" *:").append(level);
                    }

                    showLogProcess = Runtime.getRuntime().exec(command.toString());
                    try (InputStreamReader reader = new InputStreamReader(showLogProcess.getInputStream(), StandardCharsets.UTF_8)) {
                        final BufferedReader bufferedReader = new BufferedReader(reader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            // 按照规则进行筛选
                            // 添加到页面
                            final String finalLine = line;
                            Platform.runLater(() -> console.getItems().add(finalLine));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // 停止抓取日志
        this.stopOutput.setOnMouseClicked(event -> {
            if (showLogProcess != null && showLogProcess.isAlive()) {
                showLogProcess.destroyForcibly();
            }
        });

        // 保存日志
        this.saveConsole.setOnMouseClicked(event -> {
            // 打开选择保存目录
        });

        // 清空日志
        this.cleanConsole.setOnMouseClicked(event -> this.console.getItems().clear());
    }
}
