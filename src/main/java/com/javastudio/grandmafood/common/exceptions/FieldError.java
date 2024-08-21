package com.javastudio.grandmafood.common.exceptions;

/**
 * A class to represent a field error for invalid input exceptions.
 */
public record FieldError(String path, String message) {
}
