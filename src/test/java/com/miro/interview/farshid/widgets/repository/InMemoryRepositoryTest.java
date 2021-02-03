package com.miro.interview.farshid.widgets.repository;

import com.miro.interview.farshid.widgets.entity.Widget;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryRepositoryTest {
    public static final int X = 1;
    public static final int Y = 2;
    public static final int Z_INDEX = 3;
    public static final int W = 5;
    public static final int H = 6;

    @ParameterizedTest
    @MethodSource("zIndexOrderTestParams")
    void ZIndexOrderShift(List<Integer> initialZIndexes, int newZIndex, List<Integer> shiftedZIndexes) {
        InMemoryRepository repo = new InMemoryRepository();
        initialZIndexes.forEach(zIndex -> repo.create(new Widget(null, X, Y, zIndex, W, H)));

        Widget newWidget = repo.create(new Widget(null, X, Y, newZIndex, W, H));

        List<Widget> widgets = repo.list();
        assertThat(widgets.stream().map(Widget::getZIndex))
                .containsExactly(shiftedZIndexes.toArray(new Integer[0]));
        for (int i = 0; i < shiftedZIndexes.size(); i++) {
            assertThat(widgets.get(i).getZIndex()).isEqualTo(shiftedZIndexes.get(i));
        }
        assertThat(newWidget.getZIndex()).isEqualTo(newZIndex);
    }

    private static Stream<Arguments> zIndexOrderTestParams() {
        return Stream.of(
                Arguments.of(Arrays.asList(1, 2, 3), 2, Arrays.asList(1, 2, 3, 4)),
                Arguments.of(Arrays.asList(1, 2, 4), 2, Arrays.asList(1, 2, 3, 4)),
                Arguments.of(Arrays.asList(1, 5, 6), 2, Arrays.asList(1, 2, 5, 6))
        );
    }
}
