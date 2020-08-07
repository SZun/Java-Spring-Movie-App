package com.sgz.server.repos;

import com.sgz.server.entities.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerRepoTest {

    @Mock
    private CustomerRepo toTest;

    private final UUID id = new UUID(36, 36);

    private final Customer expectedCustomer = new Customer(this.id, "Sam", true, "1234567890");

    @Test
    void save() {
        given(toTest.save(any(Customer.class))).willReturn(expectedCustomer);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        Customer fromRepo = toTest.save(expectedCustomer);

        verify(toTest).save(captor.capture());

        Customer expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals("Sam", expectedParam.getName());
        assertTrue(expectedParam.isGold());
        assertEquals("1234567890", expectedParam.getPhone());

        assertEquals(expectedCustomer, fromRepo);
    }

    @Test
    void findById() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.of(expectedCustomer));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Customer> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedCustomer, fromRepo.get());
    }

    @Test
    void findByName() {
        given(toTest.findByName(anyString())).willReturn(Optional.of(expectedCustomer));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Customer> fromRepo = toTest.findByName("Sam");

        verify(toTest).findByName(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Sam", expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedCustomer, fromRepo.get());
    }

    @Test
    void findByNameEmpty() {
        given(toTest.findByName(anyString())).willReturn(Optional.empty());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Customer> fromRepo = toTest.findByName("Sam");

        verify(toTest).findByName(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Sam", expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void existsById() {
        given(toTest.existsById(any(UUID.class))).willReturn(true);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        boolean fromRepo = toTest.existsById(id);

        verify(toTest).existsById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void existsByName() {
        given(toTest.existsByName(anyString())).willReturn(true);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        boolean fromRepo = toTest.existsByName("Sam");

        verify(toTest).existsByName(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Sam", expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void findByIdEmpty() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.empty());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Customer> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void deleteById() {
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        toTest.deleteById(id);

        verify(toTest).deleteById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
    }

    @Test
    void findAll() {
        final List<Customer> expectedGenres = Arrays.asList(expectedCustomer);

        given(toTest.findAll()).willReturn(expectedGenres);

        List<Customer> fromRepo = toTest.findAll();

        verify(toTest).findAll();

        assertEquals(expectedGenres, fromRepo);
    }

}