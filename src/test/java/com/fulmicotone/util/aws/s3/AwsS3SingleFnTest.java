package com.fulmicotone.util.aws.s3;

import com.fulmicotone.commons.util.business.Time;
import com.fulmicotone.commons.util.function.FnUtil;
import com.fulmicotone.util.aws.s3.business.S3Box;
import com.fulmicotone.util.aws.s3.utils.S3FnUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dino on 03/05/2017.
 */
public class AwsS3SingleFnTest {

    @Test
    public void entryStringListToS3ObjectStreamTest () throws IOException {

        Time time=Time.newTrack();
        final String key="a";
        Map<String,List<Person>> mapPersons=new HashMap<>();
        mapPersons.put(key, Arrays.asList(new Person(key,"surname"),
                            new Person(key,"surname2")));
        Map.Entry<String, List<Object>> entry = (Map.Entry<String, List<Object>>)(Object)
                mapPersons.entrySet().stream().findFirst().get();
        S3Box<InputStream> r = S3FnUtil
                .entryStringListToS3ObjectStream(",")
                .apply(entry);
      boolean keyOk=  r.key().equals(key);
      String value = getString(r.unbox());
      Assert.assertTrue(keyOk);
      Assert.assertTrue("{\"name\":\"a\",\"surname\":\"surname\"},{\"name\":\"a\",\"surname\":\"surname2\"}"
                .equals(value));
        Duration duration = time.stopTrack();
        System.out.println(String.format("%s execution time  %s millis","Test groupByTest:",
                duration.toMillis()));
    }


    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /** finally block to close the {@link BufferedReader} */
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }









}
