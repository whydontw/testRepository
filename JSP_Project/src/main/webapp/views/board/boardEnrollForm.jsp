<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.outer table {
	border-color: black;
}
#enroll-area{
	border: 1px solid white;
}
#enroll-area input, #enroll-area textarea {
	width: 100%;
	box-sizing: border-box;
}
</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp"%>

	<div class="outer">
		<br>
		<h2 align="center">게시글 작성</h2>
		<br>
		<!-- enctype: encoding type -->
		<form action="${contextPath}/insert.bo" method="post" id="enroll-area" enctype="multipart/form-data">
			<table align="center" border="1">
				<tr>
					<th width="70">카테고리</th>
					<td width="70">
						<select name="category" id="">
						<%-- 반복문을 이용해서 카테고리 옵션 태그 뽑아주기 --%>
						<%-- <c:forEach items="반복할 아이템(꺼내오는 것)" var="접근한 대상의 이름(직접 정의)"> --%>
							<c:forEach items="${category}" var="c">
		                		<option value="${c.categoryNo}">${c.categoryName}</option>
	                		</c:forEach>
						</select>
					</td>
					<th width="70">제목</th>
					<td width="350"><input type="text" name="title" id=""></td>
				</tr>
				<tr>
					<th>작성자</th>
					<td> </td>
					<th>작성일</th>
					<td> </td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="3">
						<textarea name="content" cols="30" rows="10" required style="resize: none;"></textarea>
					</td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td colspan="3">
						<input type="file" name="uploadFile" id="">
					</td>
				</tr>
			</table>

			<div align="center">
				<button type="submit">글 작성</button>
				<button type="reset">취소</button>
			</div>
		</form>

		<br>

	</div>
</body>
</html>