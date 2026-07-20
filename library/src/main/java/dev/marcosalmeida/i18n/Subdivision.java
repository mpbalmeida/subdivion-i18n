package dev.marcosalmeida.i18n;

import java.util.Optional;

/**
 * Interface representing an ISO-3166-2 subdivision.
 */
public interface Subdivision {
    /**
     * Returns the full ISO-3166-2 code (e.g., "US-AL").
     *
     * @return the subdivision code.
     */
    String getCode();

    /**
     * Returns the English name of the subdivision.
     *
     * @return the subdivision name.
     */
    String getSubdivisionName();

    /**
     * Returns the category of the subdivision (e.g., "State", "District", "Outlying area").
     *
     * @return the subdivision category.
     */
    String getCategory();

    /**
     * Returns the subdivision part of the ISO-3166-2 code (e.g., "AL" from "US-AL").
     *
     * @return the subdivision code part.
     */
    default String getSubdivisionCode() {
        if (this instanceof Enum<?> e) {
            return e.name();
        }

        return getCode();
    }

    /**
     * Returns the parent subdivision, if any.
     *
     * @return an Optional containing the parent subdivision, or empty if none.
     */
    default Optional<Subdivision> getParent() {
        return Optional.empty();
    }
}
