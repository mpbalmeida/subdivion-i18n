package dev.marcosalmeida.i18n.generator;

import java.util.List;

public class CountryData {
    private String country;
    private String name;
    private List<SubdivisionEntry> subdivisions;

    public CountryData() {}

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<SubdivisionEntry> getSubdivisions() { return subdivisions; }
    public void setSubdivisions(List<SubdivisionEntry> subdivisions) { this.subdivisions = subdivisions; }
}
