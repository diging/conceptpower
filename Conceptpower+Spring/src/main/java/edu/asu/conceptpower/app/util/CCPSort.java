package edu.asu.conceptpower.app.util;

public class CCPSort {

    public enum SortOrder {
        DESCENDING, ASCENDING
    }

    private String sortField;
    private SortOrder sortOrder;

    public CCPSort(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
