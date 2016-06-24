package com.couponkick.svc.repository;

import com.couponkick.svc.domain.TaskInstruction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskInstruction entity.
 */
@SuppressWarnings("unused")
public interface TaskInstructionRepository extends JpaRepository<TaskInstruction,Long> {

}
