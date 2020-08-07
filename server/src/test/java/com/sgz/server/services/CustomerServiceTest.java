package com.sgz.server.services;

import com.sgz.server.entities.Customer;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.InvalidNameException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService toTest;

    @Mock
    private CustomerRepo customerRepo;

    private final UUID id = new UUID(36, 36);

    private final Customer expectedCustomer = new Customer(this.id, "Samuel", false, "1234567890");

    private final String testLongStr = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    private final String testShortStr = "1234";

    @Test
    void getAllCustomer() throws NoItemsException {
        final Customer expectedCustomer2 = new Customer(this.id, "Fake name 1", false, "1237567890");
        final Customer expectedCustomer3 = new Customer(this.id, "Fake name 2", true, "1234767890");

        when(customerRepo.findAll()).thenReturn(Arrays.asList(expectedCustomer, expectedCustomer2, expectedCustomer3));

        List<Customer> fromService = toTest.getAllCustomer();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedCustomer));
        assertTrue(fromService.contains(expectedCustomer));
        assertTrue(fromService.contains(expectedCustomer));
    }

    @Test
    void getAllCustomerNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllCustomer());
    }

    @Test
    void getCustomerById() throws InvalidIdException {
        when(customerRepo.findById(any(UUID.class))).thenReturn(Optional.of(expectedCustomer));

        Customer fromService = toTest.getCustomerById(id);

        assertEquals(expectedCustomer, fromService);
    }

    @Test
    void getCustomerByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getCustomerById(UUID.randomUUID()));
    }

    @Test
    void getCustomerByName() throws InvalidNameException, InvalidEntityException {
        when(customerRepo.findByName(anyString())).thenReturn(Optional.of(expectedCustomer));

        Customer fromService = toTest.getCustomerByName("Samuel");

        assertEquals(expectedCustomer, fromService);
    }

    @Test
    void getCustomerByNameInvalidName() {
        assertThrows(InvalidNameException.class, () -> toTest.getCustomerByName("Samuel"));
    }

    @Test
    void getCustomerByNameNullName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getCustomerByName(null));
    }

    @Test
    void getCustomerByNameEmptyName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getCustomerByName(""));
    }

    @Test
    void getCustomerByNameBlankName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getCustomerByName("  "));
    }

    @Test
    void getCustomerByNameTooLongName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getCustomerByName(testLongStr));
    }

    @Test
    void getCustomerByNameTooShortName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getCustomerByName(testShortStr));
    }

    @Test
    void createCustomer() throws InvalidNameException, InvalidEntityException {
        when(customerRepo.save(any(Customer.class))).thenReturn(expectedCustomer);

        Customer fromService = toTest.createCustomer(expectedCustomer);

        assertEquals(expectedCustomer, fromService);
    }

    @Test
    void createCustomerInvalidName() {
        when(customerRepo.existsByName(anyString())).thenReturn(true);
        assertThrows(InvalidNameException.class,  () -> toTest.createCustomer(expectedCustomer));
    }

    @Test
    void createCustomerNullCustomer() {
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(null));
    }

    @Test
    void createCustomerEmptyName() {
        final Customer toCreate = new Customer(this.id, "", false, "1234567890");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerBlankName() {
        final Customer toCreate = new Customer(this.id, "  ", false, "1234567890");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerTooLongName() {
        final Customer toCreate = new Customer(this.id, testLongStr, false, "1234567890");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerTooShortName() {
        final Customer toCreate = new Customer(this.id, testShortStr, false, "1234567890");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerInvalidPhone() {
        final Customer toCreate = new Customer(this.id, "Samuel", false, "abcdefghik");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerEmptyPhone() {
        final Customer toCreate = new Customer(this.id, "Samuel", false, "");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerBlankPhone() {
        final Customer toCreate = new Customer(this.id, "Samuel", false, "  ");
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerTooLongPhone() {
        final Customer toCreate = new Customer(this.id, "Samuel", false, testLongStr);
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void createCustomerTooShortPhone() {
        final Customer toCreate = new Customer(this.id, "Samuel", false, testShortStr);
        assertThrows(InvalidEntityException.class,  () -> toTest.createCustomer(toCreate));
    }

    @Test
    void updateCustomer() throws InvalidEntityException, InvalidIdException, AccessDeniedException {
        when(customerRepo.existsById(any(UUID.class))).thenReturn(true);

        final Customer toEdit = new Customer(this.id, "Zunnnyyy", true, "1234567800");

        when(customerRepo.save(any(Customer.class))).thenReturn(toEdit);

        Customer fromService = toTest.updateCustomer(toEdit, this.id);

        assertEquals(toEdit, fromService);
    }

    @Test
    void updateCustomerNullCustomer() {
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(null, this.id));
    }

    @Test
    void updateCustomerEmptyName() {
        final Customer toEdit = new Customer(this.id, "", true, "1234567800");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerBlankName() {
        final Customer toEdit = new Customer(this.id, "   ", true, "1234567800");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerTooLongName() {
        final Customer toEdit = new Customer(this.id, testLongStr, true, "1234567800");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerTooShortName() {
        final Customer toEdit = new Customer(this.id, testShortStr, true, "1234567800");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerInvalidPhone() {
        final Customer toEdit = new Customer(this.id, "zunnyyyy", true, "abcdefghijk");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerEmptyPhone() {
        final Customer toEdit = new Customer(this.id, "zunnyyyy", true, "");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerBlankPhone() {
        final Customer toEdit = new Customer(this.id, "zunnyyyy", true, "  ");
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerTooLongPhone() {
        final Customer toEdit = new Customer(this.id, "zunnyyyy", true, testLongStr);
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerTooShortPhone() {
        final Customer toEdit = new Customer(this.id, "zunnyyyy", true, testShortStr);
        assertThrows(InvalidEntityException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerInvalidId() {
        final Customer toEdit = new Customer(this.id, "Zunnnyyy", true, "1234567800");
        assertThrows(InvalidIdException.class, () -> toTest.updateCustomer(toEdit, this.id));
    }

    @Test
    void updateCustomerAccessDenied() {
        final Customer toEdit = new Customer(this.id, "Zunnnyyy", true, "1234567800");
        assertThrows(AccessDeniedException.class, () -> toTest.updateCustomer(toEdit, UUID.randomUUID()));
    }

    @Test
    void deleteCustomerById() throws InvalidIdException, AccessDeniedException {
        when(customerRepo.existsById(any(UUID.class))).thenReturn(true);
        toTest.deleteCustomerById(this.id, this.id);
    }

    @Test
    void deleteCustomerByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.deleteCustomerById(this.id, this.id));
    }

    @Test
    void deleteCustomerByIdAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> toTest.deleteCustomerById(this.id, UUID.randomUUID()));
    }
}