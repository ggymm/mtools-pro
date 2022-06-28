package com.ggymm.mtools;

import com.dlsc.workbenchfx.Workbench;
import com.ggymm.mtools.controller.CustomTab;
import com.ggymm.mtools.modules.coder.CoderModule;
import com.ggymm.mtools.modules.devops.DevopsModule;
import com.ggymm.mtools.modules.download.DownloadModule;
import com.ggymm.mtools.modules.encode.EncodeModule;
import com.ggymm.mtools.modules.gobang.GobangModule;
import com.ggymm.mtools.modules.linux.LinuxCommandModule;
import com.ggymm.mtools.modules.logcat.LogcatModule;
import com.ggymm.mtools.modules.qrcode.QrCodeModule;
import com.ggymm.mtools.modules.tools.ToolsModule;
import com.ggymm.mtools.utils.StyleUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * @author gongym
 * @version 创建时间: 2022-01-04 13:21
 */
public class App extends Application {

    private Workbench workbench;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initWorkbench(stage);
        final Scene scene = new Scene(this.workbench);

        stage.setTitle("工具箱");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(750);
        stage.setMinWidth(1000);
        stage.setMinHeight(750);
        stage.show();
        stage.centerOnScreen();

        // 初始化样式
        StyleUtils.initStyle(scene);
    }

    private void initWorkbench(Stage stage) {
        final MenuItem[] menuItems = initMenu();

        this.workbench = Workbench.builder(
                        new CoderModule(stage),
                        new EncodeModule(),
                        new LogcatModule(),

                        new LinuxCommandModule(),
                        new QrCodeModule(),
                        new DownloadModule(),

                        new ToolsModule(),
                        new DevopsModule(),
                        new GobangModule()
                ).navigationDrawerItems(menuItems)
                .modulesPerPage(9)
                .tabFactory(CustomTab::new)
                .build();
    }

    private MenuItem[] initMenu() {

        final MenuItem coder = new MenuItem("代码生成器", null);
        coder.setOnAction(event -> workbench.hideNavigationDrawer());

        final MenuItem setting = new MenuItem("应用设置", null);
        setting.setOnAction(event -> workbench.hideNavigationDrawer());

        return new MenuItem[]{coder, setting};
    }
}
