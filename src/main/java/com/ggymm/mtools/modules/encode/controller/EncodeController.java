package com.ggymm.mtools.modules.encode.controller;

import cn.hutool.core.util.StrUtil;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-01-25 22:09
 */
public class EncodeController implements Initializable {

    public TextArea input;
    public TextArea output;

    public ComboBox<String> encodeTypeList;
    public ComboBox<String> characterTypeList;

    public Button encode;
    public Button decode;

    private String currentType;
    private String currentCharType;

    private Map<String, Handler> handlerMap = new HashMap<String, Handler>() {
        {
            put("Base64", HandlerBase64.getInstance());
            put("Unicode", HandlerUnicode.getInstance());
            put("URL", HandlerURL.getInstance());
            put("UTF-8", null);
        }
    };

    public static Node getView() throws IOException {
        final URL url = EncodeController.class.getResource("/fxml/encode.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        this.encodeTypeList.getItems().addAll("Base64", "Unicode", "URL", "UTF-8");
        this.encodeTypeList.setValue("Base64");
        this.currentType = "Base64";

        this.characterTypeList.getItems().addAll("UTF-8", "GBK");
        this.characterTypeList.setValue("UTF-8");
        this.currentCharType = "UTF-8";
    }

    private void initEvent() {
        this.encodeTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.currentType = newValue);
        this.characterTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.currentCharType = newValue);

        // 编码
        this.encode.setOnMouseClicked(event -> {
            String input = this.input.getText();
            if (StrUtil.isBlank(input)) {
                return;
            }
            final Handler handler = handlerMap.get(this.currentType);
            if (handler != null) {
                this.output.setText(handler.encode(input, this.currentCharType));
            }
        });

        // 解码
        this.decode.setOnMouseClicked(event -> {
            String input = this.input.getText();
            if (StrUtil.isBlank(input)) {
                return;
            }
            final Handler handler = handlerMap.get(this.currentType);
            if (handler != null) {
                this.output.setText(handler.decode(input, this.currentCharType));
            }
        });
    }
}
