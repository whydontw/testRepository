package com.kh.notice.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.member.model.vo.Member;
import com.kh.notice.model.vo.Notice;

public class NoticeDao {
	
	
	//기본 생성자에 mapper파일 읽어오는 작업 처리해두기
	private Properties prop = new Properties();
	
	public NoticeDao() {
		String filePath = NoticeDao.class.getResource("/db/sql/notice-mapper.xml").getPath();
		
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	//공지사항 목록 조회 메소드
	public ArrayList<Notice> selectNoticeList(Connection conn) {
		//준비물 (SELECT)
		
		ResultSet rset = null;		//조회결과집합 받을 객체 변수
		Statement stmt = null;		//이미 완성된 sql문을 전달할 거라서 PreparedStatment 사용할 필요가 없음
		
		//목록조회 결과가 여러행이 나올 수 있기 때문에 ArrayList에 담아가기
		ArrayList<Notice> list = new ArrayList<Notice>();

		String sql = prop.getProperty("selectNoticeList");
		
		try {
			
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			while(rset.next()) {	//조회된 다음 행이 있을 때
				//조회된 행 정보를 추출하여 Notice 객체에 담아주고 그 객체를 list에 추가하면 된다.
				list.add( new Notice(rset.getInt("NOTICE_NO")
									, rset.getString("NOTICE_TITLE")
									, rset.getString("USER_NAME")
									, rset.getInt("COUNT")
									, rset.getDate("CREATE_DATE")));
			}
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		return list;
		
	}



	
	
	//공지사항 게시글 상세조회
	public Notice selectNotice(Connection conn, int noticeNo) {
		
		//detail select
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		
		Notice n = null;
		
		String sql = prop.getProperty("selectNotice");
		
		
		try {

			pstmt = conn.prepareStatement(sql);
			
			//위치 홀더에 데이터 넣어서 완성 sql 만들기
			pstmt.setInt(1, noticeNo);			//1번 ?에 noticeNo 넣기
			
			//완성된 sql 구문 실행하기(결과집합 rset에 받기)
			rset = pstmt.executeQuery();
			
			
			//글번호(식별자)로 조회하기 때문에 한 행 또는 0행이 나올 수밖에 없다.
			if(rset.next()) {
				
				//회원정보 담을 vo(Member에 담아가기)
				n = new Notice(
							//rset 객체에서 하나씩 꺼내오기~
							rset.getInt("NOTICE_NO")
							, rset.getString("NOTICE_TITLE")
							, rset.getString("NOTICE_CONTENT")
							, rset.getString("USER_NAME")
							, rset.getInt("COUNT")
							, rset.getDate("CREATE_DATE")
							);
			}
			
		
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		//공지글 상세 리턴하기
		return n;
	}

	

	public int increaseCount(Connection conn, int noticeNo) {
		
		
		int result = 0;		//처리된 행 수 담을 변수
		PreparedStatement pstmt = null;	//sql구문 처리 및 결과 돌려받을 함수
		String sql = prop.getProperty("increaseCount");
		
		try {
			
			//미완성 sql구문 전달하며 pstmt 객체 생성하기
			pstmt = conn.prepareStatement(sql);
			
			//미완성 sql구문 완성시키기: ? 위치홀더 채워주기
			//위치홀더에 noticeNo 넣어주기(글 번호 조회할 수 있도록)
			pstmt.setInt(1, noticeNo);
			
			//완성된 sql 문을 실행 및 결과받기. 결과는 처리된 행수로 돌아오기 때문에 int result로 변수에 담기.
			result = pstmt.executeUpdate();	//DML구문은 excuteUpdate() 메소드 사용
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			//자원반납
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	
	//공지사항 작성하기
	public int insertNotice(Connection conn, Notice n) {
		//회원가입 = DML(insert)
		
		PreparedStatement pstmt = null;		//위치홀더를 사용하면 편하니까 PreparedStatement로 준비하기
		
		//처리된 행 수 돌려받을 변수 준비하기
		int result = 0;
		
		//xml 파일에 작성된 sql구문 가져오기
		String sql = prop.getProperty("insertNotice");
		
		try {
			
			//pstmt 생성하기(미완성 sql구문 전달하며)
			pstmt = conn.prepareStatement(sql);
			
			//각 위치홀더에 데이터 넣어주기
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContent());
			
			//noticeWriter는 vo에 String으로 정의했기 때문에 넣을 때 변환하기
			pstmt.setInt(3, Integer.parseInt(n.getNoticeWriter()));
			
			//sql구문 실행 후 DML 구문이니 처리된 행 수 돌려받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//자원 반납하기
			JDBCTemplate.close(pstmt);
		}
	
		return result;
		
	}


	public int updateNotice(Connection conn, Notice n) {
		
		PreparedStatement pstmt = null;
		
		int result = 0;
		
		String sql = prop.getProperty("updateNotice");
		
		try {
			
			//pstmt 생성하기(미완성 sql구문 전달하며)
			pstmt = conn.prepareStatement(sql);
			
			//각 위치홀더에 데이터 넣어주기
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContent());
			pstmt.setInt(3, n.getNoticeNo());
			
			//sql구문 실행 후 DML 구문이니 처리된 행 수 돌려받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//자원 반납하기
			JDBCTemplate.close(pstmt);
		}
	
		return result;
		
	}


	//공지사항 삭제하기
	public int deleteNotice(Connection conn, String noticeNo) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("deleteNotice");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(noticeNo));
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
}
