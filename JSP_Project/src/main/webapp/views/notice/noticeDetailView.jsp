<%@page import="com.kh.notice.model.vo.Notice" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% Notice n = (Notice)request.getAttribute("n"); %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style type="text/css">
        .outer {
            background: linear-gradient(to right, gold 0%, aqua 50%, pink 100%);
            color: black;
            width: 100%;
            height: 500px;
            margin: auto;
            margin-top: 50px;
        }
    </style>



</head>

<body>

    <%@ include file="../common/menubar.jsp" %>


        <div class="outer">
            <br>
            <h1 align="center">슬기들은 자세히 들어라</h1>
            
            <br><br>
            
            
            <table id="detail-area" align="center" border="1">
                <tr>
                    <th width="70">제목</th>
                    <td width="350" colspan="5"><%= n.getNoticeTitle() %></td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td><%= n.getNoticeWriter() %></td>
                    <th>작성일</th>
                    <td><%= n.getCreateDate() %></td>
                    <th>조회수</th>
                    <td><%= n.getCount() %></td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td colspan="5">
                        <p style="height: 150px;"><%= n.getNoticeContent() %></p>
                    </td>
                </tr>

            </table>

            <br><br>

			<!-- 로그인이 되어 있고 로그인된 유저의 아이디가 admin(관리자)일 경우에만 수정 / 삭제 버튼 보이기 -->
			<%if(loginUser != null && (loginUser.getUserId()).equals("admin")){ %>

            <div align="center">
                <a href="<%= contextPath %>/update.no?nno=<%=n.getNoticeNo() %>" class="btn btn-info">수정하기</a>
                <a href="<%= contextPath %>/delete.no?nno=<%=n.getNoticeNo() %>" class="btn btn-danger" onclick="return confirm('정말 삭제하시겠습니까?')">삭제하기</a>
            <%} %>
                <a href="<%= contextPath %>/list.no" class="btn btn-secondary">목록으로</a>
            </div>
        </div>
        
</body>

</html>