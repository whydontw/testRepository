package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MemberLogoutController
 */
@WebServlet("/logout.me")
public class MemberLogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberLogoutController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//로그아웃
		//로그인 되어 있는 회원정보를 지워주면 그게 로그아웃~!
		//로그인 정보가 있는 객체: session
		//session에서 해당하는 로그인 유저 삭제하기
		//데이터 지우는 메소드: removeAttribute("키값");
		//세션 객체 얻어와서 remove하기
		HttpSession session = request.getSession();
		session.removeAttribute("loginUser");		//loginUser 키 값 데이터 지우기
		
		//사용자에게는 메인화면 보여주기(재요청)
		response.sendRedirect(request.getContextPath());		// ContextPath == "/jsp"
		
		//세션 정보 전부 지우기(세션 만료)
		//일종의 세션 초기화 작업
		//session.invalidate();
		session.invalidate();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
