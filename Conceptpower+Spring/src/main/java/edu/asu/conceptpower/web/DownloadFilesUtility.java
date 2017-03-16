package edu.asu.conceptpower.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.springframework.beans.factory.annotation.Value;

/**
 * This class is used for downloading the database file for integration tests
 * from dropbox.
 * 
 * @author karthikeyanmohan
 *
 */
public class DownloadFilesUtility {

    @Value("${dbTestPath}")
    private String toLocation;

    @Value("${conceptListUrl}")
    private String conceptListUrl;

    @Value("${conceptTypesUrl}")
    private String conceptTypesUrl;

    @Value("${usersUrl}")
    private String usersUrl;

    /**
     * This method is called while creating DownloadFilesUtility bean. This
     * method fetches the conceptLists.db, conceptTypes.db and users.db from
     * dropbox. The database files fetched from dropbox location will be placed
     * based on the value of "toLocation" variable, which is configured in
     * pom.xml. The dropbox urls are also configured in pom.xml
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {

        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        URL website = null;
        try {
            website = new URL(conceptListUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/conceptLists.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

        try {
            website = new URL(conceptTypesUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/conceptTypes.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

        try {
            website = new URL(usersUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/users.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

}