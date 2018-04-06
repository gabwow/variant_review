package org.mskcc.data.builder;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class ImageProcessTest {
    private final ImageProcess imager = new ImageProcess();

    @Test
    public void whenThreeColumnsPossible_producesThree(){
        List<ImageProcess.Point> points = new LinkedList<>();
        points.add( new ImageProcess.Point(10, 20, "c0_0"));
        points.add( new ImageProcess.Point(20, 20, "c1_0"));
        points.add( new ImageProcess.Point(20, 30, "c1_1"));
        points.add( new ImageProcess.Point(10, 10, "c0_1"));
        points.add(new ImageProcess.Point(20, 20, "c1_2"));
        points.add( new ImageProcess.Point(25, 20, "c2_0"));

        ImageProcess.Layout layout = imager.buildLayout(points);

        assertThat(layout.getWidth()).isEqualTo(55);
        assertThat(layout.getHeight()).isEqualTo(70);
        assertThat(layout.getPosition(1, 1).id).isEqualTo("c1_1");

    }

}
