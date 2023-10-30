package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberPwdUpdateController
 */
@WebServlet("/updatePwd.me")
public class MemberPwdUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberPwdUpdateController() {
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
		
		//인코딩 처리하기
		request.setCharacterEncoding("UTF-8");
		
		//입력받은 현재 비밀번호와 변경할 비밀번호를 추출하기
		String userPwd = request.getParameter("userPwd");
		String updatePwd = request.getParameter("updatePwd");
		
		//세션에 있는 로그인 정보로 회원의 식별자 추출하기
		HttpSession session = request.getSession();
		
		int userNo = ((Member)session.getAttribute("loginUser")).getUserNo();

//		위의 코드 한줄 처리하기
//		int userNo = ((Member)request.getSession().getAttribute("loginUser")).getUserNo();
		
		//update 구문이 실행되면 회원정보가 바뀌게 되고 변경된 회원정보를 session에 갱신해야 되기 때문에
		//처리된 결과 : 변경된 회원 정보를 service에서 return받는다!
		Member updateMem = new MemberService().updatePwd(userNo, userPwd, updatePwd);
		
		//처리된 회원정보를 토대로 응답페이지 선택하기
		if(updateMem != null) {	//성공페이지
			//session에 updateMem 객체를 loginUser 키값에 갱신시키기
			session.setAttribute("loginUser", updateMem);
			session.setAttribute("alertMsg", "비밀번호 변경 성공");
			
			//갱신후 mypage로 재요청보내기
			response.sendRedirect(request.getContextPath() + "/myPage.me");	// == /jsp + /myPage.me
			
		}else {	//실패 페이지
			//errorPage에 메시지 담아서 전달하기
			request.setAttribute("errorMsg", "비밀번호 수정실패... 슬기야왜그랫니");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
	}
}
