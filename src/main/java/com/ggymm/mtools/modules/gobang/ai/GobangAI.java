package com.ggymm.mtools.modules.gobang.ai;

import com.ggymm.mtools.modules.gobang.ai.model.Point;
import com.ggymm.mtools.modules.gobang.model.Chess;

import java.util.List;

import static com.ggymm.mtools.modules.gobang.ai.Role.AI;
import static com.ggymm.mtools.modules.gobang.ai.Role.PLAYER;

/**
 * @author gongym
 * @version 创建时间: 2022-04-07 21:37
 */
public class GobangAI {

    private final Board chessBoard;

    public GobangAI(Chess[][] chessArray, boolean loadRecord) {
        this.chessBoard = new Board(chessArray);
        if (loadRecord) {
            // 如果加载保存的对局
            this.chessBoard.initBoardScore();
        }
    }


    public int[] turn(int x, int y, int step) {
        // 更新棋盘分数
        this.chessBoard.putChess(x, y, PLAYER);

        // 生成所有可以落子的位置
        final List<Point> candidates = this.chessBoard.generate(AI, step);

        // 负极大值搜索
        final Negamax negamax = new Negamax(this.chessBoard);
        negamax.search(candidates, AI);

        return null;
    }
}
