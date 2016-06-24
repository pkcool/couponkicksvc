package com.couponkick.svc.repository.search;

import com.couponkick.svc.domain.Merchant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Merchant entity.
 */
public interface MerchantSearchRepository extends ElasticsearchRepository<Merchant, Long> {
}
