<%@page import="com.kh.notice.model.vo.Notice"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//수정 전 글을 담은 객체
	Notice n = (Notice)(request.getAttribute("n"));
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
    #update-form > table {
        border: 1px solid white
    }
    #update-form input, #update-form textarea{
        width: 100%;
        box-sizing: border-box;
    }
    .outer{
        background: linear-gradient(to right, gold 0%, aqua 50%, pink 100%);
        color: black;
        width: 100%;
        height: 500px;
        margin: auto;
        margin-top: 50px;
    }
</style>
</head>

<!-- 공지사항 수정하기 form -->
<body>
	<%@ include file="../common/menubar.jsp" %>
    
    <div class="outer">
        <br>
        <h2 align="center">슬기들을 위한 공지사항 수정하기</h2>

        <form action="<%= contextPath %>/update.no" id="update-form" method="post">
        	<input type="hidden" name="noticeNo" value="<%= n.getNoticeNo() %>">
            <table align="center">
                <tr>
                    <td width="50">* 제목</td>
                    <td width="350"><input type="text" name="title" value="<%= n.getNoticeTitle() %>" required></td>
                </tr>
                <tr>
                    <td>* 내용</td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <textarea name="content" id="" cols="30" rows="10" style="resize: none;"><%= n.getNoticeContent() %></textarea>
                    </td>
                </tr>
            </table>

            <br><br>

            <div align="center">
                <button type="submit">수정하기</button>
                <button type="button" onclick="history.back();">뒤로가기</button>
            </div>
        </form>
    </div>
    
</body>
</html>