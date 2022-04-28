package com.security.info.manage.config.repository;

import com.security.info.manage.entity.File;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LawFileEsRepository extends ElasticsearchRepository<File, String> {
 
}