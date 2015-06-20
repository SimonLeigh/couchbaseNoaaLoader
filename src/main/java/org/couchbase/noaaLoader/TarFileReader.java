package org.couchbase.noaaLoader;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.*;

/**
 * Created by simon on 20/03/15.
 */
public class TarFileReader {

    public static void getBufferedReader (InputStream iStream){

        TarArchiveInputStream is=null;

        is = new TarArchiveInputStream(iStream);

        ArchiveEntry entryx = null;

        try {
            while((entryx = is.getNextEntry()) != null) {
                if (entryx.isDirectory()) continue;
                else {
                    //System.out.println(entryx.getName());
                    if ( entryx.getName().endsWith("op.gz")){
                        System.out.println("Inserting data from:" + entryx.getName());
                        OpFileReader.getBufferedReader(new SizeLimitInputStream(is,entryx.getSize()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
