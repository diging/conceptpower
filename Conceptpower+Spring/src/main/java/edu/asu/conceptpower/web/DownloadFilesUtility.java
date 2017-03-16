package edu.asu.conceptpower.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * This class is used for downloading the database file for integration tests
 * from dropbox.
 * 
 * @author karthikeyanmohan
 *
 */
public class DownloadFilesUtility {

    private static final Logger logger = LoggerFactory.getLogger(DownloadFilesUtility.class);

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

        logger.debug("Starting with loading files from dropbox to " + toLocation + ".");

        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        URL website = null;
        try {
            logger.debug("Loading concept lists database from : " + conceptListUrl);
            website = new URL(conceptListUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/conceptLists.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("Finished loading files from dropbox for concept lists database.");
        } finally {
            logger.debug("Closing resources for concept lists database.");
            rbc.close();
            fos.close();
        }

        try {
            logger.debug("Loading concept types database from : " + conceptTypesUrl);
            website = new URL(conceptTypesUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/conceptTypes.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("Finished loading files from dropbox for concept types database.");
        } finally {
            logger.debug("Closing resources for concept types database.");
            rbc.close();
            fos.close();
        }

        try {
            logger.debug("Loading users database from : " + usersUrl);
            website = new URL(usersUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/users.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("Finished loading files from dropbox for users database.");
        } finally {
            logger.debug("Closing resources for users database.");
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