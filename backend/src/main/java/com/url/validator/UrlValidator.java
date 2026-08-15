package com.url.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;

public class UrlValidator
        implements ConstraintValidator<ValidUrl, String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {

        if (value == null || value.isBlank()) {
            return false;
        }

        try {

            URI uri = URI.create(value);

            String scheme = uri.getScheme();

            return ("http".equalsIgnoreCase(scheme)
                    || "https".equalsIgnoreCase(scheme))
                    && uri.getHost() != null;

        } catch (Exception exception) {

            return false;
        }
    }
}
