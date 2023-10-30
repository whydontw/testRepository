package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;
import com.kh.common.model.vo.Attachment;
import com.kh.common.model.vo.Category;
import com.kh.common.model.vo.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardUpdateController
 */
@WebServlet("/update.bo")
public class BoardUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BoardUpdateController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int bno = Integer.parseInt(request.getParameter("bno"));

		// 수정하려면 조회부터 해야한다

		BoardService bs = new BoardService();

		// 수정페이지에 띄워줄 데이터 조회해오기
		Board b = bs.selectBoard(bno);

		// 카테고리 목록 조회하기
		ArrayList<Category> clist = bs.selectCategoryList();

		// 첨부파일 정보
		Attachment at = bs.selectAttachment(bno);

		request.setAttribute("b", b);
		request.setAttribute("category", clist);
		request.setAttribute("at", at);

		request.getRequestDispatcher("views/board/boardUpdateForm.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// multipart/form-data로 전송되었는지 확인
//		System.out.println("전송되었는지 확인");
		
		// multipart/form-data로 전달되었다면
		// 기존 request 객체에서 값을 추출할 수 없기 때문에 MultipartRequest 객체로 변환 후 값을 추출해야 한다. (ex.
		// request.getParameter("") 사용 못 해)
		// 이때 MultipartRequest 객체를 생성하는 구문이 업로드 처리를 하는 구문이기 때문에 해당 파일에 대한 설정을 해줘야 함.
		if (ServletFileUpload.isMultipartContent(request)) {

			// 전송파일 용량 제한
			int maxSize = 10 * 1024 * 1024;

			// 업로드된 파일 저장 위치(물리적 경로 찾기)
			String savePath = request.getSession().getServletContext().getRealPath("/resources/uploadFiles/");

			// MultipartRequest 객체 만들기(객체가 생성되며 넘어온 파일이 있다면 서버에 파일 업로드가 된다.)
			MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8",
					new MyFileRenamePolicy());

			// 변환된 multiRequest 객체로 데이터 추출하기
			int boardNo = Integer.parseInt(multiRequest.getParameter("boardNo"));
			String category = multiRequest.getParameter("category");
			String title = multiRequest.getParameter("title");
			String content = multiRequest.getParameter("content");

			Board b = new Board();
			b.setBoardNo(boardNo);
			b.setBoardContent(content);
			b.setBoardTitle(title);
			b.setCategory(category);

			// 만약 새로운 첨부파일이 전달되었다면
			Attachment at = null;
			
			//기존 첨부파일이 있을 때 새로운 첨부파일이 들어온 경우 (기존 첨부파일 삭제해주기)
			boolean flag = false;
			
			

			if (multiRequest.getOriginalFileName("reUploadFile") != null) {
				at = new Attachment();

				// 전달된 파일명
				at.setOriginName(multiRequest.getOriginalFileName("reUploadFile"));
				// 서버에 실제적으로 업로드되는 파일명
				at.setChangeName(multiRequest.getFilesystemName("reUploadFile"));

				// 저장경로 넣기
				at.setFilePath("/resources/uploadFiles/");

				// 만약 기존에도 파일이 있었다면 파일번호와 파일명을 전달받기
				if (multiRequest.getParameter("originFileNo") != null) {
					// 새로 첨부된 파일이 있고 기존 파일도 있다면 파일 정보가 등록된 데이터에서 >변경작업<을 한다. (update)
					// 파일번호(식별자)를 이용하여 기존 데이터에서 변경하기(덮어쓰기)
					at.setFileNo(Integer.parseInt(multiRequest.getParameter("originFileNo")));
					
					
					//기존 첨부파일 삭제 플래그
					flag = true;

				}else {
					// 새로 전달된 파일은 있지만 기존에는 파일이 없었을 때
					// 데이터베이스에 Attachment 정보를 추가해야 한다.
					// 참조 게시글 번호를 세팅
					at.setRefBno(boardNo);
					
				}

			}
			
			
			//서비스로 요청보내기
			//b, at
			
			//새로운 첨부파일이 없는 경우 b, at(null) - b : update
			//새로운 첨부파일이 있고 기존에 첨부파일이 있는 경우 b, at(fileNo) : b-update, at-update
			//새로운 첨부파일이 있고 기존에 첨부파일이 없는 경우 b, at(refBno) : b-update, at-insert
			
			int result = new BoardService().updateBoard(b, at);
			
			
			HttpSession session = request.getSession();
			//성공시에 상세페이지로 성공메시지와 함께
			if(result > 0) {
				
				//기존에 있었던 파일이 지워져야 한다면
				if(flag) {
					//기존 파일 삭제해주기 (저장경로 + 원본파일명) - 원본파일명은 view에서 넘겨줌
					new File(savePath + multiRequest.getParameter("originFileName"));
				}
				
				session.setAttribute("alertMsg", "게시글 수정 성공");
				response.sendRedirect(request.getContextPath() + "/detail.bo?bno=" + boardNo);
				
			}else {
				//실패시에 상세페이지로 성공메시지와 함께
				session.setAttribute("alertMsg", "게시글 수정 실패");
				response.sendRedirect(request.getContextPath() + "/detail.bo?bno=" + boardNo);
			}

		}

	}

}
