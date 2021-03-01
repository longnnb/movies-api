package com.restapi.movies.controllers;

import com.restapi.movies.entities.Movie;
import com.restapi.movies.repos.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping(path = "/movies")
    public ResponseEntity<Page<Movie>> getMovies(@RequestParam(name="search") Optional<String> search, @RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviesPage = search.isPresent() ? movieRepository.findByTitleContaining(search.get(), pageable) : movieRepository.findAll(pageable);
        List<Movie> movies = moviesPage.getContent();
        return new ResponseEntity(movies, HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieDetail(@PathVariable int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (!movie.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return ResponseEntity.ok(movie.get());
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Movie>> getFavorites() {
        List<Movie> movies = movieRepository.findByIsFavoriteTrue();

        return new ResponseEntity(movies, HttpStatus.OK);
    }

    @PostMapping("/favorite/{id}")
    public ResponseEntity addFavorite(@PathVariable int id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (!movieOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Movie movie = movieOptional.get();
        movie.setFavorite(true);
        movieRepository.save(movie);

        return ResponseEntity.ok("Successfully");
    }
}
