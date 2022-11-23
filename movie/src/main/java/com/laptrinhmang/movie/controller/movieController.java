package com.laptrinhmang.movie.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhmang.movie.dto.AESKey;
import com.laptrinhmang.movie.dto.Cineplex;
import com.laptrinhmang.movie.dto.MovieDetail;
import com.laptrinhmang.movie.dto.MovieRespone;
import com.laptrinhmang.movie.dto.ShowTime;
import com.laptrinhmang.movie.service.MovieService;

@RestController
@RequestMapping("/movie")
@CrossOrigin(originPatterns = {"http://localhost:3000","http://192.168.1.5:3000"})
public class movieController {

	@Autowired
	MovieService movieService;

	@GetMapping("/getMovieCineplex/{id}")
	public List<Cineplex> getCineplex(@PathVariable Integer id)
			throws JSONException, IOException {
		return movieService.getShowTime(id);
	}

	@GetMapping("/getMovieShowTime")
	public List<ShowTime> getShowTime(
				@RequestParam(name = "movieId") int movieId,
				@RequestParam(name = "cinemaId") int cinemaId
				)
			throws JSONException, IOException {
		return movieService.getTime(movieId, cinemaId);
	}

	@GetMapping(value = "/getAllMovie")
	public List<MovieRespone> getMovie(HttpServletRequest request) throws IOException{
		return movieService.getAllMovie(request);
	}

	@GetMapping(value = "/getMovieDetail/{id}")
	public MovieDetail getMovie(@PathVariable int id) throws IOException {
		return movieService.getMovieDetail(id);
	}

	@PostMapping("/getPublicKey")
	public String getKey(HttpServletRequest request) throws NoSuchAlgorithmException,InvalidKeySpecException{
		return movieService.generateToken(request);
	}

	@PostMapping("/sendPublicKeyEncode")
	public String sendKey(@RequestParam(name = "AESkey") String aesKey,HttpServletRequest request) throws NoSuchAlgorithmException,InvalidKeySpecException{
		return movieService.sendKeyAES(aesKey,request);
	}

}
