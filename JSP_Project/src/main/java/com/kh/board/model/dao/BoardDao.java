package com.kh.board.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DrbgParameters.Reseed;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.crypto.spec.PSource;

import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Reply;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.Attachment;
import com.kh.common.model.vo.Category;
import com.kh.common.model.vo.PageInfo;

public class BoardDao {

	private Properties prop = new Properties();

	// 매퍼파일 읽어오는 작업 (기본생성자에 추가)
	// DAO로 메소드를 호출하는 시점에서 파일이 읽힌다.
	public BoardDao() {

		try {
			String filePath = BoardDao.class.getResource("/db/sql/board-mapper.xml").getPath();
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 게시글 개수 조회 메소드
	public int listCount(Connection conn) {
		// SELECT (조회)
		int count = 0;
		ResultSet rset = null; // 조회구문이기때문에 필요
		Statement stmt = null; // 위치홀더 필요없으니 statement활용

		String sql = prop.getProperty("listCount");

		try {
			stmt = conn.createStatement();

			// 개수 조회
			rset = stmt.executeQuery(sql);

			if (rset.next()) {
				// 조회된 게시글 개수
				count = rset.getInt("COUNT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		return count;

	}

	// 게시글 목록 조회메소드
	public ArrayList<Board> selectList(Connection conn, PageInfo pi) {

		// 준비물
		ArrayList<Board> list = new ArrayList<>();
		ResultSet rset = null;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("selectList");
		// 1페이지 : 1~10 / 5페이지 : 41~50 / 10페이지 91~100
		// 2페이지 : 11~20
		int startRow = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;
		int endRow = pi.getCurrentPage() * pi.getBoardLimit();

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);

			rset = pstmt.executeQuery();

			while (rset.next()) {
				list.add(new Board(rset.getInt("BOARD_NO"), rset.getString("CATEGORY_NAME"),
						rset.getString("BOARD_TITLE"), rset.getString("USER_ID"), rset.getInt("COUNT"),
						rset.getDate("CREATE_DATE")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return list;
	}

	// 조회수 증가 메소드
	public int increaseCount(int boardNo, Connection conn) {

		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("increaseCount");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, boardNo);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 게시글 상세 조회
	public Board selectBoard(Connection conn, int boardNo) {

		Board b = null;
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectBoard");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				b = new Board(rset.getInt("BOARD_NO"), rset.getString("CATEGORY_NAME"), rset.getString("BOARD_TITLE"),
						rset.getString("BOARD_CONTENT"), rset.getString("USER_ID"), rset.getDate("CREATE_DATE"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return b;
	}

	// 게시글 등록 메소드
	public int insertBoard(Connection conn, Board b) {
		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertBoard");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, Integer.parseInt(b.getCategory()));
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setInt(4, Integer.parseInt(b.getBoardWriter()));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 첨부파일 등록 메소드
	public int insertAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("insertAttachment");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 첨부파일 조회하기
	public Attachment selectAttachment(Connection conn, int boardNo) {

		// SELECT 조회하기
		ResultSet rset = null; // 결과받을 객체 변수
		PreparedStatement pstmt = null; // 위치홀더를 이용해야 하니 preparedstatement 이용하기

		// 첨부파일 정보 담을 객체 변수 준비하기. 아직 있을지 없을지 모르기 때문에 null로 초기화하기
		Attachment at = null;

		String sql = prop.getProperty("selectAttachment");

		try {

			// 미완성된 sql 구문을 전달하며 pstmt 객체 생성하기
			pstmt = conn.prepareStatement(sql);

			// 미완성된 부분(위치홀더) 값 채워주기
			pstmt.setInt(1, boardNo);

			// sql 구문 완성했으니 실행 및 처리결과 받기
			rset = pstmt.executeQuery();

			// 전달받은 처리결과(행집합)이 있다면
			if (rset.next()) {
				// 조회된 데이터를 객체에 담아서 컨트롤러까지 전달해야 함
				at = new Attachment( // 조회되 데이터가 있을 때 객체 생성하기
						rset.getInt("FILE_NO"), rset.getString("ORIGIN_NAME"), rset.getString("CHANGE_NAME"),
						rset.getString("FILE_PATH"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return at;

	}

	// category 목록 뽑는 메소드
	public ArrayList<Category> selectCategoryList(Connection conn) {
		// 데이터베이스에서 카테고리 테이블에 있는 정보 목록을 조회해서 담아가기(서비스로 전달 → 컨트롤러에 전달)
		// 카테고리 번호, 카테고리 이름: VO (Category)
		ArrayList<Category> list = new ArrayList<Category>();
		Category category = null;

		// SELECT 조회하기
		Statement stmt = null;

		String sql = prop.getProperty("selectCategoryList");

		ResultSet rset = null;

		try {

			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);

			while (rset.next()) {
				category = new Category(rset.getInt("CATEGORY_NO"), rset.getString("CATEGORY_NAME"));
				list.add(category);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset); // 생성역순 반납
			JDBCTemplate.close(stmt);
		}

		return list;

	}

	// 게시글 수정 메소드
	public int updateBoard(Connection conn, Board b) {
		// DML (update)
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updateBoard");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, b.getBoardTitle());
			pstmt.setString(2, b.getBoardContent());
			pstmt.setInt(3, Integer.parseInt(b.getCategory()));
			pstmt.setInt(4, b.getBoardNo());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;

	}

	// 첨부파일 수정 메소드
	public int updateAttachment(Connection conn, Attachment at) {
		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updateAttachment");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getFileNo());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 기존 게시글에 첨부파일 추가하는 메소드
	public int insertNewAttachment(Connection conn, Attachment at) {

		// DML(insert)
		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("insertNewAttachment");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, at.getRefBno());
			pstmt.setString(2, at.getOriginName());
			pstmt.setString(3, at.getChangeName());
			pstmt.setString(4, at.getFilePath());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 사진게시판 >게시글< 등록 메소드
	public int insertPhotoBoard(Connection conn, Board b) {
		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertPhotoBoard");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, b.getBoardTitle());
			pstmt.setString(2, b.getBoardContent());
			pstmt.setInt(3, Integer.parseInt(b.getBoardWriter()));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 사진게시판 사진 목록 추가 메소드
	public int insertAttachmentList(Connection conn, ArrayList<Attachment> list) {

		// 실행 후 받은 결과를 처리할 변수(여러개를 처리하기 위해서 1로 초기화해두기)
		int result = 1;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("insertAttachmentList");

		try {

			// list에 담겨져 있는 각 Attachment를 꺼내서 각 데이터로 추가 구문 작성하기
			// 순차적으로 전부 접근하여 추출할 수 있도록 향상된 for문 사용
			for (Attachment at : list) {
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, at.getOriginName());
				pstmt.setString(2, at.getChangeName());
				pstmt.setString(3, at.getFilePath());
				pstmt.setInt(4, at.getFileLevel());

				// 실행 후 받은 결과가 하나라도 0이 나오면 결과값을 0으로 만들기
				result = result * pstmt.executeUpdate();
			}

		} catch (SQLException e) {

			// try 구문에서 첫번째 처리에 실패하게 된다면 result가 1로 전달되는 것을 방지하기
			result = 0;

			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 사진게시판 게시글 목록 조회 메소드
	public ArrayList<Board> selectPhotoList(Connection conn) {
		// SELECT
		ResultSet rset = null;
		Statement stmt = null;

		// 게시글 목록을 조회해야하니까 리스트 준비하기
		ArrayList<Board> list = new ArrayList<Board>();

		String sql = prop.getProperty("selectPhotoList");

		try {

			stmt = conn.createStatement();

			rset = stmt.executeQuery(sql);

			while (rset.next()) {
				list.add(new Board(rset.getInt("BOARD_NO"), rset.getString("BOARD_TITLE"), rset.getInt("COUNT"),
						rset.getString("TITLEIMG")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(stmt);
		}

		return list;
	}

	// 사진게시글 상세조회 메소드
	public ArrayList<Attachment> selectAttachmentList(Connection conn, int boardNo) {
		ArrayList<Attachment> list = new ArrayList<Attachment>();

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectAttachment");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, boardNo);

			rset = pstmt.executeQuery();

			while (rset.next()) {
				list.add(new Attachment(rset.getInt("FILE_NO"), rset.getString("ORIGIN_NAME"),
						rset.getString("CHANGE_NAME"), rset.getString("FILE_PATH")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return list;
	}

	// 댓글 작성 메소드
	public int insertReply(Connection conn, Reply r) {
		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertReply");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, r.getReplyContent());
			pstmt.setInt(2, r.getRefBno());
			pstmt.setInt(3, Integer.parseInt(r.getReplyWriter()));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	
	//댓글 목록 조회 메소드
	public ArrayList<Reply> selectReplyList(Connection conn, int bno) {

		ResultSet rset = null;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("selectReplyList");

		ArrayList<Reply> rlist = new ArrayList<Reply>();

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bno);

			rset = pstmt.executeQuery();

			while (rset.next()) {
				rlist.add(new Reply(rset.getInt("REPLY_NO"),
									rset.getString("REPLY_CONTENT"),
									rset.getString("USER_ID"),
									rset.getDate("CREATE_DATE")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
			JDBCTemplate.close(rset);
		}
		
		return rlist;

	}

}
