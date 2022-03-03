package com.ggymm.mtools.modules.document;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * @author gongym
 * @version 创建时间: 2022-02-25 09:54
 */
public class DocumentModule extends WorkbenchModule {

    public DocumentModule() {
        super("文档生成", new Image("assets/icon/coder.png"));
    }

    @Override
    public Node activate() {
        return null;
    }
}
