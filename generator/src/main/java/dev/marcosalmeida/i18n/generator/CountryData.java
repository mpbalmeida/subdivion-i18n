package dev.marcosalmeida.i18n.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CountryData {
    private String country;
    private String name;
    @JsonProperty(required = false)
    private String wikipedia;
    @JsonProperty(required = false)
    private String dateAdded;
    @JsonProperty(required = false)
    private String lastUpdated;
    private List<SubdivisionEntry> subdivisions;

    public CountryData() {}

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWikipedia() { return wikipedia; }
    public void setWikipedia(String wikipedia) { this.wikipedia = wikipedia; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public List<SubdivisionEntry> getSubdivisions() { return subdivisions; }
    public void setSubdivisions(List<SubdivisionEntry> subdivisions) { this.subdivisions = subdivisions; }
}
