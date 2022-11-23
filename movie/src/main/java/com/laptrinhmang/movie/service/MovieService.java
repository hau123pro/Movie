package com.laptrinhmang.movie.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.google.gson.Gson;
import com.laptrinhmang.movie.dto.AESKey;
import com.laptrinhmang.movie.dto.Cinema;
import com.laptrinhmang.movie.dto.Cineplex;
import com.laptrinhmang.movie.dto.InfoMovie;
import com.laptrinhmang.movie.dto.MovieDetail;
import com.laptrinhmang.movie.dto.MovieRespone;
import com.laptrinhmang.movie.dto.ReviewUrl;
import com.laptrinhmang.movie.dto.ShowTime;
import com.laptrinhmang.movie.utils.JwtTokenUtil;
import com.laptrinhmang.movie.utils.RSAKeyPairGenerator;
import com.laptrinhmang.movie.utils.RSAUtil;
import com.laptrinhmang.movie.utils.Security;

@Service
public class MovieService {

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	

	public String sendKeyAES(String aesKey,HttpServletRequest request)throws NoSuchAlgorithmException,InvalidKeySpecException{
		
		System.out.println(String.valueOf(request.getSession().getAttribute("privateKey")));
		PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(Base64.getDecoder().decode(String.valueOf(request.getSession().getAttribute("privateKey"))));
	  	KeyFactory kf = KeyFactory.getInstance("RSA");		
	  	PrivateKey privateKey=kf.generatePrivate(spec);
		String q=aesKey.replace("\n", "");
		String key=RSAUtil.Decrypt(String.valueOf(request.getSession().getAttribute("aesKeyEncrypt")), privateKey);

		request.getSession().setAttribute("aesKey",key);
		
		return key;
	}


	public String generateToken(HttpServletRequest request) throws NoSuchAlgorithmException,InvalidKeySpecException{
		RSAKeyPairGenerator generator=new RSAKeyPairGenerator();
		String publickey=String.valueOf(generator.getPublicKey().getEncoded());
		
		byte[] privatekey=generator.getPrivateKey().getEncoded();
		byte[] publicKey=generator.getPublicKey().getEncoded();
		String a=Base64.getEncoder().encodeToString(privatekey);
		String b=Base64.getEncoder().encodeToString(publicKey);
		request.getSession().setAttribute("publicKey",b.replace("\n", ""));
		request.getSession().setAttribute("privateKey", a);
		
		if(a.equals(String.valueOf(a)))
		// System.out.println(String.valueOf(a));
		// if(request.getSession().getAttribute("publicKey").equals(generator.getPublicKey()))
		// System.out.println(generator.getPublicKey());
		System.out.println(Base64.getDecoder().decode(RSAUtil.Encrypt("hau", generator.getPublicKey())));
		// System.out.println;
		System.out.println(RSAUtil.Encrypt("hau", generator.getPublicKey()));
		request.getSession().setAttribute("aesKeyEncrypt", RSAUtil.Encrypt("hau", generator.getPublicKey()));
		return jwtTokenUtil.generateToken(b.replace("\n", ""));
		
	}
	
	public List<ShowTime> getTime(int movieId,int cinemaId) throws JSONException, IOException {
		Document doc = Jsoup.connect("https://moveek.com/showtime/movie/"+movieId+"?date=2022-11-15&cinema="+cinemaId).get();
		List<ShowTime> showTimes=new ArrayList<>();
		Elements elements=doc.select("div.mb-1");
		for(Element element:elements){
			Element elementName=element.select("label").first();
			Elements elementTime=element.select("span.time");
			List<String> showTime=new ArrayList<>();
			for(Element time:elementTime){
				showTime.add(time.text());
			}
			ShowTime time=ShowTime.builder()
								.nameShowtime(elementName.text())
								.time(showTime)
								.build();
			showTimes.add(time);
		}
		// System.out.println(doc);
		return showTimes;

	}
	public List<MovieRespone> getAllMovie(HttpServletRequest request ) throws IOException{
		System.setProperty("webdriver.chrome.driver",
                    "E:\\INTERN\\chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			try{
				driver.manage().timeouts().pageLoadTimeout(5,  TimeUnit.SECONDS);
				driver.get("https://moveek.com/dang-chieu/");
			}catch(TimeoutException e){
				// driver.close();
			}
			Document doc = Jsoup.parse(driver.getPageSource());

		List<MovieRespone> list = new ArrayList<MovieRespone>();
		Elements elements=doc.select("div.card.card-xs.mb-4");
		for (Element a : elements) {
			Element element2 = a.select("div.card-body.border-top h3 a").first();
			Element element3 = a.select("a img").first();
			Element element4 = a.select("div.btn-group.btn-actions a").first();
			MovieRespone movie = new MovieRespone(element2.attr("title"), element3.attr("src"),
					Integer.valueOf(element4.attr("data-id")));
			list.add(movie);
		}
		String json = new Gson().toJson(list);
		JSONArray array=new JSONArray(json);
		Security security=new Security(String.valueOf(request.getSession().getAttribute("aesKey")));
		System.out.println();
		return list;
	}

	public MovieDetail getMovieDetail(int id) throws IOException{
		System.setProperty("webdriver.chrome.driver",
                    "E:\\INTERN\\chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			try{
				driver.manage().timeouts().pageLoadTimeout(5,  TimeUnit.SECONDS);
				driver.get("https://moveek.com/movie/"+id);
			}catch(TimeoutException e){
				// driver.close();
			}
			Document doc = Jsoup.parse(driver.getPageSource());

		Element elementDescription=doc.select("div.row p.mb-3.text-justify").first();
		
		Element elementType=doc.select("p.mb-0.text-muted.text-truncate").first();
		String[] movieTypeName=elementType.text().split("-");
		String url=movieTypeName[0].replace(":", "").replace(" ", "_");
		String urlTomato=url.substring(0, url.length()-1);

		Elements elementReview=doc.select("div.card.card-sm.card-article.mb-3 div.article");
		Elements elementImg=doc.select("div.d-none.d-sm-block.col-2 img");
		Elements elementMovieName=doc.select("h1.mb-0.text-truncate a");
		List<ReviewUrl> reviewUrls=new ArrayList<>();
		for(Element element:elementReview){
			Element urlReview=element.select("div.col-sm-4.col-12 a").first();
			Element imgReview=urlReview.select("img").first();
			Element nameReview=element.select("h4.card-title.mb-1.mt-2.mt-sm-0 a").first();
			ReviewUrl reviewUrl=ReviewUrl.builder()
									.imgUrl(imgReview.attr("src"))
									.urlName(nameReview.text())
									.urlReview(urlReview.attr("href"))
									.build();
			reviewUrls.add(reviewUrl);
		}

		Elements elementInfo=doc.select("div.row div.col-12.col-lg-5 p.mb-2");
		List<InfoMovie>  infoMovies=new ArrayList<>();
		for(Element element:elementInfo){
			List<String> values=new ArrayList<>();
			Element fieldName=element.select("strong").first();
			Elements elementInfos = element.select("span a.text-danger");
			for(Element elementinfo:elementInfos){
				values.add(elementinfo.text());
			}
			InfoMovie infoMovie=InfoMovie.builder()
									.fieldName(fieldName.text())
									.value(values).build();
			infoMovies.add(infoMovie);
		}

		Element elementTrailer=doc.select("div.js-video.youtube.widescreen.mb-4 iframe").first();

		MovieDetail detail=MovieDetail.builder()
									.id(id)
									.description(elementDescription.text())
									.imgUrl(elementImg.attr("src"))
									.movieName(elementMovieName.text())
									.movieNameType(elementType.text())
									.urlTomato(urlTomato)
									.infoMovies(infoMovies)
									.reviewUrls(reviewUrls)
									.urlTrailer(elementTrailer.attr("src"))
									.build();
		
		return detail;

	}
	
	public List<Cineplex> getShowTime(int id) throws JSONException, IOException {
		JSONObject json = readJsonFromUrl("https://moveek.com/showtime/movie/"+id);
		JSONArray array = json.getJSONArray("cineplexes");
		List<Cineplex> cineplexs=new ArrayList<>();
		for(Object object:array) {
			JSONObject jsonObject=(JSONObject) object;
			JSONObject cineplexObj=jsonObject.getJSONObject("data");
			JSONArray arrayCinema = jsonObject.getJSONArray("cinemas");
			List<Cinema> cinemas=new ArrayList<>();
			for(Object cinema:arrayCinema) {
				JSONObject jsonCinema=(JSONObject) cinema;
				Cinema cinemaDto=Cinema.builder()
									.cinemaName(jsonCinema.getString("name"))
									.id(jsonCinema.getInt("id"))
									.build();
				cinemas.add(cinemaDto);
			}
			Cineplex cineplex=Cineplex.builder()
									.id(cineplexObj.getInt("id"))
									.cineplexName(cineplexObj.getString("name"))
									.cinemas(cinemas)
									.build();
			cineplexs.add(cineplex);
		}
		return cineplexs;
	}
	// public 
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
