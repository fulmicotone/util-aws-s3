package com.fulmicotone.util.aws.s3.exceptions;


import com.fulmicotone.commons.util.exceptions.WrappedException;

/**
 * Created by dino on 04/05/2017.
 */
public class S3RequestCreationException extends WrappedException {

    public S3RequestCreationException(Throwable wrapped) {
        super(wrapped);
    }
}
