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
 * Servlet implementation class MemberUpdateController
 */
@WebServlet("/update.me")
public class MemberUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdateController() {
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
		
		
		//마이페이지에서 정보변경 버튼을 누르면 해당 데이터들을 가지고와서 가공처리 후
		//데이터베이스에 update 구문 실행 후 해당 결과값으로 transaction(commit 또는 rollback) 처리하고
		//성공시에는 해당 유저의 정보를 다시 조회하여 컨트롤러로 해당 정보 반환 후 session 갱신 시키기
		
		//1) 인코딩 처리하기
		request.setCharacterEncoding("UTF-8");
		
		//선택 부분만 바꾸는 게 아니라 변경전 데이터도 함께 가져와서 일괄 처리해버리기
		//2) 전달받은 데이터 추출하기
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interests = request.getParameterValues("interest");
		
		String interest = "";
		if(interests != null) {
			interest = String.join(",", interests);
		}
		
		//객체에 담아주기
		Member m = new Member(userId, userName, phone, email, address, interest);
		
		//서비스에 요청보내기(갱신된 회원정보를 반환받음. (session 갱신을 위한))
		Member updateMember = new MemberService().updateMember(m);
		
		//응답화면 설정하기
		if(updateMember == null) { //실패~
			request.setAttribute("errorMsg", "회원정보를 제대로 고치세요");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}else {	//성공
			//성공시 갱신된 정보를 세션에 담고 myPage로 재요청하기
			
			//1)Session 얻어내기
			HttpSession session = request.getSession();
			
			//기존 key값에 넣으면 같은 key값은 존재할 수 없기 때문에 해당 key값의 value가 갱신된다.(덮어쓰기)
			session.setAttribute("loginUser", updateMember);
			session.setAttribute("alertMsg", "정보수정 완뇨~");
			
			//마이페이지로 재요청 보내기
			response.sendRedirect(request.getContextPath() + "/myPage.me");
			
		}
	
	}

}
