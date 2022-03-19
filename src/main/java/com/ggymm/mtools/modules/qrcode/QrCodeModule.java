package com.ggymm.mtools.modules.qrcode;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.qrcode.controller.QrCodeController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-03-14 18:31
 */
public class QrCodeModule extends WorkbenchModule {

    public QrCodeModule() {
        super("二维码工具", new Image("assets/icon/icons8-qr-code-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return QrCodeController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
