package com.capgemini.setrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy="roomType", cascade=CascadeType.ALL)
    private Set<Room> rooms;

    @NotNull(message="A type is required!")
    @Size(min=1, message="A type is required!")
    @Column(unique=true)
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
