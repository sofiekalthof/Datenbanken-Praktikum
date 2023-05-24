package de.unidue.inf.is.domain;

public class Aufgabe {
	private String name;
	private String abgabetext;
	private String note;
	
	public Aufgabe(String name, String abgabetext, String note) {
		this.name = name;
		this.abgabetext = abgabetext;
		this.note=note;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAbgabetext() {
		return abgabetext;
	}
	
	public String getNote() {
		return note;
	}
	

}
