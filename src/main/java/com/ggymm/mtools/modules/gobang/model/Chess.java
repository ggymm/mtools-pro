package com.ggymm.mtools.modules.gobang.model;

import com.ggymm.mtools.modules.gobang.ai.Role;
import javafx.scene.paint.Color;
import lombok.Data;

/**
 * @author gongym
 * @version 创建时间: 2022-04-07 09:51
 */
@Data
public class Chess {

    private final double boxSize;

    // 位置
    private int x;
    private int y;

    // 绘图
    private double top;
    private double left;
    private double width;
    private double height;

    // 中心点
    private double centerX;
    private double centerY;

    // 颜色
    private Color color;
    private Color lineColor;

    // 角色
    private Role role;
    // 颜色
    private ChessColor chessColor;

    public double[] getCoverPosition() {
        // x, y, w, h
        return new double[]{this.centerX - this.boxSize / 2, this.centerY - this.boxSize / 2,
                this.boxSize, this.boxSize};
    }

    public Color getColor() {
        switch (this.chessColor) {
            case BLACK:
                return Color.BLACK;
            case WHITE:
                return Color.WHITE;
        }
        return Color.BLACK;
    }

    public Color getLineColor() {
        switch (this.chessColor) {
            case BLACK:
                return Color.WHEAT;
            case WHITE:
                return Color.BLACK;
        }
        return Color.WHITE;
    }
}