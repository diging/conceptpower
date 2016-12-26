package edu.asu.conceptpower.rest.msg;

/**
 * This class contains the pagination details. This class will be serialized
 * using Jackson
 * 
 * @author karthikeyanmohan
 *
 */
public class Pagination {

    private Integer pageNumber;
    private Integer totalNumberOfRecords;

    public Pagination(Integer pageNumber, Integer totalNumberOfRecords) {
        this.pageNumber = pageNumber;
        this.totalNumberOfRecords = totalNumberOfRecords;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    public void setTotalNumberOfRecords(Integer totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }
}
