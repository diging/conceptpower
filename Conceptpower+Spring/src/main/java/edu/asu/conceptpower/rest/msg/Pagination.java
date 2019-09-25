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
    private Integer totalNumberOfPages;

    public Pagination(Integer pageNumber, Integer totalNumberOfRecords,Integer totalNumberOfPages) {
        this.pageNumber = pageNumber;
        this.totalNumberOfRecords = totalNumberOfRecords;
        this.totalNumberOfPages = totalNumberOfPages;
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
    
    public Integer getTotalNumberofPages() {
        return totalNumberOfPages;
    }
    
    public void setTotalNumberOfPages(Integer totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }
}
