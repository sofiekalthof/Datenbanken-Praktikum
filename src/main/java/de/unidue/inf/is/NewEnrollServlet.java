package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.DBUtil;

public class NewEnrollServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String coursekid = request.getParameter("kid");
		request.setAttribute("kid", coursekid);
		String courseName ="";
		String selectCourseName = "SELECT k.name "
				+ "FROM dbp134.kurs k WHERE k.kid=?";
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
		request.setAttribute("fehlerSchluessel",0);
		request.setAttribute("fehlerPlaetze", 0);
		/*
		 * Einschreibeschlüssel des Kurses anfragen, um zu wissen, ob man Textfeld braucht oder nicht
		 */
		String einschreibeschluessel="";
		String querySchluessel = "Select k.einschreibeschluessel, k.kid "
				+ "FROM dbp134.kurs k where k.name= ?";
		try(Connection connection=DBUtil.getExternalConnection()){
			try(PreparedStatement psSchluessel = connection.prepareStatement(querySchluessel)){
				psSchluessel.setString(1, courseName);
				ResultSet rsSchluessel = psSchluessel.executeQuery(); 
				while(rsSchluessel.next()) {
					einschreibeschluessel = rsSchluessel.getString(1);

					if(einschreibeschluessel != null) {
						request.setAttribute("einschreibeschluessel", einschreibeschluessel);
					} else {
						request.setAttribute("einschreibeschluessel", "");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}


		request.setAttribute("coursename", courseName);
		request.getRequestDispatcher("/new_enroll.ftl").forward(request, response);
	}


}