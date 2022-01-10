package com.ggymm.mtools;

import com.dlsc.workbenchfx.Workbench;
import com.ggymm.mtools.modules.coder.CoderModule;
import com.ggymm.mtools.modules.test.TestModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import static com.ggymm.mtools.toolkit.IconUtils.IconType.FontAwesome;
import static com.ggymm.mtools.toolkit.IconUtils.IconType.MaterialDesign;
import static com.ggymm.mtools.toolkit.IconUtils.createIcon;

/**
 * @author gongym
 * @version 创建时间: 2022-01-04 13:21
 */
public class ToolApp extends Application {

    private Workbench workbench;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(initWorkbench());

        stage.setTitle("工具箱");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setMinWidth(600);
        stage.setHeight(600);
        stage.setMinHeight(400);
        stage.show();
        stage.centerOnScreen();

        initStyle(scene);
    }

    private Workbench initWorkbench() {
        MenuItem[] menuItems = initMenu();

        workbench = Workbench.builder(
                        new CoderModule(),
                        new TestModule()
                ).navigationDrawerItems(menuItems)
                .modulesPerPage(6)
                .build();

        return workbench;
    }

    private MenuItem[] initMenu() {

        MenuItem coder = new MenuItem("代码生成器", createIcon(FontAwesome, FontAwesomeIcon.FILE_CODE_ALT));
        coder.setOnAction(event -> workbench.hideNavigationDrawer());

        MenuItem setting = new MenuItem("应用设置", createIcon(MaterialDesign, MaterialDesignIcon.SETTINGS));
        setting.setOnAction(event -> workbench.hideNavigationDrawer());

        return new MenuItem[]{coder, setting};
    }

    private void initStyle(Scene scene) {
        // 设置自定义样式
        String[] styles = new String[]{
                "assets/css/main.css",
                "assets/css/button.css",

                "assets/css/input.css",
                "assets/css/select.css",

                "assets/css/cover.css",
        };
        scene.getStylesheets().addAll(styles);
    }
}
