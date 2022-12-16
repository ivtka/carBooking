package org.jadatix.carbooking.controller;

import org.jadatix.carbooking.api.v1.request.OfficeRequest;
import org.jadatix.carbooking.api.v1.response.OfficeResponse;
import org.jadatix.carbooking.builder.OfficeBuilder;
import org.jadatix.carbooking.model.Office;
import org.jadatix.carbooking.service.OfficeService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@WebMvcTest
public class OfficeControllerTest extends AbstractControllerTest<Office, OfficeRequest, OfficeResponse> {
    @MockBean
    private OfficeService officeService;

    @Override
    protected String getControllerPath() {
        return "/api/v1/offices";
    }

    @Override
    protected Class<Office> getEntityClass() {
        return Office.class;
    }

    @Override
    protected OfficeService getService() {
        return officeService;
    }

    @Override
    protected List<Function<Office, Object>> getValueToBeUpdated(OfficeRequest request) {
        return List.of(Office::getCity, Office::getStreet);
    }

    @Override
    protected Map<List<String>, Sort> getSortingTestParameters() {
        return Map.of(
                List.of("street"), Sort.by("street").ascending(),
                List.of("street,desc"), Sort.by("street").descending(),
                List.of("city"), Sort.by("city").ascending(),
                List.of("city,desc"), Sort.by("city").descending()
        );
    }

    @Override
    protected Office getNewEntity() {
        return OfficeBuilder.builder().build();
    }

    @Override
    protected OfficeRequest getNewRequest() {
        return new OfficeRequest(getNewEntity());
    }

    @Override
    protected OfficeRequest convertToRequest(Office office) {
        return new OfficeRequest(office);
    }

    @Override
    protected OfficeResponse convertToResponse(Office office) {
        return new OfficeResponse(office);
    }
}
