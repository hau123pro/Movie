package com.laptrinhmang.movie.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InfoMovie {
    private String fieldName;

    private List<String> value;
}
