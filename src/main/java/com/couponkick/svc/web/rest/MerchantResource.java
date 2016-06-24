package com.couponkick.svc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.couponkick.svc.domain.Merchant;
import com.couponkick.svc.repository.MerchantRepository;
import com.couponkick.svc.repository.search.MerchantSearchRepository;
import com.couponkick.svc.web.rest.util.HeaderUtil;
import com.couponkick.svc.web.rest.util.PaginationUtil;
import com.couponkick.svc.web.rest.dto.MerchantDTO;
import com.couponkick.svc.web.rest.mapper.MerchantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Merchant.
 */
@RestController
@RequestMapping("/api")
public class MerchantResource {

    private final Logger log = LoggerFactory.getLogger(MerchantResource.class);
        
    @Inject
    private MerchantRepository merchantRepository;
    
    @Inject
    private MerchantMapper merchantMapper;
    
    @Inject
    private MerchantSearchRepository merchantSearchRepository;
    
    /**
     * POST  /merchants : Create a new merchant.
     *
     * @param merchantDTO the merchantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new merchantDTO, or with status 400 (Bad Request) if the merchant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MerchantDTO> createMerchant(@RequestBody MerchantDTO merchantDTO) throws URISyntaxException {
        log.debug("REST request to save Merchant : {}", merchantDTO);
        if (merchantDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("merchant", "idexists", "A new merchant cannot already have an ID")).body(null);
        }
        Merchant merchant = merchantMapper.merchantDTOToMerchant(merchantDTO);
        merchant = merchantRepository.save(merchant);
        MerchantDTO result = merchantMapper.merchantToMerchantDTO(merchant);
        merchantSearchRepository.save(merchant);
        return ResponseEntity.created(new URI("/api/merchants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("merchant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /merchants : Updates an existing merchant.
     *
     * @param merchantDTO the merchantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated merchantDTO,
     * or with status 400 (Bad Request) if the merchantDTO is not valid,
     * or with status 500 (Internal Server Error) if the merchantDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MerchantDTO> updateMerchant(@RequestBody MerchantDTO merchantDTO) throws URISyntaxException {
        log.debug("REST request to update Merchant : {}", merchantDTO);
        if (merchantDTO.getId() == null) {
            return createMerchant(merchantDTO);
        }
        Merchant merchant = merchantMapper.merchantDTOToMerchant(merchantDTO);
        merchant = merchantRepository.save(merchant);
        MerchantDTO result = merchantMapper.merchantToMerchantDTO(merchant);
        merchantSearchRepository.save(merchant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("merchant", merchantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /merchants : get all the merchants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of merchants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MerchantDTO>> getAllMerchants(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Merchants");
        Page<Merchant> page = merchantRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/merchants");
        return new ResponseEntity<>(merchantMapper.merchantsToMerchantDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /merchants/:id : get the "id" merchant.
     *
     * @param id the id of the merchantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the merchantDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/merchants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MerchantDTO> getMerchant(@PathVariable Long id) {
        log.debug("REST request to get Merchant : {}", id);
        Merchant merchant = merchantRepository.findOne(id);
        MerchantDTO merchantDTO = merchantMapper.merchantToMerchantDTO(merchant);
        return Optional.ofNullable(merchantDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /merchants/:id : delete the "id" merchant.
     *
     * @param id the id of the merchantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/merchants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        log.debug("REST request to delete Merchant : {}", id);
        merchantRepository.delete(id);
        merchantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("merchant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/merchants?query=:query : search for the merchant corresponding
     * to the query.
     *
     * @param query the query of the merchant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/merchants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MerchantDTO>> searchMerchants(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Merchants for query {}", query);
        Page<Merchant> page = merchantSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/merchants");
        return new ResponseEntity<>(merchantMapper.merchantsToMerchantDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
