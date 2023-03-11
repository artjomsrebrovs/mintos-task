package com.mintos.task.common.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class GenericResponse<T> {

    private final boolean success;

    private final String errorMessage;

    private final T response;
}
