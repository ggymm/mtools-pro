package com.ggymm.mtools.modules.coder.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ggymm.mtools.modules.coder.entity.CoderDatabase;
import com.ggymm.mtools.modules.coder.mapper.CoderDatabaseMapper;
import com.ggymm.mtools.modules.coder.mapper.CoderOutputPathMapper;
import com.ggymm.mtools.modules.coder.model.TemplateConfig;
import com.ggymm.mtools.modules.coder.model.TemplateData;
import com.ggymm.mtools.modules.common.toast.ToastUtils;
import com.ggymm.mtools.utils.DatabaseUtils;
import com.ggymm.mtools.utils.TemplateUtils;
import com.ggymm.mtools.utils.model.TableField;
import com.jfoenix.controls.JFXSnackbar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.common.base.CaseFormat.*;

/**
 * @author gongym
 * @version 创建时间: 2022-01-05 17:58
 */
public class CoderController implements Initializable {

    public AnchorPane root;

    public ComboBox<String> databaseList;
    public Button refreshDatabaseList;

    public TextField tableNameList;
    public Button selectTable;

    public TextField outputPath;
    public Button choosePath;

    public CheckBox isUseParent;
    public CheckBox isEntityOnly;
    public CheckBox isUseOriginColumn;
    public CheckBox isUseTableAsPackage;

    public CheckBox isColumnAutoFill;
    public CheckBox isDateColumnFormat;
    public CheckBox isColumnTableLogic;
    public CheckBox isGenFrontend;

    public TextField packageName;
    public TextField parentPackageName;
    public TextField excludeColumn;
    public TextField autoFillColumn;

    public Button genCode;
    public Button openFolder;

    private List<CoderDatabase> databases;
    private CoderDatabase currentDatabase;

    private JFXSnackbar snackbar;

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
        this.renderOutputPath();

        this.snackbar = new JFXSnackbar(root);
        // 默认配置
        this.isUseParent.setSelected(false);
        this.isEntityOnly.setSelected(true);
        this.isUseOriginColumn.setSelected(false);
        this.isUseTableAsPackage.setSelected(false);

        this.isColumnAutoFill.setSelected(true);
        this.isDateColumnFormat.setSelected(true);
        this.isColumnTableLogic.setSelected(true);
        this.isGenFrontend.setSelected(true);

        this.packageName.setText("com.ninelock.api");
        this.parentPackageName.setDisable(true);

        // 不允许更改
        this.parentPackageName.setText("com.ninelock.core.base.BaseEntity");
        this.parentPackageName.setEditable(false);

        // 不允许更改
        this.excludeColumn.setDisable(true);
        this.excludeColumn.setText("create_time;create_id;creator;update_time;update_id;del_flag");
        this.excludeColumn.setEditable(false);

        // 不允许更改
        this.autoFillColumn.setText("create_time;create_id;creator;update_time;update_id;del_flag");
        this.autoFillColumn.setEditable(false);
    }

    private void initEvent() {
        // 监听数据库列表选择
        this.databaseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            for (CoderDatabase database : databases) {
                if (newValue.equals(database.getShowName())) {
                    this.currentDatabase = database;
                }
            }
        });

        // 刷新数据库列表
        this.refreshDatabaseList.setOnMouseClicked(event -> {
            final ObservableList<String> items = this.databaseList.getItems();
            if (items != null) {
                items.clear();
                this.renderDatabase();
            }
        });

        // 选择数据库表
        this.selectTable.setOnMouseClicked(event -> this.tableNameList.setText(TableListController.showTableList(this.currentDatabase)));

        // 选择文件输出路径
        this.choosePath.setOnMouseClicked(event -> {
            final File lastFolder = new File(this.outputPath.getText());
            if (!lastFolder.exists() && !lastFolder.isDirectory()) {
                if (!lastFolder.mkdirs()) {
                    return;
                }
            }
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择保存文件位置");
            directoryChooser.setInitialDirectory(lastFolder);
            File directory = directoryChooser.showDialog(null);
            if (directory != null) {
                this.outputPath.setText(directory.getAbsolutePath());
            }
        });

        // 是否使用父类配置项监听
        this.isUseParent.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // 选择使用父类
                // 默认禁止插入/更新自动填充
                this.isColumnAutoFill.setSelected(false);
                this.autoFillColumn.setDisable(true);
                // 默认禁止插入/更新自动填充
                this.isColumnTableLogic.setSelected(false);
                this.isColumnTableLogic.setDisable(true);
            }
            this.isColumnAutoFill.setDisable(newValue);
            this.isColumnTableLogic.setDisable(newValue);

            this.parentPackageName.setDisable(!newValue);
            this.excludeColumn.setDisable(!newValue);
        });

        // 是否自动填充字段配置项监听
        this.isColumnAutoFill.selectedProperty().addListener((observable, oldValue, newValue) -> this.autoFillColumn.setDisable(!newValue));

        // 生成代码
        this.genCode.setOnMouseClicked(event -> {
            // 禁用按钮
            this.genCode.setDisable(true);
            this.genCode.setText("正在生成");

            if (this.currentDatabase == null) {
                ToastUtils.error(this.snackbar, "错误, 未选择数据库");
                this.genCode.setDisable(false);
                return;
            }

            String tableNameList = this.tableNameList.getText();
            if (StrUtil.isBlank(tableNameList)) {
                ToastUtils.error(this.snackbar, "错误, 未选择数据表");
                this.genCode.setDisable(false);
                return;
            }

            final List<TemplateConfig> templateList = new ArrayList<>();
            if (this.isGenFrontend.isSelected()) {
                templateList.add(new TemplateConfig("vue/data", "Data.js", "vue_data"));
                templateList.add(new TemplateConfig("vue/rules", "Rules.js", "vue_rules"));
            }
            if (!this.isEntityOnly.isSelected()) {
                templateList.add(new TemplateConfig("controller", "Controller.java", "controller"));
                templateList.add(new TemplateConfig("service", "Service.java", "service"));
            }
            templateList.add(new TemplateConfig("mapper", "Mapper.java", "mapper"));
            templateList.add(new TemplateConfig("mapper/xml", "Mapper.xml", "mapperXml"));
            templateList.add(new TemplateConfig("entity", ".java", "entity"));

            final String[] tableList = tableNameList.split(";");

            generateFiles(this.outputPath.getText(), templateList, tableList);
        });

        this.openFolder.setOnMouseClicked((event) -> {
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + this.outputPath.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 渲染数据库列表
     */
    private void renderDatabase() {
        new Thread(() -> {
            databases = CoderDatabaseMapper.getList();
            Platform.runLater(() -> {
                for (CoderDatabase database : databases) {
                    this.databaseList.getItems().add(database.getShowName());
                }
            });
        }).start();
    }

    private void renderOutputPath() {
        new Thread(() -> {
            final String lastOutputPath = CoderOutputPathMapper.lastOutputPath();
            Platform.runLater(() -> this.outputPath.setText(lastOutputPath));
        }).start();
    }

    private void generateFiles(String outputPath, List<TemplateConfig> templateList, String[] tableList) {
        new Thread(() -> {
            // 保存上次输出路径
            CoderOutputPathMapper.update(outputPath);

            for (String table : tableList) {
                // 获取表的字段列表
                final List<TableField> tableFieldList = DatabaseUtils.tableFieldList(this.currentDatabase, table);

                final TemplateData templateData = new TemplateData();

                templateData.setUseParent(this.isUseParent.isSelected());
                templateData.setUseOrigin(this.isUseOriginColumn.isSelected());
                templateData.setUseTableAsPackage(this.isUseTableAsPackage.isSelected());

                templateData.setAutoFill(this.isColumnAutoFill.isSelected());
                templateData.setFormatDate(this.isDateColumnFormat.isSelected());
                templateData.setTableLogic(this.isColumnTableLogic.isSelected());

                templateData.setBasePackageName(this.packageName.getText());
                templateData.setParentClass(this.parentPackageName.getText());
                templateData.setExcludeColumn(this.excludeColumn.getText());
                templateData.setAutoFillColumn(this.autoFillColumn.getText());

                templateData.setTableName(table);
                templateData.setFieldList(tableFieldList);
                if (this.isGenFrontend.isSelected()) {
                    templateData.setVueFieldList(tableFieldList);
                }

                for (TemplateConfig config : templateList) {
                    // 写入文件路径
                    String filePath = outputPath;
                    if (templateData.getUseTableAsPackage()) {
                        if (templateData.getUseOrigin()) {
                            filePath += "/" + table + "/" + config.getPath() + "/";
                        } else {
                            filePath += "/" + LOWER_UNDERSCORE.to(LOWER_CAMEL, table) + "/" + config.getPath() + "/";
                        }
                    } else {
                        filePath += "/" + config.getPath() + "/";
                    }
                    filePath += LOWER_UNDERSCORE.to(UPPER_CAMEL, table) + config.getSuffix();

                    final Template entity = TemplateUtils.getTemplate(config.getTemplateName());
                    if (entity != null) {
                        try {
                            // 生成代码
                            StringWriter stringWriter = new StringWriter();
                            entity.process(templateData, stringWriter);
                            String result = stringWriter.toString();

                            // 写入文件
                            FileUtil.writeString(result, filePath, "UTF-8");

                        } catch (TemplateException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            Platform.runLater(() -> {
                ToastUtils.info(this.snackbar, "生成完毕");
                this.genCode.setDisable(false);
                this.genCode.setText("生成代码");
            });
        }).start();
    }
}
