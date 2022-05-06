package com.ggymm.mtools.modules.gobang.ai.model;

import lombok.Getter;
import lombok.Setter;

import static com.ggymm.mtools.modules.gobang.ai.constant.Constant.Score.*;

/**
 * @author gongym
 * @version 创建时间: 2022-04-08 18:24
 */
@Setter
@Getter
public class PointScore {
    // 己方棋子数目
    private int count;
    // 对手棋子或者边界数目，大于等于1，小于等于2
    private int block;
    // 空的位置（空的位置两侧的己方棋子数）
    private int empty;
    // 另一个方向己方棋子数目
    private int reverseCount;

    public PointScore() {
        reset();
    }

    public void reset() {
        this.count = 1;
        this.block = 0;
        this.empty = -1;
        this.reverseCount = 0;
    }

    @Override
    public String toString() {
        return "PointScore{count=" + count +
                ", block=" + block +
                ", empty=" + empty + "}";
    }

    public void addCount() {
        this.count++;
    }

    public void addBlock() {
        this.block++;
    }

    public void addEmpty() {
        this.empty++;
    }

    public void addSecondCount() {
        this.reverseCount++;
    }

    /**
     * 计算落完子后的得分
     * _: 表示空位
     * ⨉: 表示对手棋子
     * 〇: 表示己方棋子
     *
     * @return 分数
     */
    public int countScore() {
        // 两侧棋子数需要加在一起
        this.count = this.count + this.reverseCount;

        if (this.empty <= 0) { // 没有空位

            if (this.count >= 5) {
                return FIVE.score;
            }

            // 没有敌方棋子
            if (this.block == 0) {
                switch (this.count) {
                    case 1:
                        // 〇
                        return ONE.score;
                    case 2:
                        // 〇〇
                        return TWO.score;
                    case 3:
                        // 〇〇〇
                        return THREE.score;
                    case 4:
                        // 〇〇〇〇
                        return FOUR.score;
                }
            }

            // 地方棋子挡住其中一边
            if (this.block == 1) {
                switch (this.count) {
                    case 1:
                        // ⨉〇
                        return BLOCKED_ONE.score;
                    case 2:
                        // ⨉〇〇
                        return BLOCKED_TWO.score;
                    case 3:
                        // ⨉〇〇〇
                        return BLOCKED_THREE.score;
                    case 4:
                        // ⨉〇〇〇〇
                        return BLOCKED_FOUR.score;
                }
            }

            // block等于2的情况在后面会右考虑

        } else if (this.empty == 1 || this.empty == this.count - 1) {

            if (this.count >= 6) {
                // 〇〇〇〇〇_〇
                // 〇_〇〇〇〇〇
                return FIVE.score;
            }

            if (this.block == 0) {
                switch (this.count) {
                    case 2:
                        // 〇_〇
                        // 〇_〇（两种情况一致，所以要除以2）
                        return TWO.score / 2;
                    case 3:
                        // 〇〇_〇
                        // 〇_〇〇
                        return THREE.score;
                    case 4:
                        // 〇〇〇_〇
                        // 〇_〇〇〇
                        return BLOCKED_FOUR.score;
                    case 5:
                        // 〇〇〇〇_〇
                        // 〇_〇〇〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 1) {
                switch (this.count) {
                    case 2:
                        // ⨉〇_〇
                        return BLOCKED_TWO.score;
                    case 3:
                        // ⨉〇〇_〇
                        // ⨉〇_〇〇
                        return BLOCKED_THREE.score;
                    case 4:
                        // ⨉〇〇〇_〇
                        // ⨉〇_〇〇〇
                    case 5:
                        // ⨉〇〇〇〇_〇
                        // ⨉〇_〇〇〇〇
                        return BLOCKED_FOUR.score;
                }
            }

        } else if (this.empty == 2 || this.empty == this.count - 2) {

            if (this.count >= 7) {
                // 〇〇〇〇〇_〇〇
                // 〇〇_〇〇〇〇〇
                return FIVE.score;
            }

            if (this.block == 0) {
                switch (this.count) {
                    case 3:
                        // 不会进入到此分支
                        // 〇〇_〇
                        // 〇_〇〇
                        return THREE.score;
                    case 4:
                        // 〇〇_〇〇
                    case 5:
                        // 〇〇_〇〇〇
                        // 〇〇〇_〇〇
                        return BLOCKED_FOUR.score;
                    case 6:
                        // 〇〇_〇〇〇〇
                        // 〇〇〇〇_〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 1) {
                switch (this.count) {
                    case 3:
                        // 不会进入到此分支
                        // ⨉〇〇_〇
                        // ⨉〇_〇〇
                        return BLOCKED_THREE.score;
                    case 4:
                        // ⨉〇〇_〇〇
                    case 5:
                        // ⨉〇〇_〇〇〇
                        // ⨉〇〇〇_〇〇
                        return BLOCKED_FOUR.score;
                    case 6:
                        // ⨉〇〇_〇〇〇〇
                        // ⨉〇〇〇〇_〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 2) {
                switch (this.count) {
                    case 4:
                        // ⨉〇〇_〇〇⨉
                    case 5:
                        // ⨉〇〇〇_〇〇⨉
                        // ⨉〇〇_〇〇〇⨉
                    case 6:
                        // ⨉〇〇〇〇_〇〇⨉
                        // ⨉〇〇_〇〇〇〇⨉
                        return BLOCKED_FOUR.score;
                }
            }

        } else if (this.empty == 3 || this.empty == this.count - 3) {

            if (this.count >= 8) {
                // 〇〇〇〇〇_〇〇〇
                // 〇〇〇_〇〇〇〇〇
                return FIVE.score;
            }

            if (this.block == 0) {
                switch (this.count) {
                    case 4:
                        // 不会进入到此分支
                        // 〇〇〇_〇
                        // 〇_〇〇〇
                    case 5:
                        // 不会进入到此分支
                        // 〇〇〇_〇〇
                        // 〇〇_〇〇〇
                        return THREE.score;
                    case 6:
                        // 〇〇〇_〇〇〇
                        return BLOCKED_FOUR.score;
                    case 7:
                        // 〇〇〇_〇〇〇〇
                        // 〇〇〇〇_〇〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 1) {
                switch (this.count) {
                    case 4:
                        // 不会进入到此分支
                        // ⨉〇〇〇_〇
                        // ⨉〇_〇〇〇
                    case 5:
                        // 不会进入到此分支
                        // ⨉〇〇〇_〇〇
                        // ⨉〇〇_〇〇〇
                    case 6:
                        // ⨉〇〇〇_〇〇〇
                        return BLOCKED_FOUR.score;
                    case 7:
                        // ⨉〇〇〇_〇〇〇〇
                        // ⨉〇〇〇〇_〇〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 2) {
                switch (this.count) {
                    case 4:
                        // ⨉〇〇〇_〇⨉
                        // ⨉〇_〇〇〇⨉
                    case 5:
                        // 不会进入到此分支
                        // ⨉〇〇〇_〇〇⨉
                        // ⨉〇〇_〇〇〇⨉
                    case 6:
                        // ⨉〇〇〇_〇〇〇⨉
                    case 7:
                        // ⨉〇〇〇_〇〇〇〇⨉
                        // ⨉〇〇〇〇_〇〇〇⨉
                        return BLOCKED_FOUR.score;
                }
            }

        } else if (this.empty == 4 || this.empty == this.count - 4) {

            if (this.count >= 9) {
                // 〇〇〇〇〇_〇〇〇〇
                // 〇〇〇〇_〇〇〇〇〇
                return FIVE.score;
            }

            if (this.block == 0) {
                switch (this.count) {
                    case 5:
                        // 不会进入到此分支
                        // 〇〇〇〇_〇
                        // 〇_〇〇〇〇
                    case 6:
                        // 不会进入到此分支
                        // 〇〇〇〇_〇〇
                        // 〇〇_〇〇〇〇
                    case 7:
                        // 不会进入到此分支
                        // 〇〇〇〇_〇〇〇
                        // 〇〇〇_〇〇〇〇
                    case 8:
                        // 〇〇〇〇_〇〇〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 1) {
                switch (this.count) {
                    case 4:
                        // 不会进入到此分支
                        // ⨉〇〇〇〇
                        // ⨉〇〇〇〇
                    case 5:
                        // 不会进入到此分支
                        // ⨉〇〇〇〇_〇
                        // ⨉〇_〇〇〇〇
                    case 6:
                        // 不会进入到此分支
                        // ⨉〇〇〇〇_〇〇
                        // ⨉〇〇_〇〇〇〇
                    case 7:
                        // 不会进入到此分支
                        // ⨉〇〇〇_〇〇〇〇
                        // ⨉〇〇〇〇_〇〇〇
                        return BLOCKED_FOUR.score;
                    case 8:
                        // ⨉〇〇〇〇_〇〇〇〇
                        return FOUR.score;
                }
            }

            if (this.block == 2) {
                switch (this.count) {
                    case 5:
                        // ⨉〇〇〇〇_〇⨉
                        // ⨉〇_〇〇〇〇⨉
                    case 6:
                        // 不会进入到此分支
                        // ⨉〇〇〇〇_〇〇⨉
                        // ⨉〇〇_〇〇〇〇⨉
                    case 7:
                        // 不会进入到此分支
                        // ⨉〇〇〇〇_〇〇〇⨉
                        // ⨉〇〇〇_〇〇〇〇⨉
                    case 8:
                        // ⨉〇〇〇〇_〇〇〇〇⨉
                        return BLOCKED_FOUR.score;
                }
            }

        } else if (this.empty == 5 || this.empty == this.count - 5) {
            return FIVE.score;
        }

        return 0;
    }
}
