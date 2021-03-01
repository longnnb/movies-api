package com.restapi.movies.repos;

import com.restapi.movies.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Page<Movie> findAll(Pageable pageable);
    Page<Movie> findByTitleContaining(String title, Pageable pageable);
    Page<Movie> findByTitleContainingOrOverviewContaining(String title, String overview, Pageable pageable);
    List<Movie> findByIsFavoriteTrue();
}
