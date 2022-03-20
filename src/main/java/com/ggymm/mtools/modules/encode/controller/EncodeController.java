package com.ggymm.mtools.modules.encode.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.ggymm.mtools.modules.coder.controller.CoderController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
        this.characterTypeList.getItems().addAll("UTF-8", "GBK");
        this.characterTypeList.setValue("UTF-8");
    }

    private void initEvent() {
        this.encodeTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.currentType = newValue);
        this.characterTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.currentCharType = newValue);

        this.encode.setOnMouseClicked(event -> {
            // 编码
            String input = this.input.getText();
            if (StrUtil.isBlank(input)) {
                return;
            }
            String output = "";
            switch (this.currentType) {
                case "Base64":
                    output = Base64.encode(input, this.currentCharType);
                    break;
                case "Unicode":
                    output = UnicodeUtil.toUnicode(input);
                    break;
                case "URL":
                    output = URLUtil.encode(input, Charset.forName(this.currentCharType));
                    break;
                case "UTF-8":
            }
            this.output.setText(output);
        });
        this.decode.setOnMouseClicked(event -> {
            // 解码
            String output = this.output.getText();
            if (StrUtil.isBlank(output)) {
                return;
            }
            String input = "";
            switch (this.currentType) {
                case "Base64":
                    input = Base64.decodeStr(output, this.currentCharType);
                    break;
                case "Unicode":
                    input = UnicodeUtil.toString(output);
                    break;
                case "URL":
                    input = URLUtil.decode(output, this.currentCharType);
                case "UTF-8":
            }
            this.input.setText(input);
        });
    }
}
