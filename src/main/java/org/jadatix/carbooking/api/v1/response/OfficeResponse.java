package org.jadatix.carbooking.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jadatix.carbooking.model.Office;

public class OfficeResponse extends AbstractResponse {
    @Schema(description = "Office city", example = "Kyiv")
    private String city;
    @Schema(description = "Office street", example = "Khreshchatyk")
    private String street;

    public OfficeResponse(Office office) {
        super(office.getId());
        this.city = office.getCity();
        this.street = office.getStreet();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
