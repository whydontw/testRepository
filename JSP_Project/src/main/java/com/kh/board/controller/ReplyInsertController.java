package com.kh.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Reply;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class ReplyInsertController
 */
@WebServlet("/insertReply.bo")
public class ReplyInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyInsertController() {
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
	
	//ajax method type을 post로 지정해놓았기 때문에 post 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//인코딩 처리하기
		request.setCharacterEncoding("UTF-8");
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		String content = request.getParameter("content");
		
		//댓글 작성자 번호 추출하기
		//session에서 추출하는 것이 아니라 ajax data 전달할 때 같이 전달해도 된다.
		Member loginUser = (Member)request.getSession().getAttribute("loginUser");
		int replyWriter = loginUser.getUserNo();
		
		
		Reply r = new Reply();
		r.setRefBno(bno);
		r.setReplyContent(content);
		r.setReplyWriter(String.valueOf(replyWriter));
		
		//insert (dml)
		int result = new BoardService().insertReply(r);
		
		
		//처리 결과에 따른 화면 요소는 view에서 결정하기
		response.getWriter().print(result);
		
	}

}
