package org.couchbase.noaaLoader;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by simon on 20/03/15.
 */
public class NoaaCBInserter {

    /*
        Method creates and inserts documents for each line in opFile based on headers.
     */
    public static void insert(BufferedReader opFile) {
        Bucket myClient = CouchbaseClientFactory.getInstance("noaa", "", "localhost");

        String line = null;
        String[] titles = null;
        StringBuilder sb = new StringBuilder();

        //Make document with firstLine
        HashMap<String, String> entry = new HashMap<String, String>();

        try {
            line = opFile.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(line != null) {
            titles = line.split("\\s+");
        }
        else{
            try {
                throw new IOException("Line was empty");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        for (String title : titles){
            System.out.print(title + " ");
        }
        System.out.println("\n");
        */

        try {
            while((line=opFile.readLine()) != null){

                // Split the line to begin with
                String[] dataLine = line.split("\\s+");

                // We use element to cycle through the array of data values
                int element = 0;
                // We use titleNum to iterate through title collection
                int titleNum = 0;

                while (titleNum < titles.length) {
                    String curTitle = titles[titleNum];

                    // If we have one of those entries where there's a double entry
                    // we increment element again and concat with a space the two.
                    switch(curTitle){
                        // Redundant to store first three again
                        /* case "STN---":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "WBAN":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        */
                        case "YEARMODA":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "TEMP":
                            entry.put(curTitle,dataLine[element] + " " + dataLine[++element]);
                            break;
                        case "DEWP":
                            entry.put(curTitle,dataLine[element] + " " + dataLine[++element]);
                            break;
                        case "SLP":
                            entry.put(curTitle,dataLine[element] + " " + dataLine[++element]);
                            break;
                        case "STP":
                            entry.put(curTitle,dataLine[element] + " " + dataLine[++element]);
                            break;
                        case "VISIB":
                            entry.put(curTitle,dataLine[element] + " " + dataLine[++element]);
                            break;
                        case "WDSP":
                            entry.put(curTitle,dataLine[element] + " " + dataLine[++element]);
                            break;
                        case "MXSPD":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "GUST":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "MAX":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "MIN":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "PRCP":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "SNDP":
                            entry.put(curTitle,dataLine[element]);
                            break;
                        case "FRSHTT":
                            entry.put(curTitle,dataLine[element]);
                            break;

                    }
                    // Line up to next element
                    element++;
                    titleNum++;
                }

                entry.put("type", "measurement");
                JsonObject doc = JsonObject.from(entry);
                //Key format is STN::WBAN::YMD
                String key = dataLine[0] + "::" + dataLine[1] + "::" + dataLine[2];
                myClient.insert(JsonDocument.create(key, doc));

                // Interestingly, doing the get here synchronises the future, so it slows stuff down quite a bit,
                // but at least we deal with the errors.


                /*try {
                    if (wasSaved.get().booleanValue()) {
                        // Fine, don't need to say anything.
                    } else {

                        throw new RuntimeException("key: " + key + ", already exists in the system");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
