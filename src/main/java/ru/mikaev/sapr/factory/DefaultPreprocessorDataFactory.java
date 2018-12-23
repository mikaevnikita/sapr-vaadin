package ru.mikaev.sapr.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.Construction;
import ru.mikaev.sapr.domain.Knot;
import ru.mikaev.sapr.domain.PreprocessorData;
import ru.mikaev.sapr.domain.Rod;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class DefaultPreprocessorDataFactory {
    public static PreprocessorData defaultPreprocessorData(String dataName,
                                                           LocalDateTime creation) {
        if (StringUtils.isBlank(dataName) || Objects.isNull(creation)) {
            throw new IllegalArgumentException("Data Name and Creation must not be null");
        }

        PreprocessorData preprocessorData = new PreprocessorData();
        preprocessorData.setDataName(dataName);
        preprocessorData.setCreationDateTime(creation);
        preprocessorData.setConstruction(defaultConstruction());

        return preprocessorData;
    }

    private static Construction defaultConstruction() {
        return Construction
                .builder()
                .supportLeft(true)
                .supportRight(false)
                .rods(defaultRods())
                .build();
    }

    private static List<Rod> defaultRods() {
        return Arrays.asList(defaultRod());
    }

    public static Rod defaultRod() {
        final Rod rod = Rod
                .builder()
                .a(0)
                .e(0)
                .l(0)
                .sigma(0)
                .load(0)
                .leftKnot(defaultKnot())
                .rightKnot(defaultKnot())
                .build();
        return rod;
    }

    private static Knot defaultKnot() {
        Knot knot = new Knot();
        knot.setLoad(0);

        return knot;
    }
}
