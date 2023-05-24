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

public class NewAssignmentServlet  extends HttpServlet{

	private static final long serialVersionUID=1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		request.setAttribute("fehlerAbgabe", 0);
		String coursekid = request.getParameter("kid");
		request.setAttribute("kid", coursekid);
		String course ="";
		String selectCourseName = "SELECT k.name "
				+ "FROM dbp134.kurs k "
				+ "WHERE k.kid= ?";
		try(Connection connectionCourse = DBUtil.getExternalConnection()){
			try(PreparedStatement psCourse = connectionCourse.prepareStatement(selectCourseName)){
				psCourse.setString(1,  coursekid);
				ResultSet rsCourse=psCourse.executeQuery();
				while(rsCourse.next()) {
					course= rsCourse.getString(1);
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("coursename", course);
		String aufgabe=request.getParameter("aufgabe");
		request.setAttribute("aufgabename", aufgabe);
		String beschreibung="";
		String queryBeschreibung="SELECT a.beschreibung "
				+ "FROM dbp134.aufgabe a, dbp134.kurs k "
				+ "WHERE a.kid=k.kid AND a.name= ? AND k.name= ?";
		try(Connection connection = DBUtil.getExternalConnection()){
			try(PreparedStatement psBeschreibung =connection.prepareStatement(queryBeschreibung)){
				psBeschreibung.setString(1, aufgabe);
				psBeschreibung.setString(2, course);
				ResultSet rsBeschreibung = psBeschreibung.executeQuery();
				while(rsBeschreibung.next()){
					beschreibung=rsBeschreibung.getString(1);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("beschreibung", beschreibung);
		request.getRequestDispatcher("new_assignment.ftl").forward(request, response);

	}


}

		
	
	
	



