package com.laptrinhmang.movie.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieDetail {

    private int id;

    private String description;
    
    private String urlTomato;

    private List<ReviewUrl> reviewUrls;

    private List<InfoMovie> infoMovies; 

    private String imgUrl;

    private String movieName;

    private String movieNameType;

    private String urlTrailer;
    // List<Cinema>

}
