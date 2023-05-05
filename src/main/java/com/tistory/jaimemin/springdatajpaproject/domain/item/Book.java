package com.tistory.jaimemin.springdatajpaproject.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "B")
public class Book extends Item {

    private String author;

    private String isbn;
}
