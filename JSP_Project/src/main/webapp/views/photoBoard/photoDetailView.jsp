<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
	.outer{
		background: red;
		background: -webkit-linear-gradient(left,red,orange,yellow,green,blue,indigo,violet); 
		background: -o-linear-gradient(left,red,orange,yellow,green,blue,indigo,violet); 
		background: -moz-linear-gradient(left,red,orange,yellow,green,blue,indigo,violet); 
		background: linear-gradient(to right, red,orange,yellow,green,blue,indigo,violet);
		
		color:white;
		width:100%;
		margin:auto;  /*가운데 정렬*/
		margin-top:50px; /*위로부터 50px만큼 여백*/
	}
	
	table{
		border: 3px solid white;
	}
		
</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	
	<div class="outer">
		<br>
		<h2 align="center">슬기사진 상세보기</h2>
		<br>
		
		<!-- enctype="multipart/form-data" 설정 따로 해주어야 함 안해주면 null값나옴 (그 처리는 BoardInsertController에서 -->
			<table align="center" border="1">
				<tr>
					<th width="100">제목</th>
					<td colspan="3">
						${b.boardTitle }
					</td>
				</tr>	
							
				<tr>
					<th>내용</th>					
					<td colspan="3">	
						<p style="height:70px">${b.boardContent }</p>
					</td>
				</tr>
				<tr>
					<th>대표이미지</th>
					<c:forEach items="${list }" var="at" varStatus="vs">
						<c:choose>
							<c:when test="${vs.index eq 0}">
								<%-- 첫번째 요소니까 대표이미지 --%>
								<td colspan="3" align="center">
									<img id="titleImg" alt="" src="${contextPath }${at.filePath}${at.changeName}" width="250" height="170">
								</td>
							</tr>
							</c:when>
							<c:otherwise>
								<%-- 상세이미지 --%>
								<tr>
									<th>상세 이미지</th>
									<td>
										<img id="contentImg${vs.count }" alt="" src="${contextPath }${at.filePath}${at.changeName}" width="250" height="170">
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:forEach>
			</table>

		<br><br>
	</div>
</body>
</html>