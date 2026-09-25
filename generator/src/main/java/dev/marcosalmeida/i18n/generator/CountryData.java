package dev.marcosalmeida.i18n.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The parsed contents of one country's ISO-3166-2 data file, as found under
 * {@code data/<continent>/<cc>.json}.
 *
 * <p>Corresponds to a JSON document such as:
 * <pre>{@code
 * {
 *   "country": "US",
 *   "name": "United States",
 *   "wikipedia": "https://en.wikipedia.org/wiki/ISO_3166-2:US",
 *   "dateAdded": "2026-01-01",
 *   "lastUpdated": "2026-01-01",
 *   "subdivisions": [ {"code": "AL", "name": "Alabama", "category": "state"} ]
 * }
 * }</pre>
 *
 * <p>Instances are populated by Jackson and consumed by
 * {@link SubdivisionCodeGenerator}; they carry no behaviour of their own.
 */
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

    /** Creates an empty instance, populated afterwards by Jackson. */
    public CountryData() {}

    /**
     * Returns the ISO 3166-1 alpha-2 country code.
     *
     * <p>This becomes the name of the generated nested enum, and the prefix of every
     * subdivision code within it.
     *
     * @return the two-letter country code, for example {@code "US"}
     */
    public String getCountry() { return country; }

    /**
     * Sets the ISO 3166-1 alpha-2 country code.
     *
     * @param country the two-letter country code
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Returns the human-readable country name.
     *
     * @return the country name, for example {@code "United States"}
     */
    public String getName() { return name; }

    /**
     * Sets the human-readable country name.
     *
     * @param name the country name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the Wikipedia URL for this country's ISO 3166-2 entry, or {@code null}.
     *
     * @return the Wikipedia URL, or {@code null} if the data file omits it
     */
    public String getWikipedia() { return wikipedia; }

    /**
     * Sets the Wikipedia URL for this country's ISO 3166-2 entry.
     *
     * @param wikipedia the Wikipedia URL
     */
    public void setWikipedia(String wikipedia) { this.wikipedia = wikipedia; }

    /**
     * Returns the date this country's data was first added, or {@code null}.
     *
     * @return an ISO-8601 date string such as {@code "2026-01-01"}, or {@code null}
     */
    public String getDateAdded() { return dateAdded; }

    /**
     * Sets the date this country's data was first added.
     *
     * @param dateAdded an ISO-8601 date string
     */
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    /**
     * Returns the date this country's data was last revised, or {@code null}.
     *
     * @return an ISO-8601 date string such as {@code "2026-01-01"}, or {@code null}
     */
    public String getLastUpdated() { return lastUpdated; }

    /**
     * Sets the date this country's data was last revised.
     *
     * @param lastUpdated an ISO-8601 date string
     */
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    /**
     * Returns the country's subdivisions, in the order they appear in the data file.
     *
     * <p>The generated enum preserves this order, so the file is expected to list
     * subdivisions alphabetically by code.
     *
     * @return the subdivisions; never {@code null} once Jackson has populated the instance
     */
    public List<SubdivisionEntry> getSubdivisions() { return subdivisions; }

    /**
     * Sets the country's subdivisions.
     *
     * @param subdivisions the subdivisions, ordered alphabetically by code
     */
    public void setSubdivisions(List<SubdivisionEntry> subdivisions) { this.subdivisions = subdivisions; }
}
