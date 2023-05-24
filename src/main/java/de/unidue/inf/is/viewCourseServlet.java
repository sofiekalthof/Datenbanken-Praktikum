package de.unidue.inf.is;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.DBUtil;
import de.unidue.inf.is.domain.Aufgabe;
import de.unidue.inf.is.domain.Course;

public class viewCourseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static List<Course> course;
	private static List<Aufgabe> aufgabe;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		String coursekid = request.getParameter("kid");
		request.setAttribute("kid", coursekid);
		String courseName ="";
		String selectCourseName = "SELECT k.name "
				+ "FROM dbp134.kurs k "
				+ "WHERE k.kid= ?";
		try(Connection connectionCourse = DBUtil.getExternalConnection()){
			try(PreparedStatement psCourse = connectionCourse.prepareStatement(selectCourseName)){
				psCourse.setString(1, coursekid);
				ResultSet rsCourse=psCourse.executeQuery();
				while(rsCourse.next()) {
					courseName = rsCourse.getString(1);
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("coursename",courseName );
		String bnummer = request.getParameter("b");
		request.setAttribute("bnummer",bnummer);
		/*
		 * Die Informationen zum Kurs anfragen
		 */
		String queryCourseInfo = "SELECT b.name, k.beschreibungstext, k.freieplaetze "
				+ "FROM dbp134.kurs k, dbp134.benutzer b, dbp134.einschreiben e "
				+ "WHERE k.ersteller=b.bnummer AND k.kid = e.kid AND k.name= ? AND e.bnummer=b.bnummer";
		/*
		 * Aufgabename, Abgabetext und durchschnittliche Note für Benutzer 1 anfragen
		 */
		String queryAbgabeInfo = "WITH bewertung(aid,note) AS (SELECT b.aid, avg(b.note) FROM dbp134.bewerten b GROUP BY b.aid) "
				+ "SELECT a.name, ab.abgabetext, b.note "
				+ "FROM dbp134.kurs k JOIN"
				+ "(dbp134.aufgabe a FULL OUTER JOIN"
				+ "(dbp134.einreichen e FULL OUTER JOIN "
				+ "(dbp134.abgabe ab FULL OUTER JOIN bewertung b "
				+ "ON ab.aid=b.aid) "
				+ "ON e.aid=ab.aid)"
				+ "ON a.anummer=e.anummer AND a.kid=e.kid AND e.bnummer=1)"
				+ "ON k.kid=a.kid AND k.name= ? "
				+ "ORDER BY a.anummer ASC" ;

		ResultSet rsCourseInfo = null;
		ResultSet rsAbgabeInfo = null;
		try(Connection connection = DBUtil.getExternalConnection()){
			try(PreparedStatement psCourseInfo = connection.prepareStatement(queryCourseInfo)){
				psCourseInfo.setString(1, courseName);
				rsCourseInfo =psCourseInfo.executeQuery();
				course=new ArrayList<>();
				while(rsCourseInfo.next()) {
					course.add(new Course(rsCourseInfo.getString(1),rsCourseInfo.getInt(3), rsCourseInfo.getString(2)));		

				}
			}
			try(PreparedStatement psAbgabeInfo = connection.prepareStatement(queryAbgabeInfo)){
				psAbgabeInfo.setString(1, courseName);
				rsAbgabeInfo=psAbgabeInfo.executeQuery();
				aufgabe = new ArrayList<>();
				while(rsAbgabeInfo.next()) {
					/*
					 * Fall, dass eine Abgabe vorhanden ist, die aber noch nicht bewertet wurde
					 */
					if(rsAbgabeInfo.getString(2) != null) {
						if(rsAbgabeInfo.getString(3)!=null) {
							aufgabe.add(new Aufgabe(rsAbgabeInfo.getString(1),rsAbgabeInfo.getString(2),rsAbgabeInfo.getString(3)));
						}else {
							aufgabe.add(new Aufgabe(rsAbgabeInfo.getString(1),rsAbgabeInfo.getString(2), "Noch keine Bewertung"));
						}
					/*
					 * Fall, dass keine Abgabe und dementsprechend noch keine Bewertung vorhanden ist
					 */
					}else {
						aufgabe.add(new Aufgabe(rsAbgabeInfo.getString(1),"Keine Abgabe", "Keine Bewertung"));

					}
				}
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("course", course);
		request.setAttribute("aufgaben", aufgabe);
		request.getRequestDispatcher("/view_course.ftl").forward(request, response);
	}
	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		/*
		 * Parameter a gibt an, ob in der Post Methode eine Abgabe oder eine Einschreibung vorgenommern werden soll
		 */
		String a=request.getParameter("a");
		response.setContentType("text/html"); 
		/*
		 * Wenn a=0, dann soll der Benutzer in diesen Kurs eingeschrieben werden
		 */
		if(a.equals("0")) {
			String coursekid = request.getParameter("kid");
			request.setAttribute("kid", coursekid);
			String courseName ="";
			String selectCourseName = "SELECT k.name FROM dbp134.kurs k WHERE k.kid= ?";
			try(Connection connectionCourse = DBUtil.getExternalConnection()){
				try(PreparedStatement psCourse = connectionCourse.prepareStatement(selectCourseName)){
					psCourse.setString(1, coursekid);
					ResultSet rsCourse=psCourse.executeQuery();
					while(rsCourse.next()) {
						courseName = rsCourse.getString(1);
					}

				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			String einschreibeschluessel="";

			String inputeschluessel="";
			inputeschluessel=request.getParameter("schluessel");
			/*
			 * Einschreibeschluessel des Kurses aus der Datenbank anfragen
			 */
			String querySchluessel = "Select k.einschreibeschluessel, k.freieplaetze "
					+ "FROM dbp134.kurs k "
					+ "WHERE k.name= ? AND k.kid= ?" ;
			try(Connection connection=DBUtil.getExternalConnection()){
				try(PreparedStatement psSchluessel = connection.prepareStatement(querySchluessel)){
					psSchluessel.setString(1, courseName);
					psSchluessel.setString(2, coursekid);
					ResultSet rsSchluessel = psSchluessel.executeQuery(); 
					while(rsSchluessel.next()) {
						einschreibeschluessel = rsSchluessel.getString(1);
						int freieplaetze = rsSchluessel.getInt(2);
						/*
						 * Überprüfen, ob es noch einen freien Platz in dem Kurs gibt
						 */
						if(freieplaetze>0) {
							/*
							 * Überprüfen, ob der Einschreibeschlüssel null ist
							 */
							if(einschreibeschluessel != null) {
								/* 
								 * Überprüfen, ob der eingegebene Einschreibeschlüssel gleich dem Einschreibeschlüssel aus der Datenbank ist
								 */
								if(einschreibeschluessel.equals(inputeschluessel)){
									/*
									 * Einschreibeschlüssel=eingegebenem Schlüssel, Fehler auf 0 gesetzt, also kein Fehler vorhanden
									 * Benutzer 1 wird in den ausgewählten Kurs in die Datenbank eingeschrieben
									 */
									request.setAttribute("fehlerSchluessel", 0);
									request.setAttribute("fehlerPlaetze", 0);
									String updateEinschreiben = "INSERT INTO dbp134.einschreiben(bnummer, kid) "
											+ "VALUES (1, ?)";
									try(PreparedStatement psUEinschreiben = connection.prepareStatement(updateEinschreiben)){
										psUEinschreiben.setString(1, coursekid);
										psUEinschreiben.executeUpdate();
									}
									String updateKurs = "UPDATE dbp134.kurs k "
											+ "SET k.freieplaetze = k.freieplaetze-1 "
											+ "WHERE k.name= ?";
									try(PreparedStatement psUKurs = connection.prepareStatement(updateKurs)){
										psUKurs.setString(1,courseName);
										psUKurs.executeUpdate();
									}
									doGet(request,response);
									
								} else {
									/*
									 * Einschreibeschlüssel != eingegebenem Schlüssel, Fehler beim Schlüssel auf 1 setzen
									 * Fehlermeldung anzeigen und Benutzer nicht einschreiben, sondern Seite mit Fehlermeldeung neu laden
									 */
									request.setAttribute("fehlerSchluessel", 1);
									request.setAttribute("fehlerPlaetze", 0); 
									request.setAttribute("kid", coursekid);
									request.setAttribute("coursename", courseName);
									request.setAttribute("einschreibeschluessel", einschreibeschluessel);
									RequestDispatcher rd=request.getRequestDispatcher("/new_enroll.ftl"); 
									rd.include(request, response); 

								}
							}else {
								/*
								 * Einschreibeschlüssel ist null, Benutzer 1 wird in die Datenbank für den Kurs eingeschrieben
								 */
								request.setAttribute("fehlerSchluessel", 0);
								request.setAttribute("fehlerPlaetze", 0);
								String updateEinschreiben = "INSERT INTO dbp134.einschreiben(bnummer, kid) "
										+ "VALUES (1, ?)";
								try(PreparedStatement psUEinschreiben = connection.prepareStatement(updateEinschreiben)){
									psUEinschreiben.setString(1, coursekid);
									psUEinschreiben.executeUpdate();
								}
								String updateKurs = "UPDATE dbp134.kurs k "
										+ "SET k.freieplaetze = k.freieplaetze-1 "
										+ "WHERE k.name= ?";
								try(PreparedStatement psUKurs = connection.prepareStatement(updateKurs)){
									psUKurs.setString(1,courseName);
									psUKurs.executeUpdate();
								}
								doGet(request,response);
							}
						}else {
							/*
							 * Kein freier Platz mehr im Kurs, Fehler bei den Plätzen auf 1
							 * Fehlermeldung anzeigen und Benutzer nicht einschreiben, sondern Seite mit Fehlermeldeung neu laden
							 */
							request.setAttribute("fehlerPlaetze", 1);
							request.setAttribute("fehlerSchluessel", 0);
							request.setAttribute("coursename", courseName);
							request.setAttribute("kid", coursekid);
							request.setAttribute("einschreibeschluessel", einschreibeschluessel);
							RequestDispatcher rd=request.getRequestDispatcher("/new_enroll.ftl"); 
							rd.include(request, response); 
						}

					}
				}

			}catch(Exception e) {
				e.printStackTrace();
			}
			/*
			 * Wenn a = 1, dann möchte der Benutzer eine Abgabe für den Kurs abgeben
			 */
		} else if(a.equals("1")) {

			String coursekid = request.getParameter("kid");
			request.setAttribute("kid", coursekid);
			String course ="";
			String selectCourseName = "SELECT k.name "
					+ "FROM dbp134.kurs k "
					+ "WHERE k.kid= ?";
			try(Connection connectionCourse = DBUtil.getExternalConnection()){
				try(PreparedStatement psCourse = connectionCourse.prepareStatement(selectCourseName)){
					psCourse.setString(1, coursekid);
					ResultSet rsCourse=psCourse.executeQuery();
					while(rsCourse.next()) {
						course = rsCourse.getString(1);
					}

				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			String aufgabe=request.getParameter("aufgabe");
			String abgabetext=request.getParameter("abgabe");
			String beschreibung = request.getParameter("beschreibung");
			request.setAttribute("coursename",course);
			request.setAttribute("b", 1);
			/*
			 * Überprüfen, ob Benutzer 1 schon eine Abgabe für diese Aufgabe getätigt hat
			 */
			String überprüfeabgabe ="SELECT e.aid "
					+ "FROM dbp134.einreichen e, dbp134.aufgabe a, dbp134.kurs k "
					+ "WHERE k.kid=e.kid AND k.name= ? and a.anummer=e.anummer AND a.name= ? AND e.bnummer=1 AND a.kid=k.kid AND a.kid=e.kid";
			try(Connection connection = DBUtil.getExternalConnection()){
				try(PreparedStatement psAbgabe =connection.prepareStatement(überprüfeabgabe)){
					psAbgabe.setString(1, course);
					psAbgabe.setString(2, aufgabe);
					ResultSet rsAbgabe = psAbgabe.executeQuery();
					/*
					 * Benutzer 1 hat eine Abgabe bereits getätigt, setze Fehler bei der Abgabe auf 1 und zeige Fehlermeldung
					 */
					if(rsAbgabe.next()){
						request.setAttribute("kid", coursekid);
						request.setAttribute("coursename", course);
						request.setAttribute("aufgabename", aufgabe);
						request.setAttribute("beschreibung", beschreibung);
						request.setAttribute("fehlerAbgabe", 1);
						RequestDispatcher rd=request.getRequestDispatcher("/new_assignment.ftl"); 
						rd.include(request, response); 
					/*
					 * Benutzer 1 hat noch keine Abgabe getätigt, Fehler bei der Abgabe auf 0, also kein Fehler vorhanden
					 * Abgabe wird in die Datenbank eingetragen	
					 */
					}else {
						request.setAttribute("fehlerAbgabe", 0);
						String insertabgabe="INSERT INTO dbp134.abgabe(abgabetext) "
								+ "VALUES(?)";
						String queryaid="SELECT ab.aid "
								+ "FROM dbp134.abgabe ab "
								+ "WHERE ab.abgabetext LIKE ?";
						String queryAnummer="SELECT a.anummer "
								+ "FROM dbp134.kurs k, dbp134.aufgabe a "
								+ "WHERE a.kid=k.kid AND k.name= ? AND a.name= ? AND k.kid =?";

						try(Connection connection1 = DBUtil.getExternalConnection()){
							try(PreparedStatement psInsertAbgabe =connection1.prepareStatement(insertabgabe)){
								psInsertAbgabe.setString(1, abgabetext);
								psInsertAbgabe.executeUpdate();
							}
						}catch(Exception e) {
							e.printStackTrace();
						}

						try(Connection connection2 = DBUtil.getExternalConnection()){
							try(PreparedStatement psAid=connection2.prepareStatement(queryaid);
									PreparedStatement psAnummer=connection2.prepareStatement(queryAnummer)){
								psAid.setString(1, abgabetext);
								psAnummer.setString(1, course);
								psAnummer.setString(2, aufgabe);
								psAnummer.setString(3, coursekid);
								ResultSet rsAid = psAid.executeQuery();
								ResultSet rsAnummer =psAnummer.executeQuery();
								while(rsAid.next()) {
									int aid=rsAid.getInt(1);
									while(rsAnummer.next()) {
										int anummer=rsAnummer.getInt(1);
										String inserteinreichen="INSERT INTO dbp134.einreichen(bnummer, kid, anummer, aid) "
												+ "VALUES (1,?,?,?)";

										try(Connection connection3 = DBUtil.getExternalConnection()){
											try(PreparedStatement psInsertEinreichen=connection3.prepareStatement(inserteinreichen)){
												psInsertEinreichen.setString(1,coursekid);
												psInsertEinreichen.setInt(2, anummer);
												psInsertEinreichen.setInt(3, aid);
												psInsertEinreichen.executeUpdate();
											}


										}catch(Exception e) {
											e.printStackTrace();
										}
									}	
								}
							}

						}catch(Exception e) {
							e.printStackTrace();


						}
						doGet(request,response);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}


		}
	}

}
 
		 


