package com.ggymm.mtools.ai;

import com.ggymm.mtools.modules.gobang.ai.model.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongym
 * @version 创建时间: 2022-04-18 15:37
 */
public class TestSort {

    @Test
    public void testPointSort() {
        final Point point1 = new Point();
        point1.setMaxScore(100);

        final Point point2 = new Point();
        point2.setMaxScore(90);

        final List<Point> points = new ArrayList<Point>(){
            {
                add(point1);
                add(point2);
            }
        };

        points.sort(null);

        System.out.println(points);
    }
}


