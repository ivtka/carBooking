package org.jadatix.carbooking.api.v1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.jadatix.carbooking.api.v1.request.OfficeRequest;
import org.jadatix.carbooking.api.v1.response.OfficeResponse;
import org.jadatix.carbooking.model.Office;
import org.jadatix.carbooking.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/offices")
@Tag(name = "Offices", description = "The Offices API")
public class OfficeController extends AbstractController<Office, OfficeRequest, OfficeResponse> {
    @Autowired
    private OfficeService officeService;

    @Override
    protected OfficeService getService() {
        return officeService;
    }

    @Override
    protected OfficeResponse convertToResponse(Office office) {
        return new OfficeResponse(office);
    }
}
