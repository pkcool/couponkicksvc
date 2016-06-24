package com.couponkick.svc.repository.search;

import com.couponkick.svc.domain.Address;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Address entity.
 */
public interface AddressSearchRepository extends ElasticsearchRepository<Address, Long> {
}
