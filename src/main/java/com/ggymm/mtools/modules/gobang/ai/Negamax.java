package com.ggymm.mtools.modules.gobang.ai;

import com.ggymm.mtools.modules.gobang.ai.model.Point;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.ggymm.mtools.modules.gobang.ai.Config.SEARCH_DEEP;
import static com.ggymm.mtools.modules.gobang.ai.constant.Constant.Score.FIVE;

/**
 * @author gongym
 * @version 创建时间: 2022-04-19 10:53
 */
@Data
public class Negamax {

    private final Board chessBoard;

    private final int MAX = 10 * FIVE.score;
    private final int MIN = -1 * MAX;

    /**
     * 思考的节点数
     */
    private int count;
    /**
     * AB剪枝的次数
     */
    private int abCut;
    private int pvCut;


    /**
     * 递归搜索，直到当前搜索层数结束
     * 每次递归计算的得分情况要把对手的得分*-1，计算总得分=己方得分-对手得分
     *
     * @param deep   搜索层数
     * @param alpha
     * @param beta
     * @param role
     * @param step
     * @param steps
     * @param spread
     */
    private void deepSearch(int deep, int alpha, int beta, Role role, int step, List<Point> steps, int spread) {
        // 落子后，首先评估棋局
        final int score = this.chessBoard.evaluateBoard(role);


    }

    private int negamax(List<Point> candidates, Role role, int deep) {
        this.count = 0;
        this.abCut = 0;
        this.pvCut = 0;

        for (Point point : candidates) {
            // 模拟落子
            chessBoard.putChess(point.getX(), point.getY(), role);

            // 向下搜索
            final List<Point> steps = new ArrayList<>();
            steps.add(point);
            deepSearch(deep - 1, -MAX, -MIN, role.reverse(), 1, steps, 0);

            // 移除棋子
            chessBoard.removeChess(point.getX(), point.getY());

        }

        return 0;
    }

    public void search(List<Point> candidates, Role role) {
        // 不是一次性模拟四回合
        // 分别模拟，一回合，两回合，三回合，四回合的落子情况和得分
        for (int i = 2; i <= SEARCH_DEEP; i += 2) {
            final int score = negamax(candidates, role, i);
            System.out.println(score);
        }
    }
}
