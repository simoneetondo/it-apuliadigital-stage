package it.exprivia.models.dtos;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        long totalElements,
        long totalPage,
        int page,
        int size
) {
}
