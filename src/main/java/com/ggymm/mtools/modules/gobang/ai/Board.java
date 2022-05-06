package com.ggymm.mtools.modules.gobang.ai;

import cn.hutool.core.collection.CollUtil;
import com.ggymm.mtools.modules.gobang.ai.constant.Constant;
import com.ggymm.mtools.modules.gobang.ai.model.Point;
import com.ggymm.mtools.modules.gobang.ai.model.PointScore;
import com.ggymm.mtools.modules.gobang.model.Chess;

import java.util.ArrayList;
import java.util.List;

import static com.ggymm.mtools.modules.gobang.ai.Config.SEARCH_RESULT_LIMIT;
import static com.ggymm.mtools.modules.gobang.ai.Role.AI;
import static com.ggymm.mtools.modules.gobang.ai.Role.PLAYER;
import static com.ggymm.mtools.modules.gobang.ai.Toolkit.fixScore;
import static com.ggymm.mtools.modules.gobang.ai.Toolkit.hasNeighbor;
import static com.ggymm.mtools.modules.gobang.ai.constant.Constant.Direction.*;
import static com.ggymm.mtools.modules.gobang.ai.constant.Constant.Score.*;

/**
 * @author gongym
 * @version 创建时间: 2022-04-12 09:25
 */
public class Board {

    /**
     * 棋盘的落子
     */
    public Role[][] chessArray;

    /**
     * AI在棋盘中每个位置的得分
     */
    public int[][] aiScore;

    /**
     * 玩家在棋盘中每个位置的得分
     */
    public int[][] playerScore;

    public Board(Chess[][] chessArray) {
        final int length = chessArray.length;
        this.chessArray = new Role[length][length];
        for (int x = 0; x < chessArray.length; x++) {
            for (int y = 0; y < chessArray[x].length; y++) {
                final Chess chess = chessArray[x][y];
                if (chess != null) {
                    this.chessArray[x][y] = chess.getRole();
                }
            }
        }

        this.aiScore = new int[length][length];
        this.playerScore = new int[length][length];
    }

    public static void init() {

    }

    public void putChess(int x, int y, Role role) {
        // 落子
        this.chessArray[x][y] = role;
        // 更新棋盘分数
        // 局部刷新: 只更新落子点米子方向位置的分数
        updateBoardScore(x, y);
    }

    public void removeChess(int x, int y) {
        // 移除棋子
        this.chessArray[x][y] = null;
        // 更新棋盘分数
        // 局部刷新: 只更新落子点米子方向位置的分数
        updateBoardScore(x, y);
    }

    /**
     * 初始化棋盘分数
     */
    public void initBoardScore() {
        for (int x = 0; x < this.chessArray.length; x++) {
            for (int y = 0; y < this.chessArray[x].length; y++) {
                final Role role = this.chessArray[x][y];
                if (role == null) {
                    if (hasNeighbor(this.chessArray, x, y, 2, 2)) {
                        // 双方都计算得分
                        aiScore[x][y] = evaluatePoint(x, y, AI, null);
                        playerScore[x][y] = evaluatePoint(x, y, PLAYER, null);
                    }
                } else {
                    switch (role) {
                        case AI:
                            // 对AI打分，玩家为0分
                            aiScore[x][y] = evaluatePoint(x, y, AI, null);
                            playerScore[x][y] = 0;
                            break;
                        case PLAYER:
                            // 对玩家打分，AI为0分
                            aiScore[x][y] = 0;
                            playerScore[x][y] = evaluatePoint(x, y, PLAYER, null);
                            break;
                    }
                }
            }
        }
    }

    /**
     * 更新棋盘分数
     *
     * @param x 落子位置坐标X
     * @param y 落子位置坐标X
     */
    public void updateBoardScore(int x, int y) {
        final int length = this.chessArray.length;
        // （-4 -> 0 -> 4）
        int radius = 4;

        // 横向
        for (int i = -radius; i <= radius; i++) {
            int newX = x + i;
            if (newX < 0) continue;
            if (newX >= length) break;
            updatePositionScore(newX, y, HORIZONTAL);
        }

        // 纵向
        for (int i = -radius; i <= radius; i++) {
            int newY = y + i;
            if (newY < 0) continue;
            if (newY >= length) break;
            updatePositionScore(x, newY, VERTICAL);
        }

        // 右斜
        for (int i = -radius; i <= radius; i++) {
            int newX = x + i;
            int newY = y + i;
            if (newX < 0 || newY < 0) continue;
            if (newX >= length || newY >= length) break;
            updatePositionScore(newX, newY, RIGHT_OBLIQUE);
        }

        // 左斜
        for (int i = -radius; i <= radius; i++) {
            int newX = x + i;
            int newY = y - i;
            if (newX < 0 || newY < 0) continue;
            if (newX >= length || newY >= length) break;
            updatePositionScore(newX, newY, LEFT_OBLIQUE);
        }
    }

    /**
     * 更新点分数
     *
     * @param x         点位置坐标X
     * @param y         点位置坐标X
     * @param direction 方向（可选）
     */
    private void updatePositionScore(int x, int y, Constant.Direction direction) {
        final Role chess = this.chessArray[x][y];
        if (chess == null) {
            // 双方都计算得分
            // **因为双方都可以落子，这里计算的是假设落子后的得分**
            aiScore[x][y] = evaluatePoint(x, y, AI, direction);
            playerScore[x][y] = evaluatePoint(x, y, PLAYER, direction);
        } else {
            switch (chess) {
                case AI:
                    // 对AI打分，玩家为0分
                    aiScore[x][y] = evaluatePoint(x, y, AI, direction);
                    playerScore[x][y] = 0;
                    break;
                case PLAYER:
                    // 对玩家打分，AI为0分
                    aiScore[x][y] = 0;
                    playerScore[x][y] = evaluatePoint(x, y, PLAYER, direction);
                    break;
            }
        }
    }

    /**
     * 评估棋局
     *
     * @param aiScore     AI的得分
     * @param playerScore 玩家的得分
     * @param role        当前角色
     * @return 总分数
     */
    public int evaluateBoard(Role role) {
        int aiMaxScore = 0;
        int playerMaxScore = 0;

        for (int i = 0; i < this.chessArray.length; i++) {
            for (int j = 0; j < this.chessArray[i].length; j++) {
                final Role chess = this.chessArray[i][j];
                switch (chess) {
                    case AI:
                        aiMaxScore += fixScore(this.aiScore[i][j]);
                        break;
                    case PLAYER:
                        playerMaxScore += fixScore(this.playerScore[i][j]);
                        break;
                }
            }
        }

        return (role == AI ? 1 : -1) * (aiMaxScore - playerMaxScore);
    }

    /**
     * 评估点的得分
     *
     * @param x         位置坐标X
     * @param y         位置坐标Y
     * @param role      当前角色
     * @param direction 方向
     * @return 分数
     */
    public int evaluatePoint(int x, int y, Role role, Constant.Direction direction) {
        final int length = this.chessArray.length;
        int count = 0;

        final PointScore pointScore = new PointScore();

        // 横向
        if (direction == null || direction == HORIZONTAL) {
            pointScore.reset();
            // 向右
            // 一直找到边界或者敌方棋子
            for (int i = x + 1; true; i++) {
                if (i >= length) {
                    // 边界等同于敌方棋子
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[i][y];
                if (chess == null) {
                    if (i + 1 < length && pointScore.getEmpty() == -1 &&
                            this.chessArray[i + 1][y] != null && this.chessArray[i + 1][y] == role) {
                        // 不是边界
                        // 之前没有空位置
                        // 下一个为己方棋子
                        pointScore.setEmpty(pointScore.getCount());
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        // 己方棋子
                        pointScore.addCount();
                    } else {
                        // 敌方棋子，跳出循环
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            // 向左
            for (int i = x - 1; true; i--) {
                if (i < 0) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[i][y];
                if (chess == null) {
                    if (i > 0 && pointScore.getEmpty() == -1 &&
                            this.chessArray[i - 1][y] != null && this.chessArray[i - 1][y] == role) {
                        pointScore.setEmpty(0);
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addSecondCount();
                        if (pointScore.getEmpty() != -1) {
                            pointScore.addEmpty();
                        }
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            count += pointScore.countScore();
            // 输出调试信息
            Debug.printEvaluatePoint(role, direction, x, y, pointScore);
        }

        // 纵向
        if (direction == null || direction == VERTICAL) {
            pointScore.reset();

            // 向上
            for (int i = y + 1; true; i++) {
                if (i >= length) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[x][i];
                if (chess == null) {
                    if (i + 1 < length && pointScore.getEmpty() == -1 &&
                            this.chessArray[x][i + 1] != null && this.chessArray[x][i + 1] == role) {
                        pointScore.setEmpty(pointScore.getCount());
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addCount();
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            // 向下
            for (int i = y - 1; true; i--) {
                if (i < 0) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[x][i];
                if (chess == null) {
                    if (i > 0 && pointScore.getEmpty() == -1 &&
                            this.chessArray[x][i - 1] != null && this.chessArray[x][i - 1] == role) {
                        pointScore.setEmpty(0);
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addSecondCount();
                        if (pointScore.getEmpty() != -1) {
                            pointScore.addEmpty();
                        }
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            count += pointScore.countScore();
            // 输出调试信息
            Debug.printEvaluatePoint(role, direction, x, y, pointScore);
        }

        // 右斜
        if (direction == null || direction == RIGHT_OBLIQUE) {
            pointScore.reset();

            // 右下
            for (int i = 1; true; i++) {
                int newX = x + i;
                int newY = y + i;
                if (newX >= length || newY >= length) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[newX][newY];
                if (chess == null) {
                    if (newX + 1 < length && newY + 1 < length && pointScore.getEmpty() == -1 &&
                            this.chessArray[newX + 1][newY + 1] != null && this.chessArray[newX + 1][newY + 1] == role) {
                        pointScore.setEmpty(pointScore.getCount());
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addCount();
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }

            }

            // 左上
            for (int i = 1; true; i++) {
                int newX = x - i;
                int newY = y - i;
                if (newX < 0 || newY < 0) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[newX][newY];
                if (chess == null) {
                    if (newX > 0 && newY > 0 && pointScore.getEmpty() == -1 &&
                            this.chessArray[newX - 1][newY - 1] != null && this.chessArray[newX - 1][newY - 1] == role) {
                        pointScore.setEmpty(0);
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addSecondCount();
                        if (pointScore.getEmpty() != -1) {
                            pointScore.addEmpty();
                        }
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            count += pointScore.countScore();
            // 输出调试信息
            Debug.printEvaluatePoint(role, direction, x, y, pointScore);
        }

        // 左斜
        if (direction == null || direction == LEFT_OBLIQUE) {
            pointScore.reset();

            // 右上
            for (int i = 1; true; i++) {
                int newX = x + i;
                int newY = y - i;
                if (newX < 0 || newY < 0 || newX >= length || newY >= length) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[newX][newY];
                if (chess == null) {
                    if (newX + 1 < length && newY > 0 && pointScore.getEmpty() == -1 &&
                            this.chessArray[newX + 1][newY - 1] != null && this.chessArray[newX + 1][newY - 1] == role) {
                        pointScore.setEmpty(pointScore.getCount());
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addCount();
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            // 左下
            for (int i = 1; true; i++) {
                int newX = x - i;
                int newY = y + i;
                if (newX < 0 || newY < 0 || newX >= length || newY >= length) {
                    pointScore.addBlock();
                    break;
                }

                final Role chess = this.chessArray[newX][newY];
                if (chess == null) {
                    if (newX > 0 && newY + 1 < length && pointScore.getEmpty() == -1 &&
                            this.chessArray[newX - 1][newY + 1] != null && this.chessArray[newX - 1][newY + 1] == role) {
                        pointScore.setEmpty(0);
                    } else {
                        break;
                    }
                } else {
                    if (chess == role) {
                        pointScore.addSecondCount();
                        if (pointScore.getEmpty() != -1) {
                            pointScore.addEmpty();
                        }
                    } else {
                        pointScore.addBlock();
                        break;
                    }
                }
            }

            count += pointScore.countScore();
            // 输出调试信息
            Debug.printEvaluatePoint(role, direction, x, y, pointScore);
        }

        return count;
    }

    /**
     * 生成所有可能的下棋位置
     *
     * @param role 角色
     * @param step 当前步骤
     * @return 可能下棋的位置
     */
    public List<Point> generate(Role role, int step) {
        final int length = this.chessArray.length;

        // 各种得分方式集合
        // 连城5子
        final List<Point> five = new ArrayList<>();

        // 活四
        final List<Point> aiFour = new ArrayList<>();
        final List<Point> playerFour = new ArrayList<>();

        // 冲四
        final List<Point> aiBlockedFour = new ArrayList<>();
        final List<Point> playerBlockedFour = new ArrayList<>();

        final List<Point> aiTwoThree = new ArrayList<>();
        final List<Point> playerTwoThree = new ArrayList<>();

        final List<Point> aiThree = new ArrayList<>();
        final List<Point> playerThree = new ArrayList<>();

        final List<Point> aiTwo = new ArrayList<>();
        final List<Point> playerTwo = new ArrayList<>();

        final List<Point> neighbor = new ArrayList<>();

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                if (this.chessArray[x][y] == null) {

                    // 优化
                    if (step < 6) {
                        if (!hasNeighbor(this.chessArray, x, y, 1, 1)) {
                            continue;
                        }
                    } else if (!hasNeighbor(this.chessArray, x, y, 2, 2)) {
                        continue;
                    }

                    int aiScore = this.aiScore[x][y];
                    int playerScore = this.playerScore[x][y];
                    int maxScore = Math.max(aiScore, playerScore);

                    final Point point = new Point();
                    point.setX(x);
                    point.setY(y);
                    point.setAiScore(aiScore);
                    point.setPlayerScore(playerScore);
                    point.setMaxScore(maxScore);
                    point.setRole(role);

                    if (aiScore >= FIVE.score || playerScore >= FIVE.score) {
                        five.add(point);
                    } else {
                        if (aiScore >= FOUR.score) {
                            aiFour.add(point);
                        } else if (aiScore >= BLOCKED_FOUR.score) {
                            aiBlockedFour.add(point);
                        } else if (aiScore >= 2 * THREE.score) {
                            aiTwoThree.add(point);
                        } else if (aiScore >= THREE.score) {
                            aiThree.add(point);
                        } else if (aiScore >= TWO.score) {
                            aiTwo.add(point);
                        } else {
                            neighbor.add(point);
                        }

                        if (playerScore >= FOUR.score) {
                            playerFour.add(point);
                        } else if (playerScore >= BLOCKED_FOUR.score) {
                            playerBlockedFour.add(point);
                        } else if (playerScore >= 2 * THREE.score) {
                            playerTwoThree.add(point);
                        } else if (playerScore >= THREE.score) {
                            playerThree.add(point);
                        } else if (playerScore >= TWO.score) {
                            playerTwo.add(point);
                        } else {
                            neighbor.add(point);
                        }
                    }
                }
            }
        }


        // 如果连成五子，直接返回
        if (five.size() > 0) {
            return five;
        }

        // 如果能活四，不考虑冲四
        if (role == AI && aiFour.size() > 0) {
            return aiFour;
        }
        if (role == PLAYER && playerFour.size() > 0) {
            return playerFour;
        }

        // 对面有活四，自己没有冲四
        // 只考虑对面活四，不考虑自己冲四
        if (role == AI && playerFour.size() > 0 && aiBlockedFour.size() <= 0) {
            return playerFour;
        }
        if (role == PLAYER && aiFour.size() > 0 && playerBlockedFour.size() <= 0) {
            return aiFour;
        }

        // 对面有活四，自己有冲四
        List<Point> allFour = new ArrayList<>();
        List<Point> allBlockedFour = new ArrayList<>();
        if (role == AI) {
            allFour.addAll(aiFour);
            allFour.addAll(playerFour);

            allBlockedFour.addAll(aiBlockedFour);
            allBlockedFour.addAll(playerBlockedFour);
        } else {
            allFour.addAll(playerFour);
            allFour.addAll(aiFour);

            allBlockedFour.addAll(playerBlockedFour);
            allBlockedFour.addAll(aiBlockedFour);
        }
        if (allFour.size() > 0) {
            allFour.addAll(allBlockedFour);
            return allFour;
        }

        // 其他情况
        List<Point> result = new ArrayList<>();
        if (role == AI) {
            result.addAll(aiTwoThree);
            result.addAll(playerTwoThree);

            result.addAll(aiBlockedFour);
            result.addAll(playerBlockedFour);

            result.addAll(aiThree);
            result.addAll(playerThree);
        }
        if (role == PLAYER) {
            result.addAll(playerTwoThree);
            result.addAll(aiTwoThree);

            result.addAll(playerBlockedFour);
            result.addAll(aiBlockedFour);

            result.addAll(playerThree);
            result.addAll(aiThree);
        }

        // 双三不一定比一个活三好
        if (aiTwoThree.size() > 0 || playerTwoThree.size() > 0) {
            return result;
        }

        List<Point> allTwo = new ArrayList<>();
        if (role == AI) {
            allTwo.addAll(aiTwo);
            allTwo.addAll(playerTwo);
        } else {
            allTwo.addAll(playerTwo);
            allTwo.addAll(aiTwo);
        }
        if (allTwo.size() > 0) {
            allTwo.sort(null);
            result.addAll(allTwo);
        } else {
            result.addAll(neighbor);
        }

        // 多余的进行截断
        if (result.size() > SEARCH_RESULT_LIMIT) {
            return CollUtil.sub(result, 0, SEARCH_RESULT_LIMIT);
        }
        return result;
    }
}
