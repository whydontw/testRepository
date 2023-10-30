package com.kh.member.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberEnrollController
 */
@WebServlet("/enrollForm.me")
public class MemberEnrollController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String parameter;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberEnrollController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//location.href 요청: get 방식
		//회원가입 페이지로 요청 위임하기(포워딩)
		
//		RequestDispatcher view = request.getRequestDispatcher("views/member/memberEnrollForm.jsp");
//		view.forward(request, response);
		//위의 두줄 아래로 한줄처리하기!
		request.getRequestDispatcher("views/member/memberEnrollForm.jsp").forward(request, response);
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// insertMember로 진행하기
		// 배열을 데이터베이스에 그대로 담을 수 없으므로 문자열 처리 후에 담아주어야 한다.
		// String.join()메소드 활용하기
		// 메소드명은 insertMember()로 진행

//		System.out.println("요청확인~~!");
		
		//1) 인코딩 처리하기
		request.setCharacterEncoding("UTF-8");
		
		//2) 요청시 전달된 데이터 추출하기
		String userId = request.getParameter("userId");			//필수
		String userPwd = request.getParameter("userPwd");		//필수
		String userName = request.getParameter("userName");		//필수
		
		//아래 세개는 입력을 안할 수 있음
		String phone = request.getParameter("phone");			//빈 문자열로 넘어올 수 있음
		String email = request.getParameter("email");			//빈 문자열로 넘어올 수 있음
		String address = request.getParameter("address");		//빈 문자열로 넘어올 수 있음
		 
		String[] interests = request.getParameterValues("interest");	//빈 문자열 배열 혹은 null값으로 넘어옴
		
		String interest = "";
		
		//null값을 java에서 미리 처리하고 나면 프론트에서 null값을 따로 처리할 필요가 없다.
		if(interests != null) {
			interest = String.join("," , interests);
		}
		
		//Member 객체에 담아서 전달하기 - 매개변수 생성자 만들기(데이터가 여러가지라서)
		Member m = new Member(userId, userPwd, userName, phone, email, address, interest);
		
		
		
		//Service에 가공 처리한 m 객체 전달하기 (처리된 행 수 돌려받기)
		int result = new MemberService().insertMember(m);
		
		
		//처리된 행 수를 토대로 사용자에게 보여줄 응답화면 지정하기
		
		// 회원가입 성공 시에 session에 회원가입 성공 메시지 담고 index 페이지로 재요청
		// 회원가입 실패시 request에 에러 메시지 담고 errorPage로 포워딩 해보기
		if(result > 0) {	//성공
			HttpSession session = request.getSession();

			//menubar에 작성되어 있는 알림 활용하기
			session.setAttribute("alertMsg", "회원가입 성공~");
			
			response.sendRedirect(request.getContextPath());	// ==/jsp
			
		}else {		//실패(실패시 화면)
//			HttpSession session = request.getSession();
			
			//뢰원가입 실패시 request에 에러 메시지 담고 errorPage로 포워딩해보기
			request.setAttribute("alertMsg", "회원가입 실패~");

			//request.getRequestDispatcher 객체에 보여줄 뷰 페이지 정보(경로) 설정한 뒤 위임하기(forward)하기
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);	//한줄처리로 슝~
		}
	}
}
