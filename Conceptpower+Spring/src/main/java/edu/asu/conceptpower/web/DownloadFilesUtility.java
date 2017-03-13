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

    public void setUp() throws IOException {

        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        URL website = null;
        try {
            website = new URL("https://www.dropbox.com/s/t2g2kclkwglv2lp/conceptLists.db?raw=1");
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/conceptLists.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

        try {
            website = new URL("https://www.dropbox.com/s/n4er1qbfk4d641z/conceptTypes.db?raw=1");
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/conceptTypes.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

        try {
            website = new URL("https://www.dropbox.com/s/idvg1f9y8k3es5i/lucene.db?raw=1");
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/lucene.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

        try {
            website = new URL("https://www.dropbox.com/s/v371ww5hqnxxjek/users.db?raw=1");
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toLocation + "/users.db");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } finally {
            rbc.close();
            fos.close();
        }

        try {
            website = new URL("https://www.dropbox.com/s/s2wss0ujzxrwn9c/wordnetCache.db?raw=1");
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
