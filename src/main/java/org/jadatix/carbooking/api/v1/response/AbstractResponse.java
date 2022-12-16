package org.jadatix.carbooking.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;

public abstract class AbstractResponse {
    @Schema(description = "Entity identifier", example = "1")
    private Long id;

    public AbstractResponse() {
    }

    public AbstractResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
