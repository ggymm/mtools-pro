package com.ggymm.mtools.modules.emoji;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.ggymm.mtools.modules.emoji.controller.EmojiController;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author gongym
 * @version 创建时间: 2022-04-02 18:26
 */
public class EmojiModule extends WorkbenchModule {

    public EmojiModule() {
        super("表情包搜索", new Image("assets/icon/icons8-happy-50.png"));
    }

    @Override
    public Node activate() {
        try {
            return EmojiController.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
