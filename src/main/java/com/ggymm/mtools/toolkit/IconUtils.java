package com.ggymm.mtools.toolkit;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Node;

/**
 * @author gongym
 * @version 创建时间: 2022-01-05 16:10
 */
public class IconUtils {

    public static Node createIcon(IconType type, GlyphIcons icon) {
        switch (type) {
            case FontAwesome:
                return new FontAwesomeIconView((FontAwesomeIcon) icon);
            case MaterialDesign:
                return new MaterialDesignIconView((MaterialDesignIcon) icon);
            default:
                return null;
        }
    }

    public enum IconType {
        FontAwesome,
        MaterialDesign,
    }
}
