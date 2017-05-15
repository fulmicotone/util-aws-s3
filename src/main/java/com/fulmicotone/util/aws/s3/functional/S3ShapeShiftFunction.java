package com.fulmicotone.util.aws.s3.functional;

import com.fulmicotone.commons.util.functional.StreamShapeShift;
import com.fulmicotone.util.aws.s3.business.S3Box;

import java.io.InputStream;
import java.util.stream.Stream;

@FunctionalInterface
public interface S3ShapeShiftFunction<T> extends StreamShapeShift<Stream<T>,Stream< S3Box<InputStream>>> {



}