package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.notice.model.service.NoticeService;

/**
 * Servlet implementation class NoticeDeleteController
 */
@WebServlet("/delete.no")
public class NoticeDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String noticeNo = request.getParameter("nno");
		
		int result = new NoticeService().deleteNotice(noticeNo);
		
//		System.out.println("삭제결과: " + result);
		
		
		//세션
		HttpSession session = request.getSession();
		
		if(result > 0) {	//성공적으로 삭제 완료
			session.setAttribute("alertMsg", "글 삭제 성공~");
			response.sendRedirect(request.getContextPath() + "/list.no");	//목록페이지 보내주기
		}else {				//실패
			session.setAttribute("alertMsg", "글 삭제 실패~");
			request.getRequestDispatcher(request.getContextPath() + "/detail.no?nno=" + noticeNo).forward(request, response);	//상세페이지 보내주기
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
