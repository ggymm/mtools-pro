package com.ggymm.mtools.modules.gobang.ai;

import static com.ggymm.mtools.modules.gobang.ai.constant.Constant.Score.*;

/**
 * @author gongym
 * @version 创建时间: 2022-04-11 19:04
 */
public class Toolkit {

    /**
     * 冲四的分比活三高
     * 但是容易盲目冲四
     * 所以有无意义的冲四，则将分数降低到和活三一样
     * 冲四活三这种杀棋，则将分数提高
     *
     * @param score 当前分数
     * @return 修复后的分数
     */
    public static int fixScore(int score) {
        if (score < FOUR.score && score >= BLOCKED_FOUR.score) {
            if (score < BLOCKED_FOUR.score + THREE.score) {
                // 单独冲四
                return THREE.score;
            }
            if (score >= (BLOCKED_FOUR.score + THREE.score) && score < BLOCKED_FOUR.score * 2) {
                // 冲四活三，比双三分高，相当于自己形成活四
                return FOUR.score;
            }
            // 双冲四 比活四分数也高
            return FOUR.score * 2;
        }
        return score;
    }

    /**
     * 判断当前位置是否有相邻棋子
     *
     * @param chessArray 棋盘棋子
     * @param x          位置坐标X
     * @param y          位置坐标Y
     * @param distance   距离
     * @param count      棋子数目
     * @return 是否包含
     */
    public static boolean hasNeighbor(Role[][] chessArray, int x, int y, int distance, int count) {
        final int length = chessArray.length;
        for (int i = x - distance; i <= x + distance; i++) {
            if (i < 0 || i >= length) {
                continue;
            }
            for (int j = y - distance; j < y + distance; j++) {
                if (j < 0 || j >= length) {
                    continue;
                }
                if (i == x && j == y) {
                    continue;
                }
                if (chessArray[i][j] != null) {
                    count--;
                    if (count <= 0) return true;
                }
            }
        }
        return false;
    }
}
