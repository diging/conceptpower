package edu.asu.conceptpower.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public void setUp() {

        logger.debug("Starting with loading files from dropbox to " + toLocation + ".");
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        URL website = null;
        try {
            logger.debug("Loading concept lists database from : " + conceptListUrl);
            website = new URL(conceptListUrl);
            rbc = Channels.newChannel(website.openStream());
            File conceptListFile = new File(toLocation + "/conceptLists.db");
            conceptListFile.createNewFile(); // if file already exists will
                                             // do
                                             // nothing
            fos = new FileOutputStream(conceptListFile, false);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("Finished loading files from dropbox for concept lists database.");
        } catch (MalformedURLException e) {
            logger.error("MalformedUrlException error for concept lists", e);
        } catch (FileNotFoundException fne) {
            logger.error("FileNotFoundException error for concept lists", fne);
        } catch (IOException ioE) {
            logger.error("IOException error for concept lists", ioE);
        } finally {
            logger.debug("Closing resources for concept lists database.");
            try {
                logger.error("Closing rbc resource for concept lists database.");
                rbc.close();
                fos.close();
            } catch (IOException e) {
                logger.error("IOException for concept lists database.", e);
            }
        }

        try {
            logger.debug("Loading concept types database from : " + conceptTypesUrl);
            website = new URL(conceptTypesUrl);
            rbc = Channels.newChannel(website.openStream());
            File conceptypesFile = new File(toLocation + "/conceptTypes.db");
            conceptypesFile.createNewFile(); // if file already exists will
                                             // do
                                             // nothing
            fos = new FileOutputStream(conceptypesFile, false);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("Finished loading files from dropbox for concept types database.");
        } catch (MalformedURLException e) {
            logger.error("MalformedUrlException error for concept types", e);
        } catch (IOException e) {
            logger.error("IOException error for concept type", e);
        } finally {
            logger.debug("Closing resources for concept types database.");
            try {
                rbc.close();
                fos.close();
            } catch (IOException e) {
                logger.error("IOException for concept types database.", e);
            }
        }

        try {
            logger.debug("Loading users database from : " + usersUrl);
            website = new URL(usersUrl);
            rbc = Channels.newChannel(website.openStream());
            File userFile = new File(toLocation + "/users.db");
            userFile.createNewFile(); // if file already exists will do
                                      // nothing
            fos = new FileOutputStream(userFile, false);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("Finished loading files from dropbox for users database.");
        } catch (MalformedURLException e) {
            logger.error("MalformedUrlException error for users", e);
        } catch (IOException e) {
            logger.error("IOException error for users", e);
        } finally {
            logger.debug("Closing resources for users database.");
            try {
                rbc.close();
                fos.close();
            } catch (IOException e) {
                logger.error("IOException for users database.", e);
            }
        }
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

}
