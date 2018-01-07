package org.carbon.sample.v2.web.task;

import lombok.Getter;
import org.carbon.sample.v2.ext.jooq.tables.records.TaskRecord;

/**
 * @author ubuntu 2017/03/28.
 */
@Getter
public class TaskDto {
    private long id;
    private String title;
    private String description;
    private boolean finished;
    private int point;

    public TaskDto(TaskRecord record) {
        this.id = record.getId();
        this.title = record.getTitle();
        this.description = record.getDescription();
        this.finished = record.getFinished();

    }
}
