package org.jadatix.carbooking.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jadatix.carbooking.model.Office;

public class OfficeRequest extends AbstractRequest<Office> {
    @Schema(description = "Office id", example = "1")
    private Long id;
    @Schema(description = "Office city", example = "Kyiv")
    private String city;
    @Schema(description = "Office street", example = "Khreshchatyk")
    private String street;

    public OfficeRequest() {

    }

    public OfficeRequest(Office office) {
        this.id = office.getId();
        this.city = office.getCity();
        this.street = office.getStreet();
    }

    @Override
    public Office create() {
        Office office = new Office(city,street);
        office.setId(id);
        return office;
    }

    @Override
    public Office update(Office office) {
        office.setCity(city);
        office.setStreet(street);
        return office;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
