package com.viathings.smartstatement.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.viathings.smartstatement.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".conducteurAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".conducteurBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".vehiculeAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".vehiculeBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".assuranceAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".assuranceBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".assureeAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Constat.class.getName() + ".assureeBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Vehicule.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Vehicule.class.getName() + ".noms", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Vehicule.class.getName() + ".constatPartAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Vehicule.class.getName() + ".constatPartBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Conducteur.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Conducteur.class.getName() + ".constatAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Conducteur.class.getName() + ".constatBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Conducteur.class.getName() + ".vehicules", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Assurance.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Assurance.class.getName() + ".constatPartAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Assurance.class.getName() + ".constatPartBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Assuree.class.getName(), jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Assuree.class.getName() + ".constatPartAS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Assuree.class.getName() + ".constatPartBS", jcacheConfiguration);
            cm.createCache(com.viathings.smartstatement.domain.Circonstances.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
