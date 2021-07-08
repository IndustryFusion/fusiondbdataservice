package io.fusion.fusiondbdataservice.config;

import io.fusion.core.FusionDataServiceConfig;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
    private final FusionDataServiceConfig fusionDataServiceConfig;

    public DatasourceConfig(FusionDataServiceConfig fusionDataServiceConfig) {
        this.fusionDataServiceConfig = fusionDataServiceConfig;
    }

    @Bean
    public DataSource getDataSource() {
        var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(fusionDataServiceConfig.getConnectionString());
        return dataSourceBuilder.build();
    }
}
