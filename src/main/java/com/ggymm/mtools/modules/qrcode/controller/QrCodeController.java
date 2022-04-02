package com.ggymm.mtools.modules.qrcode.controller;

import cn.hutool.core.util.StrUtil;
import com.ggymm.mtools.utils.QrCodeUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-03-15 09:24
 */
public class QrCodeController implements Initializable {

    public AnchorPane root;

    public Button loadClipboardImage;
    public Button loadLocalImage;
    public Button setQrCode;
    public Button saveImage;

    public ImageView qrCodeImage;

    public TextArea parseResult;

    public static Node getView() throws IOException {
        final URL url = QrCodeController.class.getResource("/fxml/qr-code.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        this.root.heightProperty().addListener((observable, oldValue, newValue) -> setImageSize());
        this.root.widthProperty().addListener((observable, oldValue, newValue) -> setImageSize());
    }

    private void initEvent() {
        this.root.setOnMouseClicked(event -> this.root.requestFocus());

        this.loadClipboardImage.setOnMouseClicked(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final Image image = clipboard.getImage();
            if (image != null) {
                this.qrCodeImage.setImage(image);
                new Thread(() -> {
                    final String result = QrCodeUtils.toDecode(this.qrCodeImage.getImage());
                    Platform.runLater(() -> this.parseResult.setText(result));
                }).start();
            }
        });

        this.loadLocalImage.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择二维码");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.bmp"),
                    new FileChooser.ExtensionFilter("All Files", "*.")
            );
            final File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                final String filepath = file.toURI().toString();
                if (StrUtil.isNotBlank(filepath)) {
                    this.qrCodeImage.setImage(new Image(filepath));
                    new Thread(() -> {
                        final String result = QrCodeUtils.toDecode(this.qrCodeImage.getImage());
                        Platform.runLater(() -> this.parseResult.setText(result));
                    }).start();
                }
            }
        });

        this.setQrCode.setOnMouseClicked(event -> {

        });

        this.saveImage.setOnMouseClicked(event -> {

        });

        this.parseResult.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                final String input = this.parseResult.getText();
                if (StrUtil.isBlank(input)) {
                    return;
                }
                new Thread(() -> {
                    final Image image = QrCodeUtils.toImage(this.parseResult.getText(), (int) this.qrCodeImage.getFitWidth());
                    if (image != null) {
                        Platform.runLater(() -> this.qrCodeImage.setImage(image));
                    }
                }).start();
            }
        });
    }

    private void setImageSize() {
        final double otherHeight = 60.0 + 200.0 + 48.0 * 2 + 20.0 * 2;
        final double currHeight = this.root.getHeight() - otherHeight;

        final double otherWidth = 48.0 * 2 + 20.0 * 2;
        final double currWidth = this.root.getWidth() - otherWidth;

        if (currWidth > currHeight) {
            this.qrCodeImage.setFitWidth(currHeight);
            this.qrCodeImage.setFitHeight(currHeight);
        } else {
            this.qrCodeImage.setFitWidth(currWidth);
            this.qrCodeImage.setFitHeight(currWidth);
        }
    }
}
