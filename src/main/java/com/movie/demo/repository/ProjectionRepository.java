package com.movie.demo.repository;

import com.movie.demo.domain.Projection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, UUID> {}
