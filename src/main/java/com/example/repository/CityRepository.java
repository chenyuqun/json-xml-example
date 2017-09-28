package com.example.repository;

import com.example.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by alexchen on 2017/9/25.
 */
@Repository
public interface CityRepository extends JpaRepository<City,Long> {
}
