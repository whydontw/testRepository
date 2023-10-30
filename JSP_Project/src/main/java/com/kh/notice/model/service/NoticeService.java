package com.kh.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.dao.NoticeDao;
import com.kh.notice.model.vo.Notice;

public class NoticeService {

	
	//공지사항 목록 조회 메소드
	public ArrayList<Notice> selectNoticeList() {
		//Connection 만들기
		Connection conn = JDBCTemplate.getConnection();
		
		//DAO에 conn 전달하면서 요청 보내기(반환받은 list 담아주기)
		ArrayList<Notice> list = new NoticeDao().selectNoticeList(conn);
		
		
		//조회구문은 트랜잭션 처리할 필요가 없기 때문에 자원반납만 해주기
		JDBCTemplate.close(conn);
		
		return list;	//DAO에게 받은 list 반환하기(Controller에게)
		
	}

	
	//공지사항 상세보기
	public Notice selectNotice(int noticeNo) {
		Connection conn = JDBCTemplate.getConnection();
		
		Notice notice = new NoticeDao().selectNotice(conn, noticeNo);
		
		JDBCTemplate.close(conn);
		
		return notice;
		
	}

	
	//상세보기 전 조회수 증가 메소드
	public int increaseCount(int noticeNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().increaseCount(conn, noticeNo);
		
		//DML 구문이니 트랜잭션 처리하기
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		
		return result;
	}

	
	//공지글 작성 메소드
	public int insertNotice(Notice n) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().insertNotice(conn, n);
		
		//dml(insert) 트랜잭션
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
		
	}

	
	//공지글 수정 메소드
	public int updateNotice(Notice n) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().updateNotice(conn, n);
		
		//트랜잭션 처리
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}


	//공지 삭제 메소드
	public int deleteNotice(String noticeNo) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().deleteNotice(conn, noticeNo);
		
		//dml 트랜잭션
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
}
