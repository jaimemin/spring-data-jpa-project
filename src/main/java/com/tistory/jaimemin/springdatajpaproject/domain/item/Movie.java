package com.tistory.jaimemin.springdatajpaproject.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "M")
public class Movie extends Item {

    private String director;

    private String actor;
}
