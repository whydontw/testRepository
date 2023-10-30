<%@page import="java.util.ArrayList, com.kh.notice.model.vo.Notice"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//전달받은 공지사항 목록 추출하기
	ArrayList<Notice> nlist = (ArrayList<Notice>)request.getAttribute("nlist");	//반환타입이 object이므로 ArrayList 형변환 맞춰줘야함

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    .outer{
        background: linear-gradient(to right, gold 0%, aqua 50%, pink 100%);
        color: black;
        width: 100%;
        height: 500px;
        margin: auto;
        margin-top: 50px;
    }
    .list-area{
        border: 1px solid white;
        text-align: center;
    }
    .list-area>tbody tr:hover{
        background-color: red;
    	color: #fff;
        text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 40px #ff0080, 0 0 55px #ff0080, 0 0 75px #ff0080;
    	cursor: pointer;
    }
</style>
</head>
<body>
    <%@ include file="../common/menubar.jsp" %>
    <div class="outer">
		<br>    
        <h1 align="center">슬기녀석들에게 알린다</h1>
        <div align="center">
        
        <!-- ＊ 조건의 순서를 바꾸면 NullpointException -->
        <%if(loginUser != null && (loginUser.getUserId()).equals("admin")){ %>
        	<!-- 글 작성 페이지로 이동 -->
            <a href="<%= contextPath %>/insert.no" class="btn btn-info">글 작성</a>
        <%} %>
        </div>

        <br><br>

        <table class="list-area" align="center">
            <thead>
                <tr>
                    <th>글 번호</th>
                    <th width="400">글 제목</th>
                    <th width="100">작성자</th>
                    <th>조회수</th>
                    <th width="100">작성일</th>
                </tr>
            </thead>
            <tbody>
			    
                <% if(nlist.isEmpty()) {%>
	                <!-- list가 비어있는 경우 -->
	                <!-- list비어있는 경우 : isEmpty() 사용하기 -->
                	<tr>
                		<td colspan="5">공지사항이 없습니다.</td>
                	<tr>
                <%}else{ %>
					<% for(Notice n : nlist){ %>
						<tr>
		                    <td><%= n.getNoticeNo() %></td>
		                    <td><%= n.getNoticeTitle() %></td>
		                    <td><%= n.getNoticeWriter() %></td>
		                    <td><%= n.getCount() %></td>
		                    <td><%= n.getCreateDate() %></td>
		                </tr>
					<% } %>

	                <!-- 목록이 존재하는 경우 -->
                <%} %>
                
            </tbody>
        </table>
    </div>
    
    <script type="text/javascript">
    	//detail.no: 요청매핑값
    	//클릭한 글번호를 detail.no?nno=글번호로 글을 클릭햇을때 요청 보내기(페이지이동)
    	
    	$(function(){
    		$(".list-area>tbody>tr").click(function(){
    			
    			//console.log("클릭됨");
    			//현재 클릭된 요소객체: $(this): <tr></tr>
    			//우리가 원하는 것 tr 안에 있는 td 중에 글 번호 td의 텍스트
				console.log($(this).children().eq(0).text());
				console.log($(this).children().first().text());

				//※this를 쓰지 않으면 내가 선택한 요소가 아니라 코드에서 가장 처음에 찾은 tr의 요소에 접근한다. 
 				//console.log($(".list-area>tbody>tr").children().first().text());
				
				
				var nno = $(this).children().eq(0).text();		//글번호 가져오기
				
				
				//요청할 url?키=밸류&키=밸류 ...
				//물음표 뒤 내용들을 쿼리스트링이라고 한다. 직접 작성하여 요청해도 get 방식 요청이 됨
				// /jsp/detail.no?nno=클릭한글번호
				location.href="<%=contextPath%>/detail.no?nno=" + nno;
    			
    		});
    	})
    </script>
    
</body>
</html>