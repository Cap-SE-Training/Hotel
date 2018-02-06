package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Address;
import com.capgemini.setrack.model.Guest;
import com.capgemini.setrack.repository.GuestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class GuestControllerTest {

    @InjectMocks
    private GuestController guestController;

    @Mock
    private GuestRepository guestRepository;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(guestController).build();
    }

    //happy flow
    @Test
    public void testGetAllGuests() throws Exception {
        List<Guest> guests = new ArrayList<Guest>();

        Address address1 = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest1 = new Guest("Kees", "Pieterson", address1, "keespieterson@hotmail.com", "061234567","999999999");
        Guest guest2 = new Guest("Piet", "Keesson", address1, "pietkeesson@hotmail.com", "061234568","999999999");

        guests.add(guest1);
        guests.add(guest2);

        when(guestRepository.findAll()).thenReturn(guests);

        this.mockMvc.perform(get("/api/guests/"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is((int) guests.get(0).getId())))
                .andExpect(jsonPath("$.[0].firstName", is(guests.get(0).getFirstName())))
                .andExpect(jsonPath("$.[0].lastName", is(guests.get(0).getLastName())))
                .andExpect(jsonPath("$.[0].email", is(guests.get(0).getEmail())))
                .andExpect(jsonPath("$.[0].telephoneNumber", is(guests.get(0).getTelephoneNumber())))
                .andExpect(jsonPath("$.[0].documentNumber", is(guests.get(0).getDocumentNumber())))
                .andExpect(jsonPath("$.[1].id", is((int) guests.get(1).getId())))
                .andExpect(jsonPath("$.[1].firstName", is(guests.get(1).getFirstName())))
                .andExpect(jsonPath("$.[1].lastName", is(guests.get(1).getLastName())))
                .andExpect(jsonPath("$.[1].email", is(guests.get(1).getEmail())))
                .andExpect(jsonPath("$.[1].telephoneNumber", is(guests.get(1).getTelephoneNumber())))
                .andExpect(jsonPath("$.[1].documentNumber", is(guests.get(1).getDocumentNumber())))
                .andExpect(status().isOk());
        Mockito.verify(guestRepository,times(1)).findAll();
    }

    //happy flow
    @Test
    public void testCreateGuest() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "06123454567","9999999999");
        //address.setGuest(guest);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(jsonPath("$.id", is((int)guest.getId())))
                .andExpect(jsonPath("$.firstName", is(guest.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(guest.getLastName())))
                .andExpect(jsonPath("$.address.id", is((int)guest.getAddress().getId())))
                //.andExpect(jsonPath("$.id", is((int)guest.getAddress().getGuest().getId())))
                .andExpect(jsonPath("$.email", is(guest.getEmail())))
                .andExpect(jsonPath("$.telephoneNumber", is(guest.getTelephoneNumber())))
                .andExpect(jsonPath("$.documentNumber", is(guest.getDocumentNumber())))
                .andExpect(status().isOk());
        Mockito.verify(guestRepository,times(1)).save(Mockito.any(Guest.class));
    }

    @Test
    public void testCreateGuestExpect400() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234ab", "Amsterdam", "Nederland");
        Guest guest = new Guest("", "Pieterson", address, "keespieterson@hotmail.com", "061234567","9999999999");
        //address.setGuest(guest);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        Mockito.verify(guestRepository,times(0)).save(Mockito.any(Guest.class));
    }

    //happy flow
    @Test
    public void testUpdateGuest() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "06123454567", "9999999999");
        //address.setGuest(guest);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(jsonPath("$.id", is((int)guest.getId())))
                .andExpect(jsonPath("$.firstName", is(guest.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(guest.getLastName())))
                .andExpect(jsonPath("$.address.id", is((int)guest.getAddress().getId())))
                //.andExpect(jsonPath("$.id", is((int)guest.getAddress().getGuest().getId())))
                .andExpect(jsonPath("$.email", is(guest.getEmail())))
                .andExpect(jsonPath("$.telephoneNumber", is(guest.getTelephoneNumber())))
                .andExpect(jsonPath("$.documentNumber", is(guest.getDocumentNumber())))
                .andExpect(status().isOk());
        Mockito.verify(guestRepository,times(1)).save(Mockito.any(Guest.class));
    }

    //happy flow
    @Test
    public void testDeleteGuest() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "061234567","999999999");

        doNothing().when(guestRepository).delete(guest);

        this.mockMvc.perform(delete("/api/guests/delete/{id}", guest.getId()))
                .andExpect(status().isOk());

        Mockito.verify(guestRepository,times(1)).delete(guest.getId());
    }

    @Test
    public void testDeleteGuestExpectEmptyResultDataAccessException() throws Exception {
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(guestRepository).delete(Mockito.anyLong());

        this.mockMvc.perform(delete("/api/guests/delete/1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(guestRepository,times(0)).delete((long)2);
    }

    @Test
    public void testDeleteGuestExpectDataIntegrityViolationException() throws Exception {
        Mockito.doThrow(new DataIntegrityViolationException("DataIntegrityViolationException")).when(guestRepository).delete(Mockito.anyLong());

        this.mockMvc.perform(delete("/api/guests/delete/1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(guestRepository,times(0)).delete((long)2);
    }

    @Test
    public void testCreateGuestExpectDataIntegrityViolationException() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "06123454567","999999999");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.doThrow(new DataIntegrityViolationException("DataIntegrityViolationException")).when(guestRepository).save(Mockito.any(Guest.class));


//        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        Mockito.verify(guestRepository,times(1)).save(Mockito.any(Guest.class));
    }

    @Test
    public void testCreateGuestExpectException() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "06123454567","999999999");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.doThrow(new ArrayIndexOutOfBoundsException("Exception")).when(guestRepository).save(Mockito.any(Guest.class));


//        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        Mockito.verify(guestRepository,times(1)).save(Mockito.any(Guest.class));
    }

    @Test
    public void testUpdateGuestExpectDataIntegrityViolationException() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "06123454567","999999999");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.doThrow(new DataIntegrityViolationException("DataIntegrityViolationException")).when(guestRepository).save(Mockito.any(Guest.class));


//        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        Mockito.verify(guestRepository,times(1)).save(Mockito.any(Guest.class));
    }

    @Test
    public void testUpdateGuestExpectException() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234AB", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "06123454567","9999999999");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.doThrow(new ArrayIndexOutOfBoundsException("Exception")).when(guestRepository).save(Mockito.any(Guest.class));


//        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

        this.mockMvc.perform(post("/api/guests/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        Mockito.verify(guestRepository,times(1)).save(Mockito.any(Guest.class));
    }
}
