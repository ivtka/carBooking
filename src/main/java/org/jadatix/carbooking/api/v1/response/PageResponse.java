package org.jadatix.carbooking.api.v1.response;

import java.util.List;

public class PageResponse<T> {

    List<T> items;
    long totalElements;
    long index;
    long size;

    public PageResponse() {
        // it is needed for JSON parsing
    }

    public PageResponse(
            List<T> items,
            long totalElements,
            long index,
            long size) {
        this.items = items;
        this.totalElements = totalElements;
        this.index = index;
        this.size = size;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<T> getItems() {
        return items;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
