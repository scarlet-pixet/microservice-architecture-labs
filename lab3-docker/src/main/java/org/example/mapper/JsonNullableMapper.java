package org.example.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class JsonNullableMapper {

    public <T> T unwrap(JsonNullable<T> nullable) {
        return nullable == null ? null : nullable.orElse(null);
    }

    public <T> JsonNullable<T> wrap(T value) {
        return JsonNullable.of(value);
    }

    @Condition
    public <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}
