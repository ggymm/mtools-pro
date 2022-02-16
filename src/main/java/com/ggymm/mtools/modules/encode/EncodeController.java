package com.ggymm.mtools.modules.encode;

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
        this.characterTypeList.getItems().addAll("UTF-8", "GBK");
    }

    private void initEvent() {
        this.encode.setOnMouseClicked((event) -> {
            // 编码
        });
        this.decode.setOnMouseClicked((event) -> {
            // 解码
        });
    }
}
