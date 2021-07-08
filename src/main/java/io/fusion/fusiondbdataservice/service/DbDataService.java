/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.fusion.fusiondbdataservice.service;

import io.fusion.core.FusionDataServiceConfig;
import io.fusion.core.MetricsPullService;
import io.fusion.fusiondbdataservice.exception.JobNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Primary
public class DbDataService implements MetricsPullService {
    private final FusionDataServiceConfig fusionDataServiceConfig;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbDataService(FusionDataServiceConfig fusionDataServiceConfig, JdbcTemplate jdbcTemplate) {
        this.fusionDataServiceConfig = fusionDataServiceConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> getMetrics(String jobId) {
        log.info("Fetching metrics for job {}", jobId);
        var jobSpec = fusionDataServiceConfig.getJobSpecs().get(jobId);
        if (jobSpec == null) {
            throw new JobNotFoundException();
        }

        var data = jobSpec.getFields().stream().collect(Collectors.toMap(
                FusionDataServiceConfig.FieldSpec::getTarget,
                sourceSql -> {
                    String value;
                    try {
                        value = jdbcTemplate.queryForObject(sourceSql.getSource(), String.class);
                    } catch (EmptyResultDataAccessException e) {
                        value = "";
                    }
                    if (value == null) {
                        value = "";
                    }
                    return value;
                }));

        log.info("Fetched {} metrics for job {}", data.size(), jobId);
        return data;
    }
}
