package org.couchbase.noaaLoader;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by simon on 21/03/15.
 */
public class NoaaCSVInserter {

    /*
        Method creates and inserts documents for each line in opFile based on headers.
     */
    public static void insert(FileInputStream csvFile, Bucket myClient) {

        Reader decoder = new InputStreamReader(csvFile);
        BufferedReader csvReader = new BufferedReader (decoder);

        String line = null;
        String[] titles = null;
        StringBuilder sb = new StringBuilder();

        //Make document with firstLine
        HashMap<String, String> entry = new HashMap<String, String>();

        try {
            line = csvReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(line != null) {
            titles = line.split(",");
        }
        else{
            try {
                throw new IOException("Line was empty");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for (String title : titles){
            System.out.print(title + " ");
        }
        System.out.println("\n");


        try {
            while((line=csvReader.readLine()) != null) {

                // Split the line to begin with
                String[] dataLine = line.split(",");

                // We use element to cycle through the array of data values
                int element = 0;
                // We use titleNum to iterate through title collection
                int titleNum = 0;

                while (titleNum < titles.length) {
                    String curTitle = titles[titleNum];

                    String toInsert = dataLine[element].replaceAll("^\"|\"$", "");
                    if (!toInsert.isEmpty()) {
                        entry.put(curTitle.replaceAll("^\"|\"$", ""), toInsert);
                    }


                    // Line up to next element
                    element++;
                    titleNum++;
                }

                entry.put("type", "station");
                JsonObject doc = JsonObject.from(entry);
                //Key format is STN::WBAN
                String key = "station::" + dataLine[0].replaceAll("^\"|\"$", "") + "-" +
                                            dataLine[1].replaceAll("^\"|\"$", "");
                                            //dataLine[2].replaceAll("^\"|\"$", "");
                System.out.println(key);
                myClient.upsert(JsonDocument.create(key, doc));


            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
