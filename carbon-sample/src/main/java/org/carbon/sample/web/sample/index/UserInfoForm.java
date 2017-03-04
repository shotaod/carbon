package org.carbon.sample.web.sample.index;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * @author Shota Oda 2016/10/12.
 */
@Data
public class UserInfoForm {

    private String name;
    private Integer age;
    private LocalDateTime birthDate;

    private List<JobForm> jobs;
}
