package edu.asu.conceptpower.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.springframework.beans.factory.annotation.Value;

public class DownloadFilesUtility {

    @Value("${dbTestPath}")
    private String toLocation;

    @Value("${conceptListUrl}")
    private String conceptListUrl;

    @Value("${conceptTypesUrl}")
    private String conceptTypesUrl;

    @Value("${luceneUrl}")
    private String luceneUrl;

    @Value("${usersUrl}")
    private String usersUrl;

    @Value("${wordnetUrl}")
    private String wordnetCacheUrl;

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
            website = new URL(luceneUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/lucene.db");
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

        try {
            website = new URL(wordnetCacheUrl);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/wordnetCache.db");
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