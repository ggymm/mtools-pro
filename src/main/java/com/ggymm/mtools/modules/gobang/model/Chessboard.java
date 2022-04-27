package com.ggymm.mtools.modules.gobang.model;

import javafx.scene.paint.Color;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.ggymm.mtools.modules.gobang.ai.Role.AI;
import static com.ggymm.mtools.modules.gobang.ai.Role.PLAYER;
import static com.ggymm.mtools.modules.gobang.model.ChessColor.BLACK;
import static com.ggymm.mtools.modules.gobang.model.ChessColor.WHITE;


/**
 * @author gongym
 * @version 创建时间: 2022-04-06 16:22
 */
@Data
public class Chessboard {

    // TODO: 默认玩家先手
    private final boolean aiFirst = false;

    private final double boxSize = 32;
    private final double chessSize = 32 * 0.85;

    private final Color lineColor = Color.valueOf("#884B08");
    private final Color boardColor = Color.valueOf("#FBE39B");

    private final double boardSize;
    private final int lineNum;
    private final double padding;

    private final Chess[][] chessArray;
    private final List<Chess> chessList;

    public Chessboard(double currentWidth, double currentHeight) {
        this.boardSize = Math.min(currentWidth, currentHeight);
        this.lineNum = (int) ((this.boardSize - 24 * 2) / this.boxSize);
        this.padding = (this.boardSize - this.lineNum * this.boxSize) / 2;

        // 初始化棋子
        this.chessArray = new Chess[this.lineNum + 1][this.lineNum + 1];
        this.chessList = new ArrayList<>();
    }

    /**
     * 计算落子位置
     *
     * @param x 鼠标点击点x坐标
     * @param y 鼠标点击点y坐标
     * @return 落子位置
     */
    public int[] calcPosition(double x, double y) {
        x = x - this.padding;
        y = y - this.padding;

        int pX = (int) ((x + this.boxSize / 2) / this.boxSize);
        int pY = (int) ((y + this.boxSize / 2) / this.boxSize);

        // 判断是否超界
        if (pX < 0 || pY < 0 || pX > this.lineNum || pY > this.lineNum) {
            return null;
        }
        return new int[]{pX, pY};
    }

    /**
     * 生成棋子配置
     *
     * @param position 落子位置
     * @return 棋子对象
     */
    public Chess calcChess(int x, int y) {
        final Chess chess = new Chess(this.boxSize);
        chess.setX(x);
        chess.setY(y);

        double centerX = x * this.boxSize + this.padding;
        double centerY = y * this.boxSize + this.padding;
        chess.setCenterX(centerX);
        chess.setCenterY(centerY);

        chess.setLeft(centerX - this.chessSize / 2);
        chess.setTop(centerY - this.chessSize / 2);
        chess.setWidth(this.chessSize);
        chess.setHeight(this.chessSize);

        final Chess lastChess = this.getLastChess();
        if (lastChess == null) {
            chess.setRole(aiFirst ? AI : PLAYER);
            chess.setChessColor(BLACK);
        } else {
            switch (lastChess.getRole()) {
                case AI:
                    chess.setRole(PLAYER);
                    break;
                case PLAYER:
                    chess.setRole(AI);
                    break;
            }
            switch (lastChess.getChessColor()) {
                case BLACK:
                    chess.setChessColor(WHITE);
                    break;
                case WHITE:
                    chess.setChessColor(BLACK);
                    break;
            }
        }
        return chess;
    }

    public boolean hasChess(int x, int y) {
        return this.chessArray[x][y] != null;
    }

    public int getStep() {
        return this.chessList.size() + 1;
    }

    public Chess getLastChess() {
        if (this.chessList.size() == 0) {
            return null;
        }
        return this.chessList.get(this.chessList.size() - 1);
    }

    /**
     * 添加棋子
     *
     * @param position 棋子位置
     * @param chess    棋子对象
     */
    public void putChess(int x, int y, Chess chess) {
        // 添加历史记录
        this.chessList.add(chess);

        // 将棋子添加到二维数组中
        this.chessArray[x][y] = chess;
    }

    /**
     * 移除当前最后一颗棋子
     */
    public void removeChess() {
        final Chess lastChess = this.getLastChess();
        if (lastChess == null) {
            return;
        }
        this.chessArray[lastChess.getX()][lastChess.getY()] = null;

        this.chessList.remove(this.chessList.size() - 1);
    }
}
