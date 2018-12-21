package ru.mikaev.sapr.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Rod {
    @Id
    @GeneratedValue
    private Long id;

    private int l;

    private int a;

    private int e;

    private int sigma;

    private int load;
}
