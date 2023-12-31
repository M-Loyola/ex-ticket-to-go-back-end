package com.tickettogo.TicketToGoBackend.controller;

import com.tickettogo.TicketToGoBackend.entity.Cinema;
import com.tickettogo.TicketToGoBackend.entity.DetailsMovAndCin;
import com.tickettogo.TicketToGoBackend.entity.Movie;
import com.tickettogo.TicketToGoBackend.repository.CinemaMovieRepository;
import com.tickettogo.TicketToGoBackend.repository.CinemaRepository;
import com.tickettogo.TicketToGoBackend.repository.MoviesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class CinemaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private CinemaMovieRepository cinemaMovieRepository;

    @BeforeEach
    void clearAll() {
        cinemaRepository.deleteAll();
        moviesRepository.deleteAll();
        cinemaMovieRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        cinemaRepository.deleteAll();
        moviesRepository.deleteAll();
        cinemaMovieRepository.deleteAll();
    }

    @Test
    void should_show_all_cinema_when_perform_get_companies_given_company_controller() throws Exception {
        //given
        cinemaRepository.save(new Cinema("Cinema 1", "Manila"));
        cinemaRepository.save(new Cinema("Cinema 2", "Taguig"));

        //when and then
        mockMvc.perform(get("/cinemas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[0].name")).value("Cinema 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[1].name")).value("Cinema 2"));
    }

    @Test
    void should_show_movies_when_perform_get_movies_by_location_given_company_controller() throws Exception {
        //given
        Cinema cinema1 = new Cinema("Cinema 1", "Manila");
        Movie save = moviesRepository.save(new Movie("John wick 1", 120));
        Movie save1 = moviesRepository.save(new Movie("John wick 2", 150));

        cinema1.setMovieList(List.of(save, save1));
        Cinema savedCinema = cinemaRepository.save(cinema1);
        //when and then
        mockMvc.perform(get("/cinemas/{location}/movies", savedCinema.getLocation()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[0].name")).value("Cinema 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[0].movieList[0].title")).value("John wick 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[0].movieList[1].title")).value("John wick 2"));
    }

    @Test
    void should_show_movie_details_when_perform_get_given_cinema_movie_id() throws Exception {
        //given
        Cinema savedCinema = cinemaRepository.save(new Cinema("Cinema 1", "Manila"));
        Movie savedMovie = moviesRepository.save(new Movie("John wick 1", 120));
        DetailsMovAndCin savedCinemaMovie = cinemaMovieRepository.save(new DetailsMovAndCin(null, savedMovie.getId(), savedCinema.getId(), 500, "2023-09-04 10:30:00", "A1,A2"));
        //when
        mockMvc.perform(get("/movies/{cinemaMovieId}/{cinemaId}/reservationDetails", savedCinemaMovie.getCinemaMovieId(),savedCinema.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.occupiedSeats").value("A1,A2"));
        //then
    }
}
