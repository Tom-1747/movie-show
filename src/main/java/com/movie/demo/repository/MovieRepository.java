package com.movie.demo.repository;

import com.movie.demo.domain.Movie;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {}
