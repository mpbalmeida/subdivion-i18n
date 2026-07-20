package dev.marcosalmeida.i18n.generator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubdivisionEntry {
    private String code;
    private String name;
    private String category;
    @JsonProperty(required = false)
    private String parent;

    public SubdivisionEntry() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
}
