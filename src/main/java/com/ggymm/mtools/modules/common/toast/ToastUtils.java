package com.ggymm.mtools.modules.common.toast;

import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * @author gongym
 * @version 创建时间: 2022-03-24 17:27
 */
public class ToastUtils {

    public static void error(JFXSnackbar snackbar, String message) {
        notification(snackbar, message, ToastType.ERROR);
    }

    public static void info(JFXSnackbar snackbar, String message) {
        notification(snackbar, message, ToastType.INFO);
    }

    private static void notification(JFXSnackbar snackbar, String message, ToastType type) {
        snackbar.close();
        snackbar.enqueue(
                new JFXSnackbar.SnackbarEvent(
                        new ToastLayout(message, "关闭", action -> Platform.runLater(snackbar::close)),
                        Duration.INDEFINITE
                )
        );
    }

    public enum ToastType {
        ERROR {
            @Override
            String getIcon() {
                return "assets/img/notification/error.png";
            }

            @Override
            String getColor() {
                return "#BB401E";
            }
        },
        INFO {
            @Override
            String getIcon() {
                return "assets/img/notification/ok.png";
            }

            @Override
            String getColor() {
                return "#469F95";
            }
        };

        abstract String getIcon();

        abstract String getColor();
    }
}
