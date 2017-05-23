package com.fulmicotone.util.aws.s3.utils;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fulmicotone.commons.util.function.FnUtil;
import com.fulmicotone.util.aws.s3.exceptions.S3RequestCreationException;
import com.fulmicotone.util.aws.s3.business.S3Box;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public  class S3FnUtil extends FnUtil {

    private S3FnUtil(){}

    //todo test
     /** (bucket,key,inputstream) => PutObjectRequest **/
    protected static Function<String,
            Function<String,
                    Function<InputStream,PutObjectRequest>>> mkPutRequestFn = bucket->key->is->{
             try {
                 byte[] contentBytes;
                 contentBytes = IOUtils.toByteArray(is);
                 Long contentLength = Long.valueOf(contentBytes.length);
                 ObjectMetadata metadata = new ObjectMetadata();
                 metadata.setContentLength(contentLength);
                 return new PutObjectRequest(bucket,key,is,metadata);
                 } catch (IOException e) {
                 log.error("Failed while reading bytes from %s", e.getMessage());
                 throw new S3RequestCreationException(e);
             }
            };

    /** (delimiter,entryStringlist) => S3Box<InputStream> **/ //todo test
    protected static Function<String,Function<Gson,
            Function<Map.Entry<String,List<Object>>,S3Box<InputStream>>>>
            entrystringlistToS3ObjectStreamFn =
            delimiter->gsonp->stringListEntry -> new S3Box<>(strToISFn.apply(stringListEntry
                    .getValue()
                    .stream()
                    .map(any->anyToCustomJsonFn.apply(any).apply(gsonp))
                    .collect(Collectors.joining(delimiter))),
                     (x) -> stringListEntry.getKey());

    /**  (entryStringlist) => S3Box<InputStream> **/
    public static <T> Function<Map.Entry<String, List<Object>>, S3Box<InputStream>>
    entryStringListToS3ObjectStream(String delimit,Gson gson){
        return  entrystringlistToS3ObjectStreamFn
                .apply(delimit).apply(gson);
    }


    /**  (entryStringlist) => S3Box<InputStream> **/
    public static <T> Function<Map.Entry<String, List<Object>>, S3Box<InputStream>>
    entryStringListToS3ObjectStream(String delimit){
        return  entrystringlistToS3ObjectStreamFn
                .apply(delimit).apply(null);
    }


    /** (bucket,S3Box<InputStream>) => PutObjectRequest **/
    public static PutObjectRequest  s3BoxToPutObjectRequest(String bucket,
                                                            S3Box<InputStream> s3Itembox){
        return mkPutRequestFn
                .apply(bucket)
                .apply(s3Itembox.key())
                .apply(s3Itembox.unbox());
    }
}
