package io.fusion.fusiondbdataservice.service;

import io.fusion.core.MetricsMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
public class PassthroughMetricsMapper implements MetricsMapper {
    @Override
    public Map<String, String> mapSourceToTargetMetrics(String jobId, Map<String, String> sourceMetrics) {
        return sourceMetrics;
    }
}
