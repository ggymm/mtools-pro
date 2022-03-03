package com.ggymm.mtools.modules.coder.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ggymm.mtools.database.mapper.DatabaseMapper;
import com.ggymm.mtools.database.model.Database;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-01-05 17:58
 */
public class CoderController implements Initializable {

    public ComboBox<String> databaseList;
    public Button refreshDatabaseList;

    public TextField tableName;
    public Button selectTable;

    public TextField outputPath;
    public Button choosePath;

    public CheckBox isOutputCover;
    public CheckBox isOnlyEntity;
    public CheckBox isUseParent;
    public CheckBox isAutoFillColumn;

    public CheckBox isFormatDateColumn;
    public CheckBox isGenFrontEnd;
    public CheckBox isUseCommentAsLabel;
    public CheckBox isUseOriginColumn;

    public TextField packageName;
    public TextField parentPackageName;
    public TextField excludeColumn;
    public TextField autoFillColumn;

    public Button genCode;
    public Button saveConfig;
    public Button openFolder;

    private List<Database> databases;
    private Database currentDatabase;


    public static Node getView() throws IOException {
        final URL url = CoderController.class.getResource("/fxml/coder.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        this.renderDatabase();

        // 默认配置
        this.isOutputCover.setSelected(true);
        this.isOnlyEntity.setSelected(true);
        this.isUseParent.setSelected(false);
        this.isAutoFillColumn.setSelected(true);

        this.isFormatDateColumn.setSelected(true);
        this.isGenFrontEnd.setSelected(false);
        this.isUseCommentAsLabel.setSelected(false);
        this.isUseOriginColumn.setSelected(false);

        this.parentPackageName.setDisable(true);
        this.parentPackageName.setText("com.ninelock.core.base.BaseEntity");
        this.excludeColumn.setDisable(true);
        this.excludeColumn.setText("create_time,create_id,creator,update_time,update_id,del_flag");
        this.autoFillColumn.setText("create_time,create_id,creator,update_time,update_id,del_flag");
    }

    private void initEvent() {
        // 监听数据库列表选择
        this.databaseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for (Database database : databases) {
                if (newValue.equals(database.getShowName())) {
                    this.currentDatabase = database;
                }
            }
        });

        // 刷新数据库列表
        this.refreshDatabaseList.setOnMouseClicked((event) -> {
            this.databaseList.getItems().clear();
            this.renderDatabase();
        });

        // 选择数据库表
        this.selectTable.setOnMouseClicked((event) -> {
            List<String> tableList = TableListController.showTableList(this.currentDatabase);
            if (CollUtil.isEmpty(tableList)) {
                return;
            }
            final String tableListStr = StrUtil.join(";", tableList.toArray());
            this.tableName.setText(tableListStr);
        });

        // 选择文件输出路径
        this.choosePath.setOnMouseClicked((event) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择保存文件位置");
            File directory = directoryChooser.showDialog(new Stage());
            if (directory != null) {
                this.outputPath.setText(directory.getAbsolutePath());
            }
        });

        // 是否使用父类配置项监听
        this.isUseParent.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // 如果选择使用父类
                // 则默认禁止插入/更新自动填充
                this.isAutoFillColumn.setSelected(false);
                this.autoFillColumn.setDisable(true);
            }
            this.isAutoFillColumn.setDisable(newValue);

            this.parentPackageName.setDisable(!newValue);
            this.excludeColumn.setDisable(!newValue);
        });

        // 是否自动填充字段配置项监听
        this.isAutoFillColumn.selectedProperty().addListener((observable, oldValue, newValue) -> this.autoFillColumn.setDisable(!newValue));

        // 生成代码
        this.genCode.setOnMouseClicked((event) -> {
            if (this.currentDatabase == null) {

                return;
            }
            System.out.println();
        });
    }

    /**
     * 渲染数据库列表
     */
    private void renderDatabase() {
        new Thread(() -> {
            databases = DatabaseMapper.getList();
            Platform.runLater(() -> {
                for (Database database : databases) {
                    this.databaseList.getItems().add(database.getShowName());
                }
            });
        }).start();
    }
}
