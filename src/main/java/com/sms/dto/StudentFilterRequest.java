package com.sms.dto;

public class StudentFilterRequest {
    private String search = "";
    private String className = "";
    private String section = "";
    private String status = "";
    private int page = 0;
    private int size = 10;

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
