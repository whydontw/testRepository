package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeDetailController
 */
@WebServlet("/detail.no")
public class NoticeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println(request.getParameter("nno"));
		
//		추출한 글 번호로 해당 공지사항 세부 정보 조회해오기
//		selectNotice 메소드명으로 작업 후 console창에 해당 notice 정보 띄워주기
//		status 제외한 정보 조회해오기
		
//		조회해올때 리스트와 마찬가지로 유저의 네임을 가지고 오자
		
		int noticeNo = Integer.parseInt(request.getParameter("nno"));
		
		
		//조회수 증가 후 해당 글 조회하게 작업하기
		int result = new NoticeService().increaseCount(noticeNo);
		
		
		//조회수 증가가 잘 되었다면 상세정보 보기 요청하기
		if(result > 0) {
			Notice n = new NoticeService().selectNotice(noticeNo);
			request.setAttribute("n", n);
			request.getRequestDispatcher("views/notice/noticeDetailView.jsp").forward(request, response);
			
			
		}else {
			//조회수 증가가 실패했다면
			//에러페이지로 보내기
			request.setAttribute("errorMsg", "게시글 조회 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
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
