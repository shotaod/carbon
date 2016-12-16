package org.dabuntu.sample.web.consumer.dto;

import lombok.Value;
import org.dabunt.sample.tables.pojos.Asset;
import org.dabunt.sample.tables.pojos.Lecturer;

/**
 * @author ubuntu 2016/12/10.
 */
@Value
public class LecturerIndex {
    private Lecturer lecturer;
    private Asset asset;
}
