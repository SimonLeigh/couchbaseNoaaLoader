package org.couchbase.noaaLoader;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by simon on 20/03/15.
 */
public class OpFileReader {

    public static void getBufferedReader (InputStream iStream){

        GZIPInputStream is=null;

        try {
            is = new GZIPInputStream(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader decoder = new InputStreamReader(is);
        BufferedReader opReader = new BufferedReader (decoder);

        try {
            NoaaCBInserter.insert(opReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            while (opReader.ready()) {
            //System.out.println(opReader.readLine());
             }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


