package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.DBUtil;

public class CreateCourseServlet extends HttpServlet  {

	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setAttribute("fehlerName", 0);
		request.setAttribute("fehlerPlaetze", 0);
		request.getRequestDispatcher("/create_course.ftl").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");  
		String name = request.getParameter("name");
		String beschreibung = request.getParameter("beschreibung");
		String schluessel= request.getParameter("schluessel");
		String plaetze = request.getParameter("plaetze");
		/*
		 * Überprüfen, ob der Name des Kurses leer oder länger als 50 Zeichen ist
		 */
		if(name.length()>50 || name.equals("")) {
			request.setAttribute("fehlerName", 1);
			request.setAttribute("fehlerPlaetze", 0);
			RequestDispatcher rd=request.getRequestDispatcher("/create_course.ftl"); 
			rd.include(request, response); 
		/*
		 * Überprüfen, ob die Anzahl der Plätze leer gelassen wurde. Überprüfung, ob nicht größer als 100 läuft im ftl file.
		 */
		}else if(plaetze=="") {
			request.setAttribute("fehlerName", 0);
			request.setAttribute("fehlerPlaetze", 1);
			RequestDispatcher rd=request.getRequestDispatcher("/create_course.ftl"); 
			rd.include(request, response); 
		/*
		 * Wenn es zu keinen Fehlern kommt, den Kurs in die Datenbank eintragen
		 */
		}else {
			String updateKurs = "INSERT INTO dbp134.kurs(name, beschreibungstext, einschreibeschluessel, freieplaetze, ersteller)  "
					+ "VALUES ( ?, ?, ?, ?, 1)";
			try(Connection connection = DBUtil.getExternalConnection()){
				try(PreparedStatement psUKurs = connection.prepareStatement(updateKurs)){
					psUKurs.setString(1, name);
					psUKurs.setString(2, beschreibung);
					psUKurs.setString(3, schluessel);
					psUKurs.setString(4, plaetze);
					psUKurs.executeUpdate();
				}catch(Exception e) {
					e.printStackTrace();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}


			doGet(request,response);
		}
	}
}
