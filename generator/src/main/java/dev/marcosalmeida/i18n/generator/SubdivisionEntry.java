package dev.marcosalmeida.i18n.generator;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One entry of the {@code subdivisions} array in a country's ISO-3166-2 data file.
 *
 * <p>Corresponds to a single JSON object such as:
 * <pre>{@code
 * {"code": "AL", "name": "Alabama", "category": "state"}
 * }</pre>
 *
 * <p>Instances are populated by Jackson and consumed by
 * {@link SubdivisionCodeGenerator}; they carry no behaviour of their own.
 */
public class SubdivisionEntry {
    private String code;
    private String name;
    private String category;
    @JsonProperty(required = false)
    private String parent;

    /** Creates an empty entry, populated afterwards by Jackson. */
    public SubdivisionEntry() {}

    /**
     * Returns the subdivision part of the ISO-3166-2 code, without the country prefix.
     *
     * @return the subdivision code, for example {@code "AL"} for {@code US-AL}
     */
    public String getCode() { return code; }

    /**
     * Sets the subdivision part of the ISO-3166-2 code.
     *
     * @param code the subdivision code, without the country prefix
     */
    public void setCode(String code) { this.code = code; }

    /**
     * Returns the subdivision name as published in ISO 3166-2.
     *
     * @return the subdivision name, for example {@code "Alabama"}
     */
    public String getName() { return name; }

    /**
     * Sets the subdivision name.
     *
     * @param name the subdivision name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the subdivision category.
     *
     * <p>Categories are lowercase for generic types ({@code "state"}, {@code "province"})
     * and Title Case for proper-noun types ({@code "Province"}, {@code "Land"}). The
     * generator derives its per-category filter methods from this value, so changing it
     * changes the generated API.
     *
     * @return the subdivision category
     */
    public String getCategory() { return category; }

    /**
     * Sets the subdivision category.
     *
     * @param category the subdivision category
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * Returns the code of this subdivision's parent, or {@code null} when it has none.
     *
     * <p>Optional. When present, the value must match the {@code code} of another entry
     * in the same data file; the generator rejects the file otherwise.
     *
     * @return the parent subdivision code, or {@code null} if this subdivision is top level
     */
    public String getParent() { return parent; }

    /**
     * Sets the parent subdivision code.
     *
     * @param parent the code of another subdivision in the same data file, or {@code null}
     */
    public void setParent(String parent) { this.parent = parent; }
}
