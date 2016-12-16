package org.carbon.sample.web.consumer.dto;

import lombok.Value;
import org.carbon.sample.tables.pojos.Asset;
import org.carbon.sample.tables.pojos.Lecturer;

/**
 * @author Shota Oda 2016/12/10.
 */
@Value
public class LecturerIndex {
    private Lecturer lecturer;
    private Asset asset;
}
