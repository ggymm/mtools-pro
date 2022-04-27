package com.ggymm.mtools.modules.gobang.ai;

/**
 * @author gongym
 * @version 创建时间: 2022-04-24 23:54
 */
public enum Role {
    AI, PLAYER;

    public Role reverse() {
        if (this == AI) {
            return PLAYER;
        } else {
            return AI;
        }
    }
}
