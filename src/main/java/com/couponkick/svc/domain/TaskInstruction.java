package com.couponkick.svc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TaskInstruction.
 */
@Entity
@Table(name = "task_instruction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "taskinstruction")
public class TaskInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "task_instruction_id")
    private Long taskInstructionId;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskInstructionId() {
        return taskInstructionId;
    }

    public void setTaskInstructionId(Long taskInstructionId) {
        this.taskInstructionId = taskInstructionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskInstruction taskInstruction = (TaskInstruction) o;
        if(taskInstruction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, taskInstruction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskInstruction{" +
            "id=" + id +
            ", taskInstructionId='" + taskInstructionId + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
