<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.list-area {
	
	background: red;
	background: -webkit-linear-gradient(left,red,orange,yellow,green,blue,indigo,violet); 
	background: -o-linear-gradient(left,red,orange,yellow,green,blue,indigo,violet); 
	background: -moz-linear-gradient(left,red,orange,yellow,green,blue,indigo,violet); 
	background: linear-gradient(to right, red,orange,yellow,green,blue,indigo,violet);

	color: white;
	
	width: 95%;
	margin: auto;
}

.thumbnail {
	border: 1px solid white;
	width: 220px;
	display: inline-block;
	margin: 15px;
}

.thumbnail:hover {
	cursor: pointer;
	opacity: 0.7;
}
</style>
</head>
<body>
	<%@ include file="../common/menubar.jsp"%>
	<div class="outer" align="center">
		<h1>정슬기 포토존</h1>
		<br>
		<c:if test="${loginUser != null }">
			<button onclick="location.href='${contextPath}/insert.ph'">글 작성하기</button>
		</c:if>
		
	</div>

		<div class="list-area">
			<%-- 게시글 없는 경우 --%>
			<c:choose>
				<c:when test="${empty phList }">
					슬기사진이 떨어졌음.
				</c:when>
			
				<c:otherwise>
					<c:forEach items="${phList }" var="ph">
						<div class="thumbnail">
							<input type="hidden" value="${ph.boardNo }">
							<img src="${contextPath}${ph.imgsrc}" alt="" width="200px" height="150px">
							<p>
								글번호: ${ph.boardNo } ${ph.boardTitle } <br> 조회수: ${ph.count }
							</p>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		
		<script>
        	//글 클릭했을때 글번호를 detail.bo 로 전달하며 페이지 요청하기
        	$(function(){
        		
        		$(".thumbnail").click(function(){
        			//$(this).children().eq(0).text() : 글번호 추출
        			location.href="detail.ph?bno="+ $(this).children().eq(0).val();
        		});
        		
        	});
        </script>
		
</body>
</html>