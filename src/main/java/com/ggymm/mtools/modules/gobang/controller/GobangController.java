package com.ggymm.mtools.modules.gobang.controller;

import com.ggymm.mtools.modules.gobang.ai.GobangAI;
import com.ggymm.mtools.modules.gobang.model.Chess;
import com.ggymm.mtools.modules.gobang.model.Chessboard;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author gongym
 * @version 创建时间: 2022-04-02 18:30
 */
public class GobangController implements Initializable {

    public StackPane root;
    public Canvas canvas;

    private GraphicsContext context2D;

    private GobangAI ai;
    private Chessboard chessboard;
    private boolean turn = true;

    public static Node getView() throws IOException {
        final URL url = GobangController.class.getResource("/fxml/gobang.fxml");
        final FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        this.root.heightProperty().addListener((observable, oldValue, newValue) -> this.resizeCanvas());
        this.root.widthProperty().addListener((observable, oldValue, newValue) -> this.resizeCanvas());
    }

    private void initEvent() {
        this.canvas.setOnMouseClicked(event -> {
            // 判断棋子落点
            final int[] position = this.chessboard.calcPosition(event.getX(), event.getY());
            if (position == null || this.chessboard.hasChess(position[0], position[1])) {
                this.removeChess();
                return;
            }

            // 判断是否轮到玩家下棋
            if (this.turn) {
                final int x = position[0];
                final int y = position[1];
                // this.turn = false;
                final Chess chess = this.drawChess(x, y);
                // TODO: AI下棋
                final int[] turn = this.ai.turn(x, y, this.chessboard.getStep());
                System.out.println(Arrays.toString(turn));
            }
        });
    }

    /**
     * 重绘棋盘
     */
    private void resizeCanvas() {
        final double otherWidth = 24.0 * 2;
        final double currentWidth = this.root.getWidth() - otherWidth;

        final double otherHeight = 24.0 * 2;
        final double currentHeight = this.root.getHeight() - otherHeight;
        if (currentWidth <= 0 || currentHeight <= 0) {
            return;
        }

        this.chessboard = new Chessboard(currentWidth, currentHeight);
        this.canvas.setWidth(this.chessboard.getBoardSize());
        this.canvas.setHeight(this.chessboard.getBoardSize());

        // 初始化AI
        this.ai = new GobangAI(this.chessboard.getChessArray(), false);
        // 绘制棋盘
        this.drawChessboard();
    }

    /**
     * 绘制棋盘
     */
    private void drawChessboard() {
        if (this.context2D == null) {
            this.context2D = this.canvas.getGraphicsContext2D();
        }
        this.context2D.setFill(this.chessboard.getBoardColor());
        this.context2D.fillRect(0, 0, this.chessboard.getBoardSize(), this.chessboard.getBoardSize());

        this.context2D.setStroke(this.chessboard.getLineColor());
        final double length = this.chessboard.getBoxSize() * this.chessboard.getLineNum() + this.chessboard.getPadding();
        for (int i = 0; i <= this.chessboard.getLineNum(); i++) {
            final double start = i * this.chessboard.getBoxSize() + this.chessboard.getPadding();
            this.context2D.strokeLine(this.chessboard.getPadding(), start, length, start);
            this.context2D.strokeLine(start, this.chessboard.getPadding(), start, length);
        }
    }

    /**
     * 绘制棋子
     *
     * @param position 棋子位置
     */
    private Chess drawChess(int x, int y) {
        final Chess chess = this.chessboard.calcChess(x, y);
        context2D.setFill(chess.getColor());
        context2D.fillOval(chess.getLeft(), chess.getTop(), chess.getWidth(), chess.getHeight());

        context2D.setFill(chess.getLineColor());
        context2D.setFont(Font.font(14.0));
        context2D.setTextAlign(TextAlignment.CENTER);
        context2D.fillText(String.valueOf(this.chessboard.getStep()), chess.getCenterX(), chess.getCenterY() + 4);

        this.chessboard.putChess(x, y, chess);
        return chess;
    }

    private void removeChess() {
        // TODO: 需要执行一回合（两次）
        final Chess lastChess = this.chessboard.getLastChess();
        if (lastChess == null) {
            return;
        }

        // x, y, w, h
        final double[] coverPosition = lastChess.getCoverPosition();

        // 重新绘制棋子区域
        context2D.setFill(this.chessboard.getBoardColor());
        context2D.fillOval(coverPosition[0], coverPosition[1], coverPosition[2], coverPosition[3]);
        // 重新绘制线
        context2D.strokeLine(coverPosition[0], lastChess.getCenterY(), coverPosition[0] + coverPosition[2], lastChess.getCenterY());
        context2D.strokeLine(lastChess.getCenterX(), coverPosition[1], lastChess.getCenterX(), coverPosition[1] + coverPosition[2]);
        this.chessboard.removeChess();
    }
}
