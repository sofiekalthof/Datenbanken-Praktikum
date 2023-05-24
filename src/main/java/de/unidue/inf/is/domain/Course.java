package de.unidue.inf.is.domain;

public class Course {
	private String name;
	private String ersteller;
	private int freiePlaetze;
	private String beschreibungstext;
	private int kid;
	
	public Course() {
		
	}
	
	public Course(String name, String ersteller, int freiePlaetze, int kid) {
		this.name = name;
		this.freiePlaetze = freiePlaetze;
		this.ersteller = ersteller;
		this.kid=kid;
	}
	
	public Course(String ersteller, int freiePlaetze) {
		this.ersteller = ersteller;
		this.freiePlaetze = freiePlaetze;
		
	}
	
	public Course(String ersteller,int freiePlaetze, String beschreibungstext) {
		this.ersteller = ersteller;
		this.freiePlaetze = freiePlaetze;
		this.beschreibungstext = beschreibungstext;
	}
	
	
	public String getName() {
		return name;
	}
	
	public int getFreiePlaetze() {
		return freiePlaetze;
	}
	
	public String getErsteller() {
		return ersteller;
	}
	
	public String getBeschreibungstext() {
		return beschreibungstext;
	}
	
	public int getKid() {
		return kid;
	}

}
