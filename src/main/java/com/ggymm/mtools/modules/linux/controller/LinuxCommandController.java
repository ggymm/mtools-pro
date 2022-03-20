package com.ggymm.mtools.modules.linux.controller;

import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongym
 * @version 创建时间: 2022-03-16 17:39
 */
public class LinuxCommandController implements Initializable {

    public AnchorPane root;
    public TextField keyword;
    public Button search;
    public ListView<CommandItem> commandList;

    public List<CommandItem> commandItemList = new ArrayList<>();
    public Map<String, Stage> commandWebviewMap = new ConcurrentHashMap<>();

    public static Node getView() throws IOException {
        final URL url = LinuxCommandController.class.getResource("/fxml/linux-command.fxml");
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
            double otherHeight = 100.0;
            this.commandList.setMinHeight(newValue.doubleValue() - otherHeight);
        });

        new Thread(() -> {
            final Set<String> commands = LinuxCommand.commands.keySet();
            for (String command : commands) {
                final Object commandInfo = LinuxCommand.commands.get(command);
                final Map<?, ?> commandInfoMap = (Map<?, ?>) commandInfo;
                this.commandItemList.add(new CommandItem(
                        String.valueOf(commandInfoMap.get("n")),
                        String.valueOf(commandInfoMap.get("d"))));
            }

            Platform.runLater(() -> this.commandList.getItems().addAll(this.commandItemList));
        }).start();
    }

    private void initEvent() {
        // 搜索
        this.search.setOnMouseClicked(event -> {
            final String keyword = this.keyword.getText();
            if (StrUtil.isBlank(keyword)) {
                this.commandList.getItems().clear();
                this.commandList.getItems().addAll(this.commandItemList);
                return;
            }
            new Thread(() -> {
                final List<CommandItem> resultItemList = new ArrayList<>();
                for (CommandItem commandItem : commandItemList) {
                    if (commandItem.name.contains(keyword) || commandItem.description.contains(keyword)) {
                        resultItemList.add(commandItem);
                    }
                }

                Platform.runLater(() -> {
                    this.commandList.getItems().clear();
                    this.commandList.getItems().addAll(resultItemList);
                });
            }).start();
        });

        // 监听点击事件
        this.commandList.setCellFactory(param -> {
            TextFieldListCell<CommandItem> cell = new TextFieldListCell<>();
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (event.getTarget() instanceof StackPane || cell.getItem() == null) {
                    return;
                }
                String itemName = cell.getItem().name;
                if (this.commandWebviewMap.containsKey(itemName)) {
                    this.commandWebviewMap.get(itemName).toFront();
                    return;
                }

                // 创建stage
                final Stage stage = new Stage();

                // 创建webview
                final WebView webView = new WebView();
                final Scene scene = new Scene(webView, 640, 640);
                stage.setTitle("Linux命令");
                stage.setScene(scene);

                // 渲染页面
                final WebEngine engine = webView.getEngine();
                final URL markdown = getClass().getResource("/commands/linux/html/" + itemName + ".md.html");
                if (markdown != null) {
                    engine.load(markdown.toExternalForm());
                }
                engine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        stage.show();
                        this.commandWebviewMap.put(itemName, stage);
                    }
                });
            });
            return cell;
        });
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class CommandItem {
        private String name;
        private String description;

        @Override
        public String toString() {
            return name + "          " + description;
        }
    }
}
