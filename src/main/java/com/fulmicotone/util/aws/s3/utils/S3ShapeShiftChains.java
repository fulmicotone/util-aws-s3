package com.fulmicotone.util.aws.s3.utils;


import com.fulmicotone.commons.util.function.StreamShapeShiftChains;
import com.fulmicotone.commons.util.functional.CollapseKeyFunction;
import com.fulmicotone.util.aws.s3.functional.S3ShapeShiftFunction;
import java.util.function.Function;

import static com.fulmicotone.util.aws.s3.utils.S3FnUtil.entryStringListToS3ObjectStream;

public class S3ShapeShiftChains extends StreamShapeShiftChains {


    private static Function<String, Function<CollapseKeyFunction, S3ShapeShiftFunction<Object>>>
            anyToS4ObjectBoxFn =
            delimiter -> collapseKeyFunction -> objectStream ->
                    groupByKeyFn
                            .apply(collapseKeyFunction)
                            .apply(objectStream)
                            .map(er -> entryStringListToS3ObjectStream(delimiter).apply(er));

    /**
     * convert stream<Object> to  Stream<S3Box>
     *
     * @param delimiter
     * @param keyfn
     * @param <T>
     * @return Stream<S3Box>
     */
    public static <T> S3ShapeShiftFunction<T>
    anyStrmToS3BoxStrm(String delimiter, CollapseKeyFunction<T> keyfn) {
        return (S3ShapeShiftFunction<T>) anyToS4ObjectBoxFn.apply(delimiter).apply(keyfn);

    }


}


