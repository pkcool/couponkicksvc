package com.couponkick.svc.web.rest;

import com.couponkick.svc.CouponkicksvcApp;
import com.couponkick.svc.domain.Merchant;
import com.couponkick.svc.repository.MerchantRepository;
import com.couponkick.svc.repository.search.MerchantSearchRepository;
import com.couponkick.svc.web.rest.dto.MerchantDTO;
import com.couponkick.svc.web.rest.mapper.MerchantMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MerchantResource REST controller.
 *
 * @see MerchantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CouponkicksvcApp.class)
@WebAppConfiguration
@IntegrationTest
public class MerchantResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_MERCHANT_ID = 1L;
    private static final Long UPDATED_MERCHANT_ID = 2L;
    private static final String DEFAULT_TRADING_NAME = "AAAAA";
    private static final String UPDATED_TRADING_NAME = "BBBBB";
    private static final String DEFAULT_ABN = "AAAAA";
    private static final String UPDATED_ABN = "BBBBB";
    private static final String DEFAULT_CONTACT = "AAAAA";
    private static final String UPDATED_CONTACT = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFIED_DATE);

    private static final ZonedDateTime DEFAULT_LAST_LOGON_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_LOGON_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_LOGON_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_LOGON_DATE);

    @Inject
    private MerchantRepository merchantRepository;

    @Inject
    private MerchantMapper merchantMapper;

    @Inject
    private MerchantSearchRepository merchantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MerchantResource merchantResource = new MerchantResource();
        ReflectionTestUtils.setField(merchantResource, "merchantSearchRepository", merchantSearchRepository);
        ReflectionTestUtils.setField(merchantResource, "merchantRepository", merchantRepository);
        ReflectionTestUtils.setField(merchantResource, "merchantMapper", merchantMapper);
        this.restMerchantMockMvc = MockMvcBuilders.standaloneSetup(merchantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        merchantSearchRepository.deleteAll();
        merchant = new Merchant();
        merchant.setMerchantId(DEFAULT_MERCHANT_ID);
        merchant.setTradingName(DEFAULT_TRADING_NAME);
        merchant.setAbn(DEFAULT_ABN);
        merchant.setContact(DEFAULT_CONTACT);
        merchant.setCreatedDate(DEFAULT_CREATED_DATE);
        merchant.setModifiedDate(DEFAULT_MODIFIED_DATE);
        merchant.setLastLogonDate(DEFAULT_LAST_LOGON_DATE);
    }

    @Test
    @Transactional
    public void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.merchantToMerchantDTO(merchant);

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
                .andExpect(status().isCreated());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getMerchantId()).isEqualTo(DEFAULT_MERCHANT_ID);
        assertThat(testMerchant.getTradingName()).isEqualTo(DEFAULT_TRADING_NAME);
        assertThat(testMerchant.getAbn()).isEqualTo(DEFAULT_ABN);
        assertThat(testMerchant.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testMerchant.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMerchant.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testMerchant.getLastLogonDate()).isEqualTo(DEFAULT_LAST_LOGON_DATE);

        // Validate the Merchant in ElasticSearch
        Merchant merchantEs = merchantSearchRepository.findOne(testMerchant.getId());
        assertThat(merchantEs).isEqualToComparingFieldByField(testMerchant);
    }

    @Test
    @Transactional
    public void getAllMerchants() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchants
        restMerchantMockMvc.perform(get("/api/merchants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
                .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID.intValue())))
                .andExpect(jsonPath("$.[*].tradingName").value(hasItem(DEFAULT_TRADING_NAME.toString())))
                .andExpect(jsonPath("$.[*].abn").value(hasItem(DEFAULT_ABN.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastLogonDate").value(hasItem(DEFAULT_LAST_LOGON_DATE_STR)));
    }

    @Test
    @Transactional
    public void getMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.merchantId").value(DEFAULT_MERCHANT_ID.intValue()))
            .andExpect(jsonPath("$.tradingName").value(DEFAULT_TRADING_NAME.toString()))
            .andExpect(jsonPath("$.abn").value(DEFAULT_ABN.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE_STR))
            .andExpect(jsonPath("$.lastLogonDate").value(DEFAULT_LAST_LOGON_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
        merchantSearchRepository.save(merchant);
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        Merchant updatedMerchant = new Merchant();
        updatedMerchant.setId(merchant.getId());
        updatedMerchant.setMerchantId(UPDATED_MERCHANT_ID);
        updatedMerchant.setTradingName(UPDATED_TRADING_NAME);
        updatedMerchant.setAbn(UPDATED_ABN);
        updatedMerchant.setContact(UPDATED_CONTACT);
        updatedMerchant.setCreatedDate(UPDATED_CREATED_DATE);
        updatedMerchant.setModifiedDate(UPDATED_MODIFIED_DATE);
        updatedMerchant.setLastLogonDate(UPDATED_LAST_LOGON_DATE);
        MerchantDTO merchantDTO = merchantMapper.merchantToMerchantDTO(updatedMerchant);

        restMerchantMockMvc.perform(put("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
                .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getMerchantId()).isEqualTo(UPDATED_MERCHANT_ID);
        assertThat(testMerchant.getTradingName()).isEqualTo(UPDATED_TRADING_NAME);
        assertThat(testMerchant.getAbn()).isEqualTo(UPDATED_ABN);
        assertThat(testMerchant.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testMerchant.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMerchant.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testMerchant.getLastLogonDate()).isEqualTo(UPDATED_LAST_LOGON_DATE);

        // Validate the Merchant in ElasticSearch
        Merchant merchantEs = merchantSearchRepository.findOne(testMerchant.getId());
        assertThat(merchantEs).isEqualToComparingFieldByField(testMerchant);
    }

    @Test
    @Transactional
    public void deleteMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
        merchantSearchRepository.save(merchant);
        int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Get the merchant
        restMerchantMockMvc.perform(delete("/api/merchants/{id}", merchant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean merchantExistsInEs = merchantSearchRepository.exists(merchant.getId());
        assertThat(merchantExistsInEs).isFalse();

        // Validate the database is empty
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
        merchantSearchRepository.save(merchant);

        // Search the merchant
        restMerchantMockMvc.perform(get("/api/_search/merchants?query=id:" + merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].tradingName").value(hasItem(DEFAULT_TRADING_NAME.toString())))
            .andExpect(jsonPath("$.[*].abn").value(hasItem(DEFAULT_ABN.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)))
            .andExpect(jsonPath("$.[*].lastLogonDate").value(hasItem(DEFAULT_LAST_LOGON_DATE_STR)));
    }
}
