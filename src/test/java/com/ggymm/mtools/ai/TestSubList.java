package com.ggymm.mtools.ai;

import cn.hutool.core.collection.CollUtil;
import com.beust.jcommander.internal.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongym
 * @version 创建时间: 2022-04-18 16:21
 */
public class TestSubList {

    @Test
    public void testSubList() {
        final Point point1 = new Point();
        point1.setScore(100);

        final Point point2 = new Point();
        point2.setScore(50);

        final Point point3 = new Point();
        point3.setScore(10);

        final List<Point> pointList = new ArrayList<Point>() {
            {
                add(point1);
                add(point2);
                add(point3);
            }
        };

        List<Point> newPointList1 = pointList.subList(0, 2);
        System.out.println("1: " + newPointList1);
        newPointList1 = Lists.newArrayList(newPointList1);

        final List<Point> newPointList2 = CollUtil.sub(pointList, 0, 2);
        System.out.println("2: " + newPointList2);

        point1.setScore(101);

        System.out.println("1: " + newPointList1);
        System.out.println("2: " + newPointList2);
    }

    static class Point {
        private int score;

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Point{score=" + score + '}';
        }
    }
}
