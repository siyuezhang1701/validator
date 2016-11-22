package com.thoughtworks.ketsu.domain;

import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import static javax.ws.rs.core.UriBuilder.fromUri;

public class PaginatedList<T> {
    private static int MAX_PER_PAGE = 100;
    private static int MIN_PER_PAGE = 1;
    private static int DEFAULT_PER_PAGE = 10;

    private long size;
    private BiFunction<Integer, Integer, List<T>> provider;
    private String pageableUri;

    public PaginatedList(long size, BiFunction<Integer, Integer, List<T>> provider) {
        this.size = size;
        this.provider = provider;
        this.pageableUri = "";
    }

    public List<T> page(int page, int perPage) {
        return provider.apply(getConsolidatedPage(page, perPage) - 1, getConsolidatedPerPage(perPage));
    }

    public long size() {
        return size;
    }

    private int getConsolidatedPage(int page, int perPage) {
        int totalPagesCount = (int) getTotalPagesCount(getConsolidatedPerPage(perPage));
        return page < 1 || page > totalPagesCount ? totalPagesCount : page;
    }

    private int getConsolidatedPerPage(int perPage) {
        if (perPage > MAX_PER_PAGE) return MAX_PER_PAGE;
        if (perPage < MIN_PER_PAGE) return DEFAULT_PER_PAGE;
        return perPage;
    }

    public Page<T> toPage(int page, int perPage, UriInfo uri) {
        this.pageableUri = templatizePageUri(uri);

        int consolidatedPerPage = getConsolidatedPerPage(perPage);
        int consolidatedPage = getConsolidatedPage(page, perPage);

        String first = getFirstPageUri(consolidatedPerPage);
        String last = getLastPageUri(consolidatedPerPage);
        String prev = getPrevPageUri(consolidatedPage, consolidatedPerPage);
        String next = getNextPageUri(consolidatedPage, consolidatedPerPage);
        String current = getCurrentPageUri(consolidatedPage, consolidatedPerPage);

        return new Page<>(page(consolidatedPage, consolidatedPerPage),
                size, first, last, prev, next, current);
    }



    private long getTotalPagesCount(int perPage) {
        return (size - 1) / perPage + 1;
    }

    public String templatizePageUri(UriInfo uriInfo) {
        return fromUri(uriInfo.getRequestUri())
                .replaceQueryParam("per-page", "{perPage}")
                .replaceQueryParam("page", "{page}")
                .toTemplate();
    }

    private String getCurrentPageUri(int page, int perPage) {
        return build(page, perPage);
    }

    private String getFirstPageUri(int perPage) {
        return size > 0 ? build(1, perPage) : "";
    }

    private String getLastPageUri(int perPage) {
        return size > 0 ? build((int) getTotalPagesCount(perPage), perPage) : "";
    }

    private String getPrevPageUri(int page, int perPage) {
        return size > 0 && page > 1 ? build(page - 1, perPage) : "";
    }

    private String getNextPageUri(int page, int perPage) {
        return page < getTotalPagesCount(perPage) ? build(page + 1, perPage) : "";
    }

    private String build(int page, int perPage) {
        return fromUri(pageableUri).buildFromMap(new HashMap<String, Object>() {{
            put("perPage", perPage);
            put("page", page);
        }}).toASCIIString();
    }

}

