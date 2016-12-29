package edu.asu.conceptpower.app.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:pos.properties")
public class POSMessage {

    private Map<String, String> posMap = new HashMap<>();

    @PostConstruct
    public void init() {
        ResourceBundle rb = ResourceBundle.getBundle("pos");
        Enumeration <String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            posMap.put(key, rb.getString(key));
        }
    }

    public Map<String, String> getPOSMap() {
        return posMap;
    }
}
