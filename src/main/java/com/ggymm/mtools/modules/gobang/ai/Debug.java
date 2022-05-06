package com.ggymm.mtools.modules.gobang.ai;

import com.ggymm.mtools.modules.gobang.ai.constant.Constant;
import com.ggymm.mtools.modules.gobang.ai.model.PointScore;

import static com.ggymm.mtools.modules.gobang.ai.Role.AI;

/**
 * @author gongym
 * @version 创建时间: 2022-04-27 09:38
 */
public class Debug {

    private static final Role DEBUG_ROLE = AI;

    private static final boolean DEBUG_EVALUATE_POINT = true;

    public static void printEvaluatePoint(Role role, Constant.Direction direction, int x, int y, PointScore pointScore) {
        if (DEBUG_EVALUATE_POINT && DEBUG_ROLE == role) {
            System.out.println(role + " ========== " + direction);
            System.out.println("x: " + x + " == y: " + y);
            System.out.println(pointScore);
            System.out.println("========================================");
        }
    }


}
