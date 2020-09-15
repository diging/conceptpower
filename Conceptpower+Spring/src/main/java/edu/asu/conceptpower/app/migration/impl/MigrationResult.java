package edu.asu.conceptpower.app.migration.impl;

import java.time.ZonedDateTime;

/**
 * Class to keep track of details about ongoing migration
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

public class MigrationResult{
    private int migratedFiles;
    private ZonedDateTime finished;
    
    public MigrationResult(int migratedFiles, ZonedDateTime finished) {
        super();
        this.migratedFiles = migratedFiles;
        this.finished = finished;
    }
    
    public int getMigratedFiles() {
        return migratedFiles;
    }
    public void setMigratedFiles(int migratedFiles) {
        this.migratedFiles = migratedFiles;
    }
    public ZonedDateTime getFinished() {
        return finished;
    }
    public void setFinished(ZonedDateTime finshed) {
        this.finished = finshed;
    }
}