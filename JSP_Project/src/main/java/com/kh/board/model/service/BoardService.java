package com.kh.board.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDao;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Reply;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.Attachment;
import com.kh.common.model.vo.Category;
import com.kh.common.model.vo.PageInfo;

public class BoardService {
	
	
	//게시글 개수 조회 메소드
	public int listCount() {
		Connection conn = JDBCTemplate.getConnection();
		
		//게시글 개수 받아줄 변수 준비
		int count = new BoardDao().listCount(conn);
		
		JDBCTemplate.close(conn);
		
		
		return count; //게시글 개수 돌려주기
	}

	
	
	//게시글 목록 조회 메소드
	public ArrayList<Board> selectList(PageInfo pi){
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Board> list = new BoardDao().selectList(conn,pi);
		
		JDBCTemplate.close(conn);
		
		return list; 
	}

	
	
	//조회수 증가 메소드
	public int increaseCount(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().increaseCount(boardNo,conn);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		
		return result; //결과행수 돌려주기
	}

	
	
	//게시글 상세조회
	public Board selectBoard(int boardNo) {
		Connection conn = JDBCTemplate.getConnection();
		
		Board b = new BoardDao().selectBoard(conn,boardNo);
		
		//조회구문이니 트랜잭션 처리 필요없음
		
		JDBCTemplate.close(conn); //자원반납 
		
		return b;
	}


	
	//게시글 등록하는 메소드
	public int insertBoard(Board b, Attachment at) {
		Connection conn = JDBCTemplate.getConnection();
		
		
		//게시글 정보 등록부터 하기(첨부파일은 해당 게시글 번호를 갖고 추가되어야 하기 때문에)
		int result = new BoardDao().insertBoard(conn, b);
		
		//첨부파일이 있는 경우에 등록하기
		int result2 = 1;
		
		if(at != null) {
			result2 = new BoardDao().insertAttachment(conn,at);	//요청시 0으로 되면 조건식 판별하기
		}
		
		
		if(result*result2 > 0) {	//성공 시 (두 DML 다 0이 아닌 경우 == result, result2가 둘 다 정상적인 결과값 반환)
			JDBCTemplate.commit(conn);
		}else {		//둘중 하나라도 0으로 돌아오면 실패(되돌리기)
			JDBCTemplate.rollback(conn);
		}
		
		
		return result * result2;
		
	}


	//첨부파일 조회 메소드
	public Attachment selectAttachment(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		Attachment at = new BoardDao().selectAttachment(conn, boardNo);
		
		//조회구문이니 트랜잭션 처리는 필요하지 않다.
		//자원반납 처리만 할 것
		JDBCTemplate.close(conn);
		
		
		//조회해온 첨부파일 정보
		return at;
		
	}


	//카테고리 목록 조회 메소드
	public ArrayList<Category> selectCategoryList() {

		
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Category> list = new BoardDao().selectCategoryList(conn);
		
		JDBCTemplate.close(conn);
		
		return list;
		
	}


	//게시글 수정 메소드
	public int updateBoard(Board b, Attachment at) {
		Connection conn = JDBCTemplate.getConnection();
		
		//게시글 정보 수정
		int result = new BoardDao().updateBoard(conn, b);
		
		
		//첨부파일이 있으면 처리 후 값이 담길 변수를 준비(없으면 게시글 처리만 하기 위해서 1로 초기화
		int result2 = 1;
		
		
		//첨부파일이 있다면 수정 또는 추가작업 수행
		if(at != null) {
			//기존 첨부파일이 있다면 (update) - fileNo가 있는지 확인
			if(at.getFileNo() != 0) {
				result2 = new BoardDao().updateAttachment(conn, at);
			}else {
			//기존에 첨부파일이 없었다면 (insert)
			//기존에 첨부파일 추가 메소드에서는 sql 구문이 refBno(참조게시글 번호) 부분이 currval로 들어가있어서 사용할 수 없다.
			//controller에서 가져온 boardNo를 넣어서 추가해야 한다.
				result2 = new BoardDao().insertNewAttachment(conn, at);
				
			}
		}
		
		
		//게시글 수정과 첨부파일 수정 또는 추가작업중 하나라도 잘못되었다면 되돌리기 작업 수행해야 한다
		
		if(result * result2 > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		
		return result * result2;
	}


	//사진게시판 게시글 등록 메소드
	public int insertPhotoBoard(Board b, ArrayList<Attachment> list) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		//사진게시판의 글(게시판)에 대한 처리
		int result = new BoardDao().insertPhotoBoard(conn, b);
		
		//사진게시판의 사진에 대한 처리(파일처리)	- Attachment 테이블
		int result2 = new BoardDao().insertAttachmentList(conn, list);
		
		
		//둘중 하나라도 실패하면 rollback
		//둘 다 성공해야 commit
		if(result * result2 > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
	
		return result * result2;
	
	}


	
	//사진게시글 목록 조회하기
	public ArrayList<Board> selectPhotoList() {
		
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Board> list = new BoardDao().selectPhotoList(conn);
		
		JDBCTemplate.close(conn);
		
		return list;
		
	}


	
	//사진 게시판 상세조회 메소드
	public ArrayList<Attachment> selectAttachmentList(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		//글이 하나가 쓰였어도 해당 글에 이미지가 여러개일수있기 때문에 list: 위에 정의된 selectAttachment() 사용 불가
		ArrayList<Attachment> list = new BoardDao().selectAttachmentList(conn, boardNo);
		
		JDBCTemplate.close(conn);
		
		return list;
	}


	//댓글 작성 메소드
	public int insertReply(Reply r) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().insertReply(conn, r);
		
		//DML (처리된 행 수)
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}


	//댓글 목록 조회하기 함수
	public ArrayList<Reply> selectReplyList(int bno) {
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Reply> rlist = new BoardDao().selectReplyList(conn, bno);
		
		JDBCTemplate.close(conn);
		
		return rlist;
		
		
	}
}
