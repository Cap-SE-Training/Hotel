package com.capgemini.setrack.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy="roomType", cascade=CascadeType.ALL)
    private Set<Room> rooms;

    private String type;

    public RoomType(){}

    public RoomType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
