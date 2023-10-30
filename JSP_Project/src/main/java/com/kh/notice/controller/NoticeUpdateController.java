package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeUpdateController
 */
@WebServlet("/update.no")
public class NoticeUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//상세보기 페이지 -> 수정하기 페이지로 이동하기
		//해당 글 정보를 조회해서 수정하기 페이지로 전달하기 작업
		//수정하고자 하는 공지글의 번호를 추출하기
		
		int nno = Integer.parseInt(request.getParameter("nno"));
		Notice n = new NoticeService().selectNotice(nno);
		
		request.setAttribute("n", n);
		
		//수정할 공지글 정보 담았으니 forwarding
		request.getRequestDispatcher("views/notice/noticeUpdateForm.jsp").forward(request, response);
		
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//공지글 변경해보기 updateNotice() 메소드명

		//인코딩 처리
		request.setCharacterEncoding("UTF-8");
		
		//form에서 전달한 title, content, noticeNo가 필요하다.
		String updateTitle = request.getParameter("title");
		String updateContent = request.getParameter("content");
		int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));
		
		Notice n = new Notice();
		n.setNoticeTitle(updateTitle);
		n.setNoticeContent(updateContent);
		n.setNoticeNo(noticeNo);
		
		int result = new NoticeService().updateNotice(n);
		
		if(result > 0) {
			//성공시 변경 성공 메시지 alert과 함께 해당 공지글 상세보기 페이지로 이동하기 (재요청)
			HttpSession session = request.getSession();
			session.setAttribute("alertMsg", "수정 완료~");	//재요청할 때에는 request에 담아서 사용할 수 없음
			response.sendRedirect(request.getContextPath()/*==/jsp*/ + "/detail.no?nno=" + noticeNo);
			
		}else {
			//실패시 에러 페이지로 메시지와 함께 포워딩하기
			request.setAttribute("errorMsg", "수정 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp"/*== localhost:8888/jsp/views/common/errorPage.jsp*/).forward(request, response);
		}
	}
}