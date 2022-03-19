package com.ggymm.mtools.modules.download;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.download.controller.DownloadController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-03-09 11:32
 */
public class DownloadModule extends WorkbenchModule {

    public DownloadModule() {
        super("视频下载", new Image("assets/icon/icons8-downloads-folder-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return DownloadController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
