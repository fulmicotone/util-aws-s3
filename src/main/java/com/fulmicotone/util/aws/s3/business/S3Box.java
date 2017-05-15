package com.fulmicotone.util.aws.s3.business;


import com.fulmicotone.commons.util.business.ObjBox;
import com.fulmicotone.commons.util.functional.CollapseKeyFunction;

public class S3Box<T> extends ObjBox<T> {

    public S3Box(T object, CollapseKeyFunction fn) {
        super(object,  fn);
    }

}
