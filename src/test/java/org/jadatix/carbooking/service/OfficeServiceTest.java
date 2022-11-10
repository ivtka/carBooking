package org.jadatix.carbooking.service;

import org.jadatix.carbooking.model.Office;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.security.SecureRandom;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OfficeServiceTest {

    @Autowired
    private OfficeService officeService;

    private SecureRandom random = new SecureRandom();

    private Office initOffice() {
        return new Office("Chernivtsi", "Bohdana Khmelnytskoho " + random.nextInt(101 - 1));
    }


    private void assertOffice(Office expected, Office actual) {
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getStreet(), actual.getStreet());
    }

    @Test
    void getAll() {
        List<Office> offices = List.of(initOffice(), initOffice(), initOffice());
        offices.forEach(officeService::save);

        offices.forEach(office -> {
            Office actual = officeService.get(office.getId()).get();

            assertOffice(office, actual);
        });
    }

    @Test
    void save() {
        Office expected = initOffice();
        officeService.save(expected);
        Office actual = officeService.get(expected.getId()).get();

        assertOffice(expected, actual);
    }

    @Test
    void get() {
        Office expected = initOffice();
        officeService.save(expected);
        Office actual = officeService.get(expected.getId()).get();

        assertOffice(expected, actual);
    }

    @Test
    void update() {
        Office expected = initOffice();
        officeService.save(expected);
        expected.setCity("Kyiv");
        officeService.update(expected.getId(), expected);
        Office actual = officeService.get(expected.getId()).get();

        assertOffice(expected, actual);
    }

    @Test
    void delete() {
        Office expected = initOffice();
        officeService.save(expected);
        Long deletedId = expected.getId();
        officeService.delete(deletedId);
        assertEquals(false, officeService.get(deletedId).isPresent());
    }

}