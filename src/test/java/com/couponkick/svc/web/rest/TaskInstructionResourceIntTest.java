package com.couponkick.svc.web.rest;

import com.couponkick.svc.CouponkicksvcApp;
import com.couponkick.svc.domain.TaskInstruction;
import com.couponkick.svc.repository.TaskInstructionRepository;
import com.couponkick.svc.repository.search.TaskInstructionSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TaskInstructionResource REST controller.
 *
 * @see TaskInstructionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CouponkicksvcApp.class)
@WebAppConfiguration
@IntegrationTest
public class TaskInstructionResourceIntTest {


    private static final Long DEFAULT_TASK_INSTRUCTION_ID = 1L;
    private static final Long UPDATED_TASK_INSTRUCTION_ID = 2L;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TaskInstructionRepository taskInstructionRepository;

    @Inject
    private TaskInstructionSearchRepository taskInstructionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskInstructionMockMvc;

    private TaskInstruction taskInstruction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskInstructionResource taskInstructionResource = new TaskInstructionResource();
        ReflectionTestUtils.setField(taskInstructionResource, "taskInstructionSearchRepository", taskInstructionSearchRepository);
        ReflectionTestUtils.setField(taskInstructionResource, "taskInstructionRepository", taskInstructionRepository);
        this.restTaskInstructionMockMvc = MockMvcBuilders.standaloneSetup(taskInstructionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskInstructionSearchRepository.deleteAll();
        taskInstruction = new TaskInstruction();
        taskInstruction.setTaskInstructionId(DEFAULT_TASK_INSTRUCTION_ID);
        taskInstruction.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTaskInstruction() throws Exception {
        int databaseSizeBeforeCreate = taskInstructionRepository.findAll().size();

        // Create the TaskInstruction

        restTaskInstructionMockMvc.perform(post("/api/task-instructions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskInstruction)))
                .andExpect(status().isCreated());

        // Validate the TaskInstruction in the database
        List<TaskInstruction> taskInstructions = taskInstructionRepository.findAll();
        assertThat(taskInstructions).hasSize(databaseSizeBeforeCreate + 1);
        TaskInstruction testTaskInstruction = taskInstructions.get(taskInstructions.size() - 1);
        assertThat(testTaskInstruction.getTaskInstructionId()).isEqualTo(DEFAULT_TASK_INSTRUCTION_ID);
        assertThat(testTaskInstruction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the TaskInstruction in ElasticSearch
        TaskInstruction taskInstructionEs = taskInstructionSearchRepository.findOne(testTaskInstruction.getId());
        assertThat(taskInstructionEs).isEqualToComparingFieldByField(testTaskInstruction);
    }

    @Test
    @Transactional
    public void getAllTaskInstructions() throws Exception {
        // Initialize the database
        taskInstructionRepository.saveAndFlush(taskInstruction);

        // Get all the taskInstructions
        restTaskInstructionMockMvc.perform(get("/api/task-instructions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskInstruction.getId().intValue())))
                .andExpect(jsonPath("$.[*].taskInstructionId").value(hasItem(DEFAULT_TASK_INSTRUCTION_ID.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTaskInstruction() throws Exception {
        // Initialize the database
        taskInstructionRepository.saveAndFlush(taskInstruction);

        // Get the taskInstruction
        restTaskInstructionMockMvc.perform(get("/api/task-instructions/{id}", taskInstruction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskInstruction.getId().intValue()))
            .andExpect(jsonPath("$.taskInstructionId").value(DEFAULT_TASK_INSTRUCTION_ID.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskInstruction() throws Exception {
        // Get the taskInstruction
        restTaskInstructionMockMvc.perform(get("/api/task-instructions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskInstruction() throws Exception {
        // Initialize the database
        taskInstructionRepository.saveAndFlush(taskInstruction);
        taskInstructionSearchRepository.save(taskInstruction);
        int databaseSizeBeforeUpdate = taskInstructionRepository.findAll().size();

        // Update the taskInstruction
        TaskInstruction updatedTaskInstruction = new TaskInstruction();
        updatedTaskInstruction.setId(taskInstruction.getId());
        updatedTaskInstruction.setTaskInstructionId(UPDATED_TASK_INSTRUCTION_ID);
        updatedTaskInstruction.setDescription(UPDATED_DESCRIPTION);

        restTaskInstructionMockMvc.perform(put("/api/task-instructions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTaskInstruction)))
                .andExpect(status().isOk());

        // Validate the TaskInstruction in the database
        List<TaskInstruction> taskInstructions = taskInstructionRepository.findAll();
        assertThat(taskInstructions).hasSize(databaseSizeBeforeUpdate);
        TaskInstruction testTaskInstruction = taskInstructions.get(taskInstructions.size() - 1);
        assertThat(testTaskInstruction.getTaskInstructionId()).isEqualTo(UPDATED_TASK_INSTRUCTION_ID);
        assertThat(testTaskInstruction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the TaskInstruction in ElasticSearch
        TaskInstruction taskInstructionEs = taskInstructionSearchRepository.findOne(testTaskInstruction.getId());
        assertThat(taskInstructionEs).isEqualToComparingFieldByField(testTaskInstruction);
    }

    @Test
    @Transactional
    public void deleteTaskInstruction() throws Exception {
        // Initialize the database
        taskInstructionRepository.saveAndFlush(taskInstruction);
        taskInstructionSearchRepository.save(taskInstruction);
        int databaseSizeBeforeDelete = taskInstructionRepository.findAll().size();

        // Get the taskInstruction
        restTaskInstructionMockMvc.perform(delete("/api/task-instructions/{id}", taskInstruction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean taskInstructionExistsInEs = taskInstructionSearchRepository.exists(taskInstruction.getId());
        assertThat(taskInstructionExistsInEs).isFalse();

        // Validate the database is empty
        List<TaskInstruction> taskInstructions = taskInstructionRepository.findAll();
        assertThat(taskInstructions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaskInstruction() throws Exception {
        // Initialize the database
        taskInstructionRepository.saveAndFlush(taskInstruction);
        taskInstructionSearchRepository.save(taskInstruction);

        // Search the taskInstruction
        restTaskInstructionMockMvc.perform(get("/api/_search/task-instructions?query=id:" + taskInstruction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskInstruction.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskInstructionId").value(hasItem(DEFAULT_TASK_INSTRUCTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
