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

    @Test
    public void whenNoMatchWidths_thenAllColumnsLengthOne(){
        //given 4 points with no matching widths
        List<ImageProcess.Point> points = new LinkedList<>();
        points.add( new ImageProcess.Point(5, 20, "c3"));
        points.add( new ImageProcess.Point(1, 20, "c0"));
        points.add( new ImageProcess.Point(2, 20, "c1"));
        points.add( new ImageProcess.Point(4, 20, "c2"));

        //when layout generated
        ImageProcess.Layout layout = imager.buildLayout(points);

        //then 4 columns with length 1
        assertThat(layout.getPosition(3, 0).id).isEqualTo("c3");
        assertThatThrownBy(() -> layout.getPosition(4, 0)).isInstanceOf(GenomicRequestException.class)
                .hasMessageContaining("cell is out of bounds for index");
        assertThatThrownBy(() -> layout.getPosition(1, 1)).isInstanceOf(GenomicRequestException.class)
                .hasMessageContaining("cell is out of bounds for index");

    }

    @Test
    public void whenLayoutFourWidths_thenOrderedByIncreasingWidth(){
        //given 4 points with no matching widths
        List<ImageProcess.Point> points = new LinkedList<>();
        points.add( new ImageProcess.Point(6, 20, "c3"));
        points.add( new ImageProcess.Point(1, 20, "c0"));
        points.add( new ImageProcess.Point(5, 20, "c2"));
        points.add( new ImageProcess.Point(2, 20, "c1"));
        //when layout generated
        ImageProcess.Layout layout = imager.buildLayout(points);

        //then columns are ordered by increasing width
        assertThat(layout.getPosition(0, 0).getX()).isEqualTo(0);
        assertThat(layout.getPosition(1, 0).getX()).isEqualTo(1);
        assertThat(layout.getPosition(2, 0).getX()).isEqualTo(3);
        assertThat(layout.getPosition(3, 0).getX()).isEqualTo(8);
    }

    @Test
    public void oneColumnBalanceOptimal(){
        ImageProcess.Point columnNumber = imager.squareColumnOfIdenticalShapes(10, 1, 10);
        assertThat(columnNumber.getX()).isEqualTo(1);

    }

    @Test
    public void twoColumnBalanceOptimal(){
        ImageProcess.Point columnNumber = imager.squareColumnOfIdenticalShapes(10, 10, 4);
        assertThat(columnNumber.getX()).isEqualTo(2);
    }

    @Test
    public void threeColumnBalanceOptimal(){
        ImageProcess.Point columnNumber = imager.squareColumnOfIdenticalShapes(3, 3, 9);
        assertThat(columnNumber.getX()).isEqualTo(3);
    }

    @Test
    public void whenOptimalBalanceChanges_thenRebalanceOccurs(){
        ImageProcess.Point columnNumber = imager.squareColumnOfIdenticalShapes(8, 2, 5);
        assertThat(columnNumber.getX()).isEqualTo(1);
        columnNumber = imager.squareColumnOfIdenticalShapes(8, 2, 6);
        assertThat(columnNumber.getX()).isEqualTo(2);
    }



}
