package com.ggymm.mtools.modules.qrcode.controller;

import cn.hutool.core.util.StrUtil;
import com.ggymm.mtools.modules.common.toast.ToastUtils;
import com.ggymm.mtools.utils.QrCodeUtils;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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

import javax.imageio.ImageIO;
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

    private JFXSnackbar snackbar;

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
        this.snackbar = new JFXSnackbar(root);

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
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择二维码");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
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
            final Image image = this.qrCodeImage.getImage();
            if (image == null) {
                ToastUtils.error(this.snackbar, "图片为空, 不能保存");
                return;
            }
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存二维码");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                    new FileChooser.ExtensionFilter("BPM", "*.bmp"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif")
            );
            final File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    String[] fileType = file.getPath().split("\\.");
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                            fileType[fileType.length - 1],
                            file);
                    ToastUtils.info(this.snackbar, "图片保存成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        final double otherWidth = 48.0 * 2 + 20.0 * 2;
        final double currentWidth = this.root.getWidth() - otherWidth;

        final double otherHeight = 60.0 + 200.0 + 48.0 * 2 + 20.0 * 2;
        final double currentHeight = this.root.getHeight() - otherHeight;

        if (currentWidth > currentHeight) {
            this.qrCodeImage.setFitWidth(currentHeight);
            this.qrCodeImage.setFitHeight(currentHeight);
        } else {
            this.qrCodeImage.setFitWidth(currentWidth);
            this.qrCodeImage.setFitHeight(currentWidth);
        }
    }
}
