package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Address;
import com.capgemini.setrack.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class AddressControllerTest {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressRepository addressRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    public void goodCreateAddressTest() throws Exception {
        Address address = new Address("Teststraat", "1a", "6666AA", "amsterdam", "nederland");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);

        this.mockMvc.perform(post("/api/address/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(jsonPath("$.id", is((int)address.getId())))
                .andExpect(jsonPath("$.street", is(address.getStreet())))
                .andExpect(jsonPath("$.houseNumber", is(address.getHouseNumber())))
                .andExpect(jsonPath("$.postalCode", is(address.getPostalCode())))
                .andExpect(jsonPath("$.city", is(address.getCity())))
                .andExpect(jsonPath("$.country", is(address.getCountry())))
                .andExpect(status().isOk());
        verify(addressRepository,times(1)).save(Mockito.any(Address.class));
    }
    @Test
    public void badCreateAddressTest() throws Exception {
        Address address = new Address("Teststraat", "1a", "6666A", "amsterdam", "nederland");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);

        this.mockMvc.perform(post("/api/address/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(addressRepository,times(0)).save(Mockito.any(Address.class));
    }

    @Test
    public void goodEditAddressTest() throws Exception {
        Address address = new Address("Teststraat", "1a", "6666AA", "amsterdam", "nederland");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);

        this.mockMvc.perform(post("/api/address/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(jsonPath("$.id", is((int)address.getId())))
                .andExpect(jsonPath("$.street", is(address.getStreet())))
                .andExpect(jsonPath("$.houseNumber", is(address.getHouseNumber())))
                .andExpect(jsonPath("$.postalCode", is(address.getPostalCode())))
                .andExpect(jsonPath("$.city", is(address.getCity())))
                .andExpect(jsonPath("$.country", is(address.getCountry())))
                .andExpect(status().isOk());
        verify(addressRepository,times(1)).save(Mockito.any(Address.class));
    }
    @Test
    public void badEditAddressTest() throws Exception {
        Address address = new Address("Teststraat", "1a", "6666A", "amsterdam", "nederland");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);

        this.mockMvc.perform(delete("/api/address/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(addressRepository,times(0)).save(Mockito.any(Address.class));
    }

    @Test
    public void goodDeleteAddressTest() throws Exception {
        Address address = new Address("Teststraat", "1a", "6666AA", "amsterdam", "nederland");
        doNothing().when(addressRepository).delete(address.getId());

        this.mockMvc.perform(delete("/api/address/delete/{id}", address.getId()))
                .andExpect(status().isOk());
            verify(addressRepository, times(1)).delete(address.getId());
        }

    @Test
    public void test_get_all_success() throws Exception{
        List<Address> addresses = new ArrayList<Address>();
        Address address = new Address("Teststraat", "1a", "6666AA", "amsterdam", "nederland");
        addresses.add(address);
        when(addressRepository.findAll()).thenReturn(addresses);
        this.mockMvc.perform(get("/api/address/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is((int)address.getId())))
                .andExpect(jsonPath("$[0].street", is(address.getStreet())))
                .andExpect(jsonPath("$[0].houseNumber", is(address.getHouseNumber())))
                .andExpect(jsonPath("$[0].postalCode", is(address.getPostalCode())))
                .andExpect(jsonPath("$[0].city", is(address.getCity())))
                .andExpect(jsonPath("$[0].country", is(address.getCountry())));
        verify(addressRepository, times(1)).findAll();
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    public void testDeleteAddressEmptyResultDataAccessException() throws Exception {
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(addressRepository).delete(Mockito.anyLong());

        this.mockMvc.perform(delete("/api/address/delete/1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(addressRepository,times(0)).delete((long)2);
    }

    }
