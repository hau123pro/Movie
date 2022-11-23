package com.laptrinhmang.movie.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewUrl {
    private String urlReview;

    private String urlName;

    private String imgUrl;
}
