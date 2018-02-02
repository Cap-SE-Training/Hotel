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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

//import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
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

    //also uses address, is tested in separate controller
    @Test
    public void testGetAllGuests() throws Exception {

        List<Guest> guests = new ArrayList<>();

        Address address1 = new Address("Haarlemmerstraat", "10", "1234ab", "Amsterdam", "Nederland");
        Guest guest1 = new Guest("Kees", "Pieterson", address1, "keespieterson@hotmail.com", "061234567");
        Guest guest2 = new Guest("Piet", "Keesson", address1, "pietkeesson@hotmail.com", "061234568");

        guests.add(guest1);
        guests.add(guest2);

        when(guestRepository.findAll()).thenReturn(guests);

        this.mockMvc.perform(get("/api/guests/"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is((int)guests.get(0).getId())))
                .andExpect(jsonPath("$.[0].firstName", is(guests.get(0).getFirstName())))
                .andExpect(jsonPath("$.[0].lastName", is(guests.get(0).getLastName())))
                .andExpect(jsonPath("$.[0].email", is(guests.get(0).getEmail())))
                .andExpect(jsonPath("$.[0].telephoneNumber", is(guests.get(0).getTelephoneNumber())))
                .andExpect(jsonPath("$.[1].id", is((int)guests.get(1).getId())))
                .andExpect(jsonPath("$.[1].firstName", is(guests.get(1).getFirstName())))
                .andExpect(jsonPath("$.[1].lastName", is(guests.get(1).getLastName())))
                .andExpect(jsonPath("$.[1].email", is(guests.get(1).getEmail())))
                .andExpect(jsonPath("$.[1].telephoneNumber", is(guests.get(1).getTelephoneNumber())))
                .andExpect(status().isOk());
    }

    //also uses address, is tested in separate controller
    @Test
    public void testCreateGuest() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234ab", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "061234567");
        //address.setGuest(guest);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

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
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateGuest() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234ab", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "061234567");
        //address.setGuest(guest);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(guest);

        Mockito.when(guestRepository.save(Mockito.any(Guest.class))).thenReturn(guest);

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
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteGuest() throws Exception {
        Address address = new Address("Haarlemmerstraat", "10", "1234ab", "Amsterdam", "Nederland");
        Guest guest = new Guest("Kees", "Pieterson", address, "keespieterson@hotmail.com", "061234567");

        doNothing().when(guestRepository).delete(guest.getId());

        this.mockMvc.perform(delete("/api/guests/delete/{id}", guest.getId()))
                .andExpect(status().isOk());
    }
}
