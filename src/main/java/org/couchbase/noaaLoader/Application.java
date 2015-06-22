package org.couchbase.noaaLoader;

import com.couchbase.client.java.Bucket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by simon on 20/03/15.
 */
public class Application {

    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("Usage: arg0=csv file, arg1=cluster ip, arg2=bucket.")
            return;
        }

        String destinationCluster = args[1];
        String destinationBucket = args[2];

        Bucket myClient = CouchbaseClientFactory.getInstance("noaa",destinationBucket, destinationCluster);


        try {
            NoaaCSVInserter.insert(new FileInputStream(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

/*
        try {
            for (int i=1; i <= args.length-1;i++) {
                TarFileReader.getBufferedReader(new FileInputStream(args[i]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return;
*/
    }

}
