package ru.mikaev.sapr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Construction {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Заделка слева
     */
    private boolean supportLeft;

    /**
     * Заделка справа
     */
    private boolean supportRight;

    /**
     * Стержни
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "construction_id")
    private Set<Rod> rods;
}
