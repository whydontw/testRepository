package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.service.MemberService;

/**
 * Servlet implementation class MemberIdCheckController
 */
@WebServlet("/idCheck.me")
public class MemberIdCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberIdCheckController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//파라미터값 잘 넘어오는지 확인하기
		String checkId = request.getParameter("checkId");
//		확인
//		System.out.println(checkId);
		
		
		//해당 아이디값을 데이터베이스에 있는지 없는지 조회처리하기
		//해당 아이디가 존재하는지 숫자를 세서 있다면 사용 못하게 하고 없다면 사용 가능하게 하기
		//1이면 아이디가 이미 존재, 0이면 아이디 사용 가능
		int count = new MemberService().checkId(checkId);
		//count에는 아이디가 있으면(중복) 1, 없으면(사용가능) 0이 반환되어있는 상태
		
		System.out.println(count);
		
		if(count > 0) {	//중복인 경우
			//사용 불가 NNNNN
			response.getWriter().print("NNNNN");
			
		}else {	//중복이 아닌 경우
			//사용 가능
			response.getWriter().print("NNNNY");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
