package org.hyphen.ross.model;

import org.joda.time.LocalDate;

/**
 * Represents a Price at a given Date
 */
public class PriceRecord {
    private LocalDate date;
    private Double price;

    public PriceRecord(LocalDate date, Double price) {
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
