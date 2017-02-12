package br.com.fiap;

import static javax.ejb.LockType.READ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Singleton
@Lock(READ)
@Path("/ejb")
public class SimpleRestEJB {
	
	@Resource
    private DataSource dbUnit;
	
	@PostConstruct
    private void construct() throws Exception {
        Connection connection = dbUnit.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE movie ( director VARCHAR(255), title VARCHAR(255), year integer)");
            stmt.execute();
        } finally {
            connection.close();
        }
    }

	@Path("/db/insert")
    public void addMovie(Movie filme) throws Exception {
        Connection conn = dbUnit.getConnection();
        try {
            PreparedStatement sql = conn.prepareStatement("INSERT into movie (director, title, year) values (?, ?, ?)");
            sql.setString(1, filme.getDirector());
            sql.setString(2, filme.getTitle());
            sql.setInt(3, filme.getYear());
            sql.execute();
        } finally {
            conn.close();
        }
    }

	@Path("/db/delete")
    public void deleteMovie(Movie filme) throws Exception {
        Connection conn = dbUnit.getConnection();
        try {
            PreparedStatement sql = conn.prepareStatement("DELETE from movie where director = ? AND title = ? AND year = ?");
            sql.setString(1, filme.getDirector());
            sql.setString(2, filme.getTitle());
            sql.setInt(3, filme.getYear());
            sql.execute();
        } finally {
            conn.close();
        }
    }

	@Path("/db/movies")
    public List<Movie> getMovies() throws Exception {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        Connection conn = dbUnit.getConnection();
        try {
            PreparedStatement sql = conn.prepareStatement("SELECT director, title, year from movie");
            ResultSet set = sql.executeQuery();
            while (set.next()) {
                Movie movie = new Movie();
                movie.setDirector(set.getString("director"));
                movie.setTitle(set.getString("title"));
                movie.setYear(set.getInt("year"));
                movies.add(movie);
            }
        } finally {
            conn.close();
        }
        return movies;
    }
	
	@Path("/db/movie/{id}")	
	@GET
	@Produces
    public String getMovieTitle(@PathParam("id") String id) throws Exception {
        ArrayList<Movie> filmes = new ArrayList<Movie>();
        Connection conn = dbUnit.getConnection();
        try {
            PreparedStatement sql = conn.prepareStatement("SELECT director, title, year from movie");
            ResultSet set = sql.executeQuery();
            while (set.next()) {
                Movie filme = new Movie();
                filme.setDirector(set.getString("director"));
                filme.setTitle(set.getString("title"));
                filme.setYear(set.getInt("year"));
                filmes.add(filme);
            }
        } finally {
            conn.close();
        }
        return filmes.get(Integer.parseInt(id)).getTitle();
    }

	@GET
	public String ejb(){
		return "Ejb ok @ " + new Date().toString();
	}
	
	
}
