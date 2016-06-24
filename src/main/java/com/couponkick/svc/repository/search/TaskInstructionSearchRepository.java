package com.couponkick.svc.repository.search;

import com.couponkick.svc.domain.TaskInstruction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TaskInstruction entity.
 */
public interface TaskInstructionSearchRepository extends ElasticsearchRepository<TaskInstruction, Long> {
}
