package com.ggymm.mtools.modules.coder;

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
import javafx.scene.text.Font;

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
    public CheckBox isUseLombok;
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
    private Database currentDB;


    public static Node getView() throws IOException {
        URL url = CoderController.class.getResource("/fxml/coder.fxml");
        FXMLLoader fXMLLoader = new FXMLLoader(url);
        return fXMLLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();

        System.out.println(Font.getFontNames());
    }

    private void initView() {
        new Thread(() -> {
            // 获取数据库列表
            databases = DatabaseMapper.getList();
            Platform.runLater(() -> {
                for (Database database : databases) {
                    this.databaseList.getItems().add(database.getShowName());
                }
            });
        }).start();
    }

    private void initEvent() {
        this.databaseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for (Database database : databases) {
                if (newValue.equals(database.getShowName())) {
                    this.currentDB = database;
                }
            }
        });
        this.refreshDatabaseList.setOnMouseClicked((e) -> {

        });

    }
}
