package com.kh.member.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.member.model.vo.Member;

public class MemberDao {
	//쿼리구문 파일 xml 파일 읽어오는 작업
	
	private Properties prop = new Properties();
	
	//기본생성자가 생성되는 시점(메소드 호출 시점)에 파일 읽어오기
	public MemberDao() {
		
		//컴파일된 파일 기준으로 읽어올 xml파일 경로 찾기
		String filePath = MemberDao.class.getResource("/db/sql/member-mapper.xml").getPath();
		
		try {
			//찾은 경로를 넣고 해당 파일 읽어오기
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//로그인 기능
	public Member loginMember(Connection conn, String userId, String userPwd) {
		//Read (select)
		//준비물
		PreparedStatement pstmt = null;			//sql문 실행시켜줄 객체 변수
		ResultSet rset = null;					//조회결과 담을 객체 변수
		
		//회원정보를 담아갈 객체 준비하기
		Member m = null;
		
		
		//xml 파일에서 키값으로 sql 구문 가져오기
		String sql = prop.getProperty("loginMember");
		
		try {
			//미완성 sql 구문 전달하기
			pstmt = conn.prepareStatement(sql);
			
			//미완성부분 (위치홀더) 채우기
			pstmt.setString(1, userId);			//1번 ?에 userId 넣기
			pstmt.setString(2, userPwd);		//2번 ?에 userPwd 넣기
			
			
			//완성시켰으면 쿼리구문 실행해서 결과값 받아오기
			rset = pstmt.executeQuery();
			
			//조회된 결과는 한 행 또는 없음이므로(userId unique 제약조건) if문으로 조건 확인하기
			if(rset.next()) {	//조회된 결과가 있다면 회원정보 꺼내오기
				
				//회원정보 담을 vo(Member에 담아가기)
				m = new Member(rset.getInt("USER_NO")
							, rset.getString("USER_ID")
							, rset.getString("USER_PWD")
							, rset.getString("USER_NAME")
							, rset.getString("PHONE")
							, rset.getString("EMAIL")
							, rset.getString("ADDRESS")
							, rset.getString("INTEREST")
							, rset.getDate("ENROLL_DATE")
							, rset.getDate("MODIFY_DATE")
							, rset.getString("STATUS")
							);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			//생성의 역순으로 반납하기
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
						
		}
		
		return m;	//회원정보 담은 객체 반환하기
	}
	
	//insert~
	//회원가입 처리
	public int insertMember(Connection conn, Member m) {
		
		//회원가입 = DML(insert)
		PreparedStatement pstmt = null;		//위치홀더를 사용하면 편하니까 PreparedStatement로 준비하기
		
		//처리된 행 수 돌려받을 변수 준비하기
		int result = 0;
		
		//xml 파일에 작성된 sql구문 가져오기
		String sql = prop.getProperty("insertMember");
		
		try {
			
			//pstmt 생성하기(미완성 sql구문 전달하며)
			pstmt = conn.prepareStatement(sql);
			
			//각 위치홀더에 데이터 넣어주기
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getPhone());
			pstmt.setString(5, m.getEmail());
			pstmt.setString(6, m.getAddress());
			pstmt.setString(7, m.getInterest());
			
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


	//회원정보 수정 메소드
	public int updateMember(Connection conn, Member m) {
		
		//DML 구문 실행할 준비
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMember");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			//미완성 SQL구문 전달하며 pstmt 객체 생성
			pstmt.setString(1, m.getUserName());
			pstmt.setString(2, m.getPhone());
			pstmt.setString(3, m.getEmail());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getInterest());
			pstmt.setString(6, m.getUserId());
			
			//sql 구문 실행 및 결과 행 수 받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;		//처리된 행 수 돌려주기
		
	}


	//아이디로 회원정보 조회 메소드(정보갱신용)
	public Member selectMember(Connection conn, String userId) {
		
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		Member m = null;
		String sql = prop.getProperty("selectMember");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			//조회 결과가 있다면
			if(rset.next()) {
				m = new Member(rset.getInt("USER_NO")
							, rset.getString("USER_ID")
							, rset.getString("USER_PWD")
							, rset.getString("USER_NAME")
							, rset.getString("PHONE")
							, rset.getString("EMAIL")
							, rset.getString("ADDRESS")
							, rset.getString("INTEREST")
							, rset.getDate("ENROLL_DATE")
							, rset.getDate("MODIFY_DATE")
							, rset.getString("STATUS")
							);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return m;
	}


	
	public int deleteMember(Connection conn, int userNo, String userPwd) {
		
		//DML 구문 실행할 준비
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteMember");
		
		try {
			
			//delete/update: DML (처리된 행 수)
			pstmt = conn.prepareStatement(sql);
			
			//위치홀더에 알맞은 데이터 넣어주기
			pstmt.setInt(1, userNo);
			pstmt.setString(2, userPwd);
			
			//sql 구문 실행 및 결과 행 수 받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;		//처리된 행 수 돌려주기
	}


	//비밀번호 변경 작업하기
	public int updatePwd(Connection conn, int userNo, String userPwd, String updatePwd) {
		
		//DML(UPDATE)
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updatePwd");

		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, updatePwd);			//변경할 비밀번호
			pstmt.setString(2, userPwd);			//현재 비밀번호
			pstmt.setInt(3, userNo);				//회원 번호
			
			//실행결과 받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}


	//회원정보 조회 메소드(userNo)
	public Member selectMember2(Connection conn, int userNo) {
		
		//SELECT
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		Member m = null;
		
		String sql = prop.getProperty("selectMember2");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userNo);
			
			rset = pstmt.executeQuery();
			
			//조회결과가 있다면 (식별자로 조회하기 때문에 없거나 한 행 있음)
			if(rset.next()) {
				m = new Member(rset.getInt("USER_NO")
							, rset.getString("USER_ID")
							, rset.getString("USER_PWD")
							, rset.getString("USER_NAME")
							, rset.getString("PHONE")
							, rset.getString("EMAIL")
							, rset.getString("ADDRESS")
							, rset.getString("INTEREST")
							, rset.getDate("ENROLL_DATE")
							, rset.getDate("MODIFY_DATE")
							, rset.getString("STATUS")
							);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCTemplate.close(conn);
		}
		
		return m;
	}


	//아이디 중복확인
	public int checkId(Connection conn, String checkId) {
		
		//숫자를 세어 넣을 변수(아이디 있는지 없는지 판별용)
		int count = 0;
		
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("checkId");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, checkId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				count = rset.getInt("COUNT");
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		
		return count;
	}
	
}
