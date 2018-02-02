import com.capgemini.setrack.controller.RoomController;
import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.model.RoomType;
import com.capgemini.setrack.repository.RoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.rmi.server.ExportException;
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
public class RoomControllerTests {

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomRepository roomRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    public void getAllRooms() throws Exception {
        List<Room> rooms = new ArrayList<Room>();

        RoomType roomType1 = new RoomType("basic");
        Room room1 = new Room("test", "13", roomType1, 6, 250);

        RoomType roomType2 = new RoomType("luxe");
        Room room2 = new Room("test2", "787A", roomType2, 4, 20);

        rooms.add(room1);
        rooms.add(room2);

        when(roomRepository.findAll()).thenReturn(rooms);

        this.mockMvc.perform(get("/api/rooms/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateRoomWithInvalidRoomSizeExpectError() throws Exception {
        RoomType roomType = new RoomType("luxe");
        Room room = new Room("Test Room", "1a", roomType, -1, 10);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(room);

        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenReturn(room);

        this.mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(roomRepository, times(0)).save(Mockito.any(Room.class));

    }

    @Test
    public void testEditRoomWithInvalidRoomSizeExpectError() throws Exception {
        RoomType roomType = new RoomType("luxe");
        Room room = new Room("test", "250A", roomType, -1, 250);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(room);

        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenReturn(room);

        this.mockMvc.perform(post("/api/rooms/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(roomRepository, times(0)).save(Mockito.any(Room.class));
    }

    @Test
    public void testCreateRoomWithInvalidRoomTypeExpectException() throws Exception {
        RoomType nonExistingRoomType = new RoomType("luxe");
        Room room = new Room("Test Room", "1a", nonExistingRoomType, 6, 10);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(room);

        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenThrow(new DataIntegrityViolationException("Oh no!"));

        this.mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(roomRepository, times(1)).save(Mockito.any(Room.class));
    }

    @Test
    public void testDeleteRoomWithErrorHandling() throws Exception {

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(roomRepository).delete(Mockito.anyLong());

        this.mockMvc.perform(delete("/api/rooms/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Mockito.verify(roomRepository, times(0)).delete((long) 2);
    }

    @Test
    public void testDeleteRoomIsOk() throws Exception {

        Mockito.doNothing().when(roomRepository).delete(Mockito.anyLong());

        this.mockMvc.perform(delete("/api/rooms/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(roomRepository, times(1)).delete((long) 1);
    }

    @Test
    public void testCreateRoom() throws Exception {
        RoomType roomType = new RoomType("luxe");
        Room room = new Room("Test Room", "1a", roomType, 1, 10);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(room);

        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenReturn(room);

        this.mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(jsonPath("$.id", is((int) room.getId())))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.number", is(room.getNumber())))
                .andExpect(jsonPath("$.roomType.id", is((int) room.getRoomType().getId())))
                .andExpect(jsonPath("$.roomType.type", is(room.getRoomType().getType())))
                .andExpect(jsonPath("$.size", is((int) room.getSize())))
                .andExpect(jsonPath("$.price", is((double) room.getPrice())))
                .andExpect(status().isOk());

        Mockito.verify(roomRepository, times(1)).save(Mockito.any(Room.class));
    }
}