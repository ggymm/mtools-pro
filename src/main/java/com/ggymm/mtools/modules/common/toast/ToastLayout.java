package com.ggymm.mtools.modules.common.toast;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * @author gongym
 * @version 创建时间: 2022-03-24 17:21
 */
public class ToastLayout extends BorderPane {
    private static final String DEFAULT_STYLE_CLASS = "jfx-snackbar-layout";
    private final Label toast;
    private final StackPane actionContainer;

    public ToastLayout(String message) {
        this(message, null, null);
    }

    public ToastLayout(String message, String actionText, EventHandler<ActionEvent> actionHandler) {
        initialize();

        toast = new Label();
        toast.setMinWidth(Control.USE_PREF_SIZE);
        toast.getStyleClass().add("jfx-snackbar-toast");
        toast.setWrapText(true);
        toast.setText(message);
        StackPane toastContainer = new StackPane(toast);
        toastContainer.setPadding(new Insets(20));
        actionContainer = new StackPane();
        actionContainer.setPadding(new Insets(0, 10, 0, 0));

        toast.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            if (getPrefWidth() == -1) {
                return getPrefWidth();
            }
            double actionWidth = actionContainer.isVisible() ? actionContainer.getWidth() : 0.0;
            return prefWidthProperty().get() - actionWidth;
        }, prefWidthProperty(), actionContainer.widthProperty(), actionContainer.visibleProperty()));

        setLeft(toastContainer);
        setRight(actionContainer);

        if (actionText != null) {
            JFXButton action = new JFXButton();
            action.setText(actionText);
            action.setOnAction(actionHandler);
            action.setMinWidth(Control.USE_PREF_SIZE);
            action.setButtonType(JFXButton.ButtonType.FLAT);
            action.getStyleClass().add("jfx-snackbar-action");
            actionContainer.getChildren().add(action);

            if (!actionText.isEmpty()) {
                action.setVisible(true);
                actionContainer.setVisible(true);
                actionContainer.setManaged(true);
                action.setText("");
                action.setText(actionText);
                action.setOnAction(actionHandler);
            } else {
                actionContainer.setVisible(false);
                actionContainer.setManaged(false);
                action.setVisible(false);
            }
        }
    }

    public String getToast() {
        return toast.getText();
    }

    public void setToast(String toast) {
        this.toast.setText(toast);
    }


    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }
}
