open module com.ggymm.mtools {
    requires javafx.web;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.controls;

    requires java.xml;
    requires java.sql;

    requires org.slf4j;

    requires lombok;
    requires freemarker;
    requires hutool.all;
    requires commons.dbutils;
    requires com.zaxxer.hikari;

    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires com.google.common;

    requires com.dlsc.workbenchfx.core;
    requires com.jfoenix;

    exports com.ggymm.mtools;
}