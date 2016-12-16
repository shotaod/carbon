package org.carbon.sample.web.consumer.dto;

import lombok.Value;
import org.carbon.sample.tables.pojos.Asset;
import org.carbon.sample.tables.pojos.Lecturer;

/**
 * @author ubuntu 2016/12/10.
 */
@Value
public class LecturerIndex {
    private Lecturer lecturer;
    private Asset asset;
}
