package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;

/**
 * Servlet implementation class MemberDeleteController
 */
@WebServlet("/delete.me")
public class MemberDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		String userPwd = request.getParameter("userPwd");
		
		//메소드명: deleteMember로 하기
		int result = new MemberService().deleteMember(userNo, userPwd);
		
		
		
		//삭제 성공시 회원 탈퇴 완료 메시지를 세션에 담고 세션에 있던 loginUser정보 삭제하고
		if(result > 0) {	//성공
			
			
			HttpSession session = request.getSession();
			session.setAttribute("alertMsg", "잘 가요 정없는 슬기");
			
			//로그인 정보 지워주기
			//인덱스(메인페이지)로 재요청하기
			//세션 정보 전부 지우기(세션 만료)
			session.invalidate();
			
			response.sendRedirect(request.getContextPath());
			
			
		}else {
			
			
			//실패시 errorPage로 에러 메시지와 함께 forwarding(위임하기)
			request.setAttribute("errorMsg", "탈퇴실패~!~!~!~!~!~!~!~!~!!~ 당신은 슬기의 늪에서 벗어나지 못했답니다.");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
	}
	
	
	//다 한사람
	//updatePwd 메소드명으로 비밀번호 변경해보기
	//비밀번호 변경은 변경할 비밀번호와 비밀번호 확인 값이 같지 않으면 서버에 전송되지 않도록 스크립트 구현하기
	
	
	

}
