package com.ggymm.mtools.modules.coder.controller;

import com.ggymm.mtools.modules.coder.entity.CoderDatabase;
import com.ggymm.mtools.utils.DatabaseUtils;
import com.ggymm.mtools.utils.StyleUtils;
import com.ggymm.mtools.utils.model.Table;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-02-28 09:30
 */
public class TableListController implements Initializable {

    public AnchorPane root;

    public ListView<TableItem> tableList;

    public Button selectAll;
    public Button reverseSelect;
    public Button confirm;

    private Stage stage;
    private CoderDatabase currentDatabase;

    public static String showTableList(CoderDatabase currentDatabase) {
        try {
            final URL url = TableListController.class.getResource("/fxml/table-list.fxml");
            final FXMLLoader loader = new FXMLLoader(url);
            final Scene scene = new Scene(loader.load());
            StyleUtils.initStyle(scene);

            final Stage stage = new Stage();
            stage.setTitle("选择表");
            stage.setScene(scene);

            final TableListController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentDatabase(currentDatabase);

            stage.showAndWait();
            return controller.getSelectedTables();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.root.heightProperty().addListener((observable, oldValue, newValue) -> {
            final double otherHeight = 100.0;
            this.tableList.setMinHeight(newValue.doubleValue() - otherHeight);
        });

        this.selectAll.setOnMouseClicked(event -> {
            final ObservableList<TableItem> items = this.tableList.getItems();
            for (final TableItem tableItem : items) {
                tableItem.setSelected(true);
            }
        });

        this.reverseSelect.setOnMouseClicked(event -> {
            final ObservableList<TableItem> items = this.tableList.getItems();
            for (final TableItem tableItem : items) {
                tableItem.setSelected(!tableItem.isSelected());
            }
        });
        this.confirm.setOnMouseClicked(event -> this.stage.close());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentDatabase(CoderDatabase currentDatabase) {
        if (currentDatabase == null) {
            return;
        }
        this.currentDatabase = currentDatabase;
        new Thread(() -> {
            // 查询数据库表
            final List<Table> tableList = DatabaseUtils.tableList(this.currentDatabase);
            final List<TableItem> tableItemList = new ArrayList<>();
            for (Table table : tableList) {
                tableItemList.add(new TableItem(false, table.getTableName()));
            }
            Platform.runLater(() -> {
                this.tableList.getItems().addAll(tableItemList);
                this.tableList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                this.tableList.setCellFactory(new Callback<ListView<TableItem>, ListCell<TableItem>>() {

                    @Override
                    public ListCell<TableItem> call(ListView<TableItem> param) {
                        CheckBoxListCell<TableItem> cell = new CheckBoxListCell<TableItem>() {
                            @Override
                            public void updateItem(TableItem team, boolean empty) {
                                super.updateItem(team, empty);
                                setSelectedStateCallback(TableItem::selectedProperty);
                            }
                        };

                        cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                            if (event.getTarget() instanceof StackPane) {
                                return;
                            }
                            TableItem item = cell.getItem();
                            item.setSelected(!item.isSelected());
                        });
                        return cell;
                    }
                });
            });
        }).start();
    }

    public String getSelectedTables() {
        final StringBuilder selectedTableList = new StringBuilder();
        final ObservableList<TableItem> tableItemList = tableList.getItems();
        for (TableItem table : tableItemList) {
            if (table.getSelected().get()) {
                selectedTableList.append(table).append(";");
            }
        }
        return selectedTableList.toString();
    }

    @Data
    static class TableItem {
        private final SimpleStringProperty tableName;
        private final SimpleBooleanProperty selected;

        public TableItem(boolean id, String tableName) {
            this.selected = new SimpleBooleanProperty(id);
            this.tableName = new SimpleStringProperty(tableName);
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public final boolean isSelected() {
            return this.selectedProperty().get();
        }

        public final void setSelected(final boolean selected) {
            this.selectedProperty().set(selected);
        }

        @Override
        public String toString() {
            return getTableName().get();
        }
    }
}

