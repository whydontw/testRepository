package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.vo.Member;
import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeInsertController
 */
@WebServlet("/insert.no")
public class NoticeInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//enrollForm으로 요청 위임하기
		request.getRequestDispatcher("views/notice/noticeEnrollForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//encoding 처리하기
		request.setCharacterEncoding("UTF-8");
		
		
		//제목, 내용, 작성자번호를 추출해서 Notice객체에 담고 Service - DAO - DB 순으로 처리 후 돌아와서 사용자에게 보여줄 화면 지정하기
		String noticeTitle = request.getParameter("title");
		String noticeContent = request.getParameter("content");
		
//		HttpSession session = request.getSession();
//		int uno = ((Member)session.getAttribute("loginUser")).getUserNo();
//		String userNo = String.valueOf(uno);
		String userNo = request.getParameter("userNo");
		
		Notice n = new Notice();
		n.setNoticeTitle(noticeTitle);
		n.setNoticeContent(noticeContent);
		n.setNoticeWriter(userNo);
		
		int result = new NoticeService().insertNotice(n);	//제목, 내용, 회원번호를 갖는 notice 객체 전달
		
		
		//insertNotice() 로 지정하기
		//성공했을 경우: 공지글 작성 성공 메시지와 함께 list.no 즉 목록페이지를 띄워주기(alert)
		
		HttpSession session = request.getSession();
		
		if(result > 0) {
			session.setAttribute("alertMsg", "슬기롭게 공지 등록 완료");
			//갱신후 mypage로 재요청보내기
			response.sendRedirect(request.getContextPath() + "/list.no");
			
		}else {
			//실패했을 경우: 에러 메시지와 함께 보내기 or 목록 페이지 띄워주면서 실패메시지 alert 해주기
			session.setAttribute("alertMsg", "공지글 작성에 실패했습니당.. 슬기로움을 잃었군요.");
			response.sendRedirect(request.getContextPath() + "/list.no");
		}
	}

}
