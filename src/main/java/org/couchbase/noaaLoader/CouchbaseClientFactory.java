package org.couchbase.noaaLoader;

import com.couchbase.client.java.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 11/02/15.
 */
//public class CouchbaseClientFactory {
//
//    private static CouchbaseClient cbClient = null;
//
//    public static CouchbaseClient getInstance(String bucket, String password){
//
//        /* We only want to create and instance if there
//        *    isn't one already
//        */
//
//        if (cbClient == null) {
//            try {
//                List<URI> nodes = new ArrayList<URI>();
//                URI host1 = new URI("http://127.0.0.1:8091/pools");
//                nodes.add(host1);
//                cbClient = new CouchbaseClient(nodes, bucket, password);
//            }
//            catch (IOException ioe){
//                ioe.printStackTrace();
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        return cbClient;
//
//    }
//
//
//
//}

public class CouchbaseClientFactory {

    private static Map<String, Bucket> cbClients = null;

    public static Bucket getInstance(String bucket, String password, String server){

        /* We only want to create and instance if there
        *    isn't one already
        */

        String key = bucket.concat(password);

        if (cbClients == null){
            cbClients = new HashMap<String, Bucket>();
        }

        if (!cbClients.containsKey(key)) {
            try {
                List<String> nodes = new ArrayList<String>();
                String host1 = server;
                nodes.add(host1);
                Cluster cluster = CouchbaseCluster.create(host1);
                Bucket theBucket = cluster.openBucket(bucket,password);
                cbClients.put(key, theBucket);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return cbClients.get(key);

    }


}