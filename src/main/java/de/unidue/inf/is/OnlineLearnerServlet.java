package de.unidue.inf.is;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.utils.DBUtil;

import java.sql.SQLException;





/**
 * Das könnte die Eintrittsseite sein.
 */
public final class OnlineLearnerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static List<Course> courses;
	private static List<Course> myCourses;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Kurse anfragen, in denen Benutzer 1 schon eingeschrieben ist.
		 */

		String queryMyCourses = "SELECT DISTINCT k.name, b.name, k.freieplaetze, k.kid "
				+ "FROM dbp134.kurs k, dbp134.benutzer b, dbp134.einschreiben e "
				+ "WHERE k.ersteller=b.bnummer AND k.kid = e.kid and e.bnummer=1";
		ResultSet rsMyCourses = null;

		try(Connection connection = DBUtil.getExternalConnection()){
			try(PreparedStatement psMyCourses = connection.prepareStatement(queryMyCourses)){
				rsMyCourses=psMyCourses.executeQuery();

				myCourses = new ArrayList<>();

				while(rsMyCourses.next()) {
					myCourses.add(new Course(rsMyCourses.getString(1),rsMyCourses.getString(2), rsMyCourses.getInt(3), rsMyCourses.getInt(4)));
				}

			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("myCourses", myCourses);

		/*
		 * Kurse anfragen, die für Benutzer 1 noch verfügbar sind
		 */


		String queryCourses = "(SELECT DISTINCT k.name, b.name, k.freieplaetze, k.kid "
				+ "FROM dbp134.kurs k, dbp134.benutzer b, dbp134.einschreiben e "
				+ "WHERE k.ersteller=b.bnummer AND k.kid = e.kid AND e.bnummer<>1 AND k.freieplaetze>0)"
				+ "	MINUS"
				+ "	(SELECT DISTINCT k.name, b.name, k.freieplaetze, k.kid "
				+ "FROM dbp134.kurs k, dbp134.benutzer b, dbp134.einschreiben e "
				+ "WHERE k.ersteller=b.bnummer AND k.kid = e.kid and e.bnummer=1 AND k.freieplaetze>0)";
		ResultSet rsCourses = null;

		try(Connection connection = DBUtil.getExternalConnection()){
			try(PreparedStatement psCourses = connection.prepareStatement(queryCourses)){
				rsCourses=psCourses.executeQuery();

				courses = new ArrayList<>();

				while(rsCourses.next()) {
					courses.add(new Course(rsCourses.getString(1),rsCourses.getString(2), rsCourses.getInt(3), rsCourses.getInt(4)));
				}

			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("courses", courses);



		request.getRequestDispatcher("/onlineLearner_start.ftl").forward(request, response);

	}

	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		String coursekid = request.getParameter("kid");

		String deleteCourse = "DELETE FROM dbp134.kurs WHERE kid=?";
		String deleteEinreichen = "DELETE FROM dbp134.einreichen e WHERE kid=?";
		String selectaid ="SELECT e.aid from dbp134.einreichen e WHERE e.kid=?";
		String deleteAufgabe ="DELETE FROM dbp134.aufgabe WHERE kid=?";
		String deleteEinschreiben = "DELETE FROM dbp134.einschreiben WHERE kid=?";
		String deleteBewerten = "DELETE FROM dbp134.bewerten WHERE aid=?";
		String deleteAbgabe = "DELETE FROM dbp134.abgabe WHERE aid=?";
		try(Connection connection = DBUtil.getExternalConnection()){
			try(PreparedStatement psDEinschreiben = connection.prepareStatement(deleteEinschreiben);
					PreparedStatement psDEinreichen = connection.prepareStatement(deleteEinreichen);
					PreparedStatement psDCourse = connection.prepareStatement(deleteCourse);
					PreparedStatement psDBewerten = connection.prepareStatement(deleteBewerten);
					PreparedStatement psDAbgabe = connection.prepareStatement(deleteAbgabe);
					PreparedStatement psDAufgabe = connection.prepareStatement(deleteAufgabe);
					PreparedStatement psQueryaid = connection.prepareStatement(selectaid)){
				psQueryaid.setString(1, coursekid);
				ResultSet courseaid = psQueryaid.executeQuery();
				
				/*
				 * Kurs aus einschreiben löschem
				 */
				psDEinschreiben.setString(1, coursekid);
				psDEinschreiben.executeUpdate();
				/*
				 * Kurs aus einreichen löschen
				 */
				psDEinreichen.setString(1, coursekid);
				psDEinreichen.executeUpdate();
				/*
				 * Kurs aus aufgabe löschen
				 */
				psDAufgabe.setString(1,  coursekid);
				psDAufgabe.executeUpdate();
				/*
				 * Kurs aus kurs löschen
				 */
				psDCourse.setString(1, coursekid);
				psDCourse.executeUpdate();
				/*
				 * Abgabe IDs aus dem zu löschenden Kurs anfragen
				 */
				while(courseaid.next()) {
					int aid = courseaid.getInt(1);

					
					/*
					 * Kurs aus abgabe löschen
					 */
					psDAbgabe.setInt(1,aid);
					psDAbgabe.executeUpdate();
					/*
					 * Kurs aus bewerten löschen
					 */
					psDBewerten.setInt(1, aid);
					psDBewerten.executeUpdate();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}


		doGet(request,response);	
	}

}