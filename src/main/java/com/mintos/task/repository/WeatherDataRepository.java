package com.mintos.task.repository;

import com.mintos.task.entity.WeatherDataDO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeatherDataRepository extends CrudRepository<WeatherDataDO, Long> {

    List<WeatherDataDO> findAll();
}
