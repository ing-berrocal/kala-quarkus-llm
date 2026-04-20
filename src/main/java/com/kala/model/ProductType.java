package com.kala.model;

public enum ProductType {
    CAMISAS,
    BODIES;

    public static ProductType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("product is required");
        }

        try {
            return ProductType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("invalid product. allowed values: CAMISAS, BODIES");
        }
    }
}
