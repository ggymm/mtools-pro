package com.ggymm.mtools.modules.gobang.ai.constant;

/**
 * @author gongym
 * @version 创建时间: 2022-04-12 08:43
 */
public class Constant {
    public enum Direction {
        HORIZONTAL, // 横向
        VERTICAL, // 纵向
        RIGHT_OBLIQUE, // 右斜
        LEFT_OBLIQUE, // 左斜
    }

    public enum Score {
        ONE(10),
        // 活二
        TWO(100),
        THREE(1000),
        // 活四
        FOUR(100000),
        // 连五
        FIVE(10000000),
        BLOCKED_ONE(1),
        BLOCKED_TWO(10),
        BLOCKED_THREE(100),
        // 冲四
        BLOCKED_FOUR(10000);

        public final int score;

        Score(int score) {
            this.score = score;
        }
    }
}
