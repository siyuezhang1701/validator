package com.thoughtworks.ketsu.domain;

import java.util.List;

public class Page<T> {
    private List<T> entries;
    protected String first;
    protected String last;
    protected String prev;
    protected String next;
    private long totalCount;
    private String current;

    public Page(List<T> entries, long size, String first, String last, String prev, String next, String current) {
        this.entries = entries;
        this.totalCount = size;
        this.first = first;
        this.last = last;
        this.prev = prev;
        this.next = next;
        this.current = current;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getPrev() {
        return prev;
    }

    public String getNext() {
        return next;
    }

    public List<T> getItems() {
        return entries;
    }


    public long getTotalCount() {
        return totalCount;
    }

    public String getCurrent() {
        return current;
    }
}


