package org.carbon.sample.domain.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Shota Oda 2016/11/05.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(name = "address")
    private String address;

    @Column(name = "password")
    private String password;

    // -----------------------------------------------------
    //                                               Foreign Table
    //                                               -------
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "lecturer")
    private List<LectureApplyHistory> lectureApplyHistory;
}
