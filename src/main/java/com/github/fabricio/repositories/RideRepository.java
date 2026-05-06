package com.github.fabricio.repositories;

import com.github.fabricio.dto.RideRecord;
import com.github.fabricio.entities.Ride;
import dev.langchain4j.agent.tool.Tool;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class RideRepository implements PanacheRepository<Ride> {

    @Tool("get best ride")
    @Transactional
    public RideRecord getTheBestRideByRatings() {
      log.info("get the best ride query");

      return findAll(Sort.descending("rating"))
              .project(RideRecord.class)
              .firstResult();
    }
}
