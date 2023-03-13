package com.mintos.task.common.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public final class GenericResponse<T> {

    private final boolean success;

    private final String errorMessage;

    private final T response;
}
