package com.tistory.jaimemin.springdatajpaproject.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "A")
public class Album extends Item {

    private String artist;

    private String etc;
}
