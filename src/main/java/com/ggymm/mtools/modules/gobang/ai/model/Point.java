package com.ggymm.mtools.modules.gobang.ai.model;

import com.ggymm.mtools.modules.gobang.ai.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gongym
 * @version 创建时间: 2022-04-12 11:48
 */
@Setter
@Getter
@ToString
public class Point implements Comparable<Point> {
    private int x;
    private int y;

    private Role role;

    private int aiScore;
    private int playerScore;
    private int maxScore;

    private int calcScore;

    @Override
    public int compareTo(Point other) {
        return other.maxScore - this.maxScore;
    }
}
