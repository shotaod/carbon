package org.carbon.sample.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Shota Oda 2016/11/25.
 */
@Getter
@Setter
@Entity
public class LecturerSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "begin_datetime")
    private LocalDateTime beginDatetime;
    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;
}
