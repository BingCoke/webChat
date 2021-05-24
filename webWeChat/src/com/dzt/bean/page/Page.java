package com.dzt.bean.page;

import java.util.List;

public class Page<T> {
    private Integer currentPage;
    private Integer totalPages;
    private Integer dataSize;
    private Integer totalDateSize;
    private List<T> data;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getTotalDateSize() {
        return totalDateSize;
    }

    public void setTotalDateSize(Integer totalDateSize) {
        this.totalDateSize = totalDateSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "Page{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", dataSize=" + dataSize +
                ", totalDateSize=" + totalDateSize +
                ", data=" + data +
                '}';
    }
}
