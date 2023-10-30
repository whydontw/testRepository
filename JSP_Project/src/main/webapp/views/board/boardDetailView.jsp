<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.outer table{border-color:magenta;}
</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	
	<div class="outer">
		<br>
		<h2 align="center">게시글 상세 보기</h2>
		<br>
		<table id="detail-area" align="center" border="1">
			<tr>
				<th width="70">카테고리</th>
				<td width="70">${b.category }</td>
				<th width="70">제목</th>
				<td width="350">${b.boardTitle }</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td>${b.boardWriter }</td>
				<th>작성일</th>
				<td>${b.createDate }</td>
			</tr>
			<tr>
				<th>내용</th>
				<td colspan="3">
					<p style="height:200px; white-space:pre;">${b.boardContent }</p>
						
				</td>
			</tr>
			 <tr>
               	<th>첨부파일</th>
               	<td colspan="3">
               		<c:choose>
               			<c:when test="${empty at}">
               				첨부파일 없다
               			</c:when>
               			<c:otherwise>
		               		<a download="${at.originName}" href="${contextPath }${at.filePath}${at.changeName}">${at.originName}</a>
               			</c:otherwise>
               		</c:choose>
               	</td>
             </tr>
		</table>
		<br>
		<div align="center">
			<button onclick="">목록가기</button>
			<!--현재 로그인된 유저의 아이디가 글 작성자와 동일하다면  -->
		    <button onclick="location.href='${contextPath}/update.bo?bno=${b.boardNo }'">수정하기</button>
		    <button onclick="location.href='${contextPath}/delete.bo?bno=${b.boardNo }'">삭제하기</button>
		</div>
		<br>
		
		
		
		<!-- 비동기 통신을 이용하여 댓글 목록 뿌려주기, 댓글 작성 부분 요청하기 -->
		<c:if test="${not empty loginUser}">
			<div id="reply-area">
				<table border="1" align="center">
					<thead>
						<tr>
							<th>댓글작성</th>
							<td>
								<textarea id="replyContent"rows="3" cols="50" style="resize:none"></textarea>
							</td>
							<td><button onclick="insertReply()">댓글 작성</button></td>
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
			</div>
		</c:if>
		
		<script type="text/javascript">
		
		
			$(function(){
				//DOM 요소가 모두 load되었을 시점에 함수가 호출되는 형식
				selectReplyList();
			})
			
		
			function selectReplyList(){	//댓글목록 조회 함수
				//목록 조회해와서 tbody에 tr 만들어 넣기
				//mapping 주소: reply.bo
				
				$.ajax({
					url: "replyList.bo",
					data: { bno: ${b.boardNo} },
					type: "get",
					success: function(result){
						
	                    for(var i in result){
	                    	var str = str + "<tr><td>" + result[i].replyWriter + "</td><td>" + result[i].replyContent
			                        + "</td><td>" + result[i].createDate +"</td></tr>";
	                    };
	                    
	                    $("#reply-area table tbody").html(str);
	                    
					},
					error: function(){
						console.log("통신 오류");
					}
					
				})
			}
		
		
			//댓글 작성 (insert)
			function insertReply(){
				$.ajax({
					url: "insertReply.bo",
					data: { 
						bno : ${b.boardNo},
						content: $("#replyContent").val()
					},
					type: "post",
					success: function(result){	//성공시
						console.log(result);
						
						if(result > 0){	//댓글입력 성공
							
							alert("댓글 작성 성공");
							
							//추가된 댓글목록 재조회하기
 							selectReplyList();

							$("#replyContent").val("");
							
						
						}else{	//댓글 입력실패
							alert("댓글 작성 실패");
						}
					
					},
					error: //실패시
					function(){
						console.log("통신 오류");
					}
				})
			}
		</script>
		
		<br><br>
	</div>
</body>
</html>