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

#update-area {
	border: 1px solid white;
}

#update-area input, #update-area textarea {
	width: 100%;
	box-sizing: border-box;
}
</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp"%>

	<div class="outer">
		<br>
		<h2 align="center">게시글 수정</h2>
		<br>
		<!-- enctype: encoding type -->
		<form action="${contextPath}/update.bo" method="post" id="update-area" enctype="multipart/form-data">
			<%-- 어떠한 게시글을 수정할지 알아야하기 때문에 번호 보내기+
		1 --%>
			<input type="hidden" name="boardNo" value="${b.boardNo }">
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
					
					<script type="text/javascript">
						$(function(){
							//비교대상: Board 객체에 담겨 있는 category(카테고리 이름)
							//select option 목록에서 똑같은 카테고리 이름인 요소를 선택하기
							$("#update-area option").each(function(){	//각각 요소접근하기 each
								
								var choose = "${b.category}";
								
								console.log("=================");
								console.log(choose);
								console.log($(this).text());	//각 요소의 텍스트
								console.log("=================");
								
								//데이터베이스에서 조회해온 게시글의 카테고리와 카테고리 목록중 텍스트가 같은 요소
								if($(this).text() == choose){	//비교대상이 같다면
									$(this).attr("selected", true);	//해당 요소객체의 selected 속성을 true 값으로 변경하기
									//선택되어짐
									return false; 		//== jquery 반복문의 break 역할
								}
								
							})
						
							//각 옵션요소에 순차적으로 접근해서 내가 가지고 있는 값과 비교하기
						});
					</script>
					
					<th width="70">제목</th>
					<td width="350"><input type="text" name="title"
						value="${b.boardTitle }" id=""></td>
				</tr>
				<tr>
					<th>작성자</th>
					<td></td>
					<th>작성일</th>
					<td></td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="3"><textarea name="content" cols="30" rows="10"
							required style="resize: none;">${b.boardContent }</textarea></td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td colspan="3">
						<c:if test="${at != null}">
							<%-- 첨부파일이 있다면 해당 정보를 보여줘야 한다. --%>
							${at.originName }
							<%-- 원본파일의 파일번호와 수정명을 서버에 전달하기. --%>
							<input type="hidden" name="originFileNo" value="${at.fileNo }">
							<input type="hidden" name="originFileName" value="${at.changeName }">
						</c:if>
						<input type="file" name="reUploadFile">
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