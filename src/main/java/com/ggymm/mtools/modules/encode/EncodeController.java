package com.ggymm.mtools.modules.encode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.URLUtil;
import com.ggymm.mtools.modules.coder.CoderController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
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
        URL url = CoderController.class.getResource("/fxml/encode.fxml");
        FXMLLoader fXMLLoader = new FXMLLoader(url);
        return fXMLLoader.load();
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

        this.encode.setOnMouseClicked((event) -> {
            // 编码
            String output = "";
            switch (this.currentType) {
                case "Base64":
                    output = Base64.encode(this.input.getText(), this.currentCharType);
                    break;
                case "Unicode":
                    output = UnicodeUtil.toUnicode(this.input.getText());
                    break;
                case "URL":
                    output = URLUtil.encode((this.input.getText()));
                    break;
                case "UTF-8":
            }
            this.output.setText(output);
        });
        this.decode.setOnMouseClicked((event) -> {
            // 解码
            String input = "";
            switch (this.currentType) {
                case "Base64":
                    input = Base64.decodeStr(this.output.getText(), this.currentCharType);
                    break;
                case "Unicode":
                    input = UnicodeUtil.toString(this.output.getText());
                    break;
                case "URL":
                    input = URLUtil.decode(this.output.getText(), this.currentCharType);
                case "UTF-8":
            }
            this.input.setText(input);
        });
    }
}
