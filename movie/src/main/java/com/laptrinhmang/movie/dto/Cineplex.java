package com.laptrinhmang.movie.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Cineplex {
    private int id;
    private String cineplexName;
    private List<Cinema> cinemas;

}
