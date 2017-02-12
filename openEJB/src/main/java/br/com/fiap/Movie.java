package br.com.fiap;

public class Movie {
	
	private String  director;
	private String title;
	private int year;
	
	
	
	
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public Movie() {
	}
	
	public Movie(String diretor, String titulo, Integer ano) {
		this.director = diretor;
		this.title = titulo;
		this.year = ano;
	}
	

}
