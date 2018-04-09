package org.mskcc.data.builder;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class IgvRunnerTest{
    private final IgvRunner runner = new IgvRunner();
    @Test
    public void whenGotoStartIsGreaterThanEnd_shouldThrowException(){
        assertThatThrownBy(() -> runner.buildGoto(Chrome.CHR1, 1000000, 100)).isInstanceOf(GenomicRequestException.class)
                            .hasMessageContaining("greater than");

    }

    @Test
    public void whenGotoIsProper_shouldBeWellFormed(){
        Chrome c = Chrome.CHRX;
        final long start = 1000;
        final long end = 2000;
        assertThat(runner.buildGoto(c, start, end)).isEqualTo("goto chrX:1000-2000");
    }

    @Test
    public void whenPropertiesProvided_shouldHaveKeys(){
        Map<String, String > props = runner.processConfigureFile("configure.properties");
        assertThat(props.get("socket_number")).isEqualTo("60151");
        assertThat(props.get("snapshot_directory")).isEqualTo("~/snapshots");
    }

}
