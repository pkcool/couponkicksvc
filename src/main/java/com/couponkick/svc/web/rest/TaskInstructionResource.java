package com.couponkick.svc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.couponkick.svc.domain.TaskInstruction;
import com.couponkick.svc.repository.TaskInstructionRepository;
import com.couponkick.svc.repository.search.TaskInstructionSearchRepository;
import com.couponkick.svc.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TaskInstruction.
 */
@RestController
@RequestMapping("/api")
public class TaskInstructionResource {

    private final Logger log = LoggerFactory.getLogger(TaskInstructionResource.class);
        
    @Inject
    private TaskInstructionRepository taskInstructionRepository;
    
    @Inject
    private TaskInstructionSearchRepository taskInstructionSearchRepository;
    
    /**
     * POST  /task-instructions : Create a new taskInstruction.
     *
     * @param taskInstruction the taskInstruction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskInstruction, or with status 400 (Bad Request) if the taskInstruction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/task-instructions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskInstruction> createTaskInstruction(@RequestBody TaskInstruction taskInstruction) throws URISyntaxException {
        log.debug("REST request to save TaskInstruction : {}", taskInstruction);
        if (taskInstruction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taskInstruction", "idexists", "A new taskInstruction cannot already have an ID")).body(null);
        }
        TaskInstruction result = taskInstructionRepository.save(taskInstruction);
        taskInstructionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/task-instructions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskInstruction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-instructions : Updates an existing taskInstruction.
     *
     * @param taskInstruction the taskInstruction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskInstruction,
     * or with status 400 (Bad Request) if the taskInstruction is not valid,
     * or with status 500 (Internal Server Error) if the taskInstruction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/task-instructions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskInstruction> updateTaskInstruction(@RequestBody TaskInstruction taskInstruction) throws URISyntaxException {
        log.debug("REST request to update TaskInstruction : {}", taskInstruction);
        if (taskInstruction.getId() == null) {
            return createTaskInstruction(taskInstruction);
        }
        TaskInstruction result = taskInstructionRepository.save(taskInstruction);
        taskInstructionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskInstruction", taskInstruction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-instructions : get all the taskInstructions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taskInstructions in body
     */
    @RequestMapping(value = "/task-instructions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskInstruction> getAllTaskInstructions() {
        log.debug("REST request to get all TaskInstructions");
        List<TaskInstruction> taskInstructions = taskInstructionRepository.findAll();
        return taskInstructions;
    }

    /**
     * GET  /task-instructions/:id : get the "id" taskInstruction.
     *
     * @param id the id of the taskInstruction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskInstruction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/task-instructions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskInstruction> getTaskInstruction(@PathVariable Long id) {
        log.debug("REST request to get TaskInstruction : {}", id);
        TaskInstruction taskInstruction = taskInstructionRepository.findOne(id);
        return Optional.ofNullable(taskInstruction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /task-instructions/:id : delete the "id" taskInstruction.
     *
     * @param id the id of the taskInstruction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/task-instructions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskInstruction(@PathVariable Long id) {
        log.debug("REST request to delete TaskInstruction : {}", id);
        taskInstructionRepository.delete(id);
        taskInstructionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskInstruction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/task-instructions?query=:query : search for the taskInstruction corresponding
     * to the query.
     *
     * @param query the query of the taskInstruction search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/task-instructions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskInstruction> searchTaskInstructions(@RequestParam String query) {
        log.debug("REST request to search TaskInstructions for query {}", query);
        return StreamSupport
            .stream(taskInstructionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
