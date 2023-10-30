<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
    #enroll-form > table {
        border: 1px solid white
    }
    #enroll-form input, #enroll-form textarea{
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

<body>
	<%@ include file="../common/menubar.jsp" %>
    
    <div class="outer">
        <br>
        <h2 align="center">슬기들을 위한 공지사항 작성하기</h2>

        <form action="<%= contextPath %>/insert.no" id="enroll-form" method="post">
			<input type="hidden" name="userNo" value="<%= loginUser.getUserNo() %>">
        	<%-- userNo는 menubar에서 getAttribute()로 꺼내놓았기 때문에 여기서도 활용이 가능하다! --%>
            <table align="center">
                <tr>
                    <td width="50">* 제목</td>
                    <td width="350"><input type="text" name="title" required></td>
                </tr>
                <tr>
                    <td>* 내용</td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <textarea name="content" id="" cols="30" rows="10" style="resize: none;"></textarea>
                    </td>
                </tr>
            </table>

            <br><br>

            <div align="center">
                <button type="submit">등록하기</button>
                <button type="button" onclick="history.back();">뒤로가기</button>
            </div>
            <!-- history.back(): 뒤로가기 (이전페이지) -->
        </form>
    </div>
    
</body>
</html>