package com.fulmicotone.util.aws.s3;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fulmicotone.commons.util.functional.CollapseKeyFunction;
import com.fulmicotone.util.aws.s3.exceptions.S3RequestCreationException;
import com.fulmicotone.util.aws.s3.functional.S3ShapeShiftFunction;
import com.fulmicotone.util.aws.s3.utils.S3ShapeShiftChains;
import com.fulmicotone.util.aws.s3.utils.S3FnUtil;


import java.util.List;
import java.util.stream.Collectors;

public class S3RequestWizard {

    private S3RequestWizard(){}

    public  static <T> PutRequestsBuilder<T> newPutRequest(){ return  new PutRequestsBuilder();}

    public static class PutRequestsBuilder<T> {

        private String bucket;
        private CollapseKeyFunction<T> collapseKeyFn;
        private S3ShapeShiftFunction<T> shapeShiftFunction;
        private String delimiter;

        private PutRequestsBuilder( ){  }

        public PutRequestsBuilder<T> withBucket(String bucket){
            this.bucket=bucket;
            return  this;
        }

        /**
        * @param collapseKey
        @return the objects will be collapsed
        will be converted in jsonString + optional delimiter,
        merged in one unique content that will be transformed in inputstream
        ready to upload on s3  in the key indicated by this function*/
        public PutRequestsBuilder<T> withCollapseKey(CollapseKeyFunction<T> collapseKey){
            this.collapseKeyFn=collapseKey;
            return this;
        }

        public PutRequestsBuilder<T> withDelimiter(String delimiter){
            this.delimiter=delimiter;
            return this;
        }

        public List<PutObjectRequest> createRequests(List<T> sourceList) throws S3RequestCreationException {

          return  S3ShapeShiftChains
                   .anyStrmToS3BoxStrm(this.delimiter,this.collapseKeyFn).apply(sourceList.stream())
                   .map(s3box-> S3FnUtil.s3BoxToPutObjectRequest(this.bucket,s3box))
                  .collect(Collectors.toList());

        }

    }


}
