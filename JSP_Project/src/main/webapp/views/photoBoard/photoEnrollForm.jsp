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
	
	#enroll-area>table{
		border: 1px solid white;
	}
	
	#enroll-area input,#enroll-area textarea{
		width : 100%;
		box-sizing : border-box;
	}
	
</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	
	<div class="outer">
		<br>
		<h2 align="center">사진 게시글 작성</h2>
		<br>
		
		<!-- enctype="multipart/form-data" 설정 따로 해주어야 함 안해주면 null값나옴 (그 처리는 BoardInsertController에서 -->
		<form action="${contextPath }/insert.ph" method="post" id="enroll-area" enctype="multipart/form-data">
			<input type="hidden" name="userNo" value=${loginUser.userNo }>
			<table align="center" border="1">
				<tr>
					<th width="100">제목</th>
					<td colspan="3">
						<input type="text" name="title" required>
					</td>
				</tr>	
							
				<tr>
					<th>내용</th>					
					<td colspan="3">	
						<textarea name="content" rows="10" style="resize:none" required></textarea>
											
					</td>
				</tr>
				<tr>
					<th>대표이미지</th>
					<td colspan="3" align="center">
						<img id="titleImg" width="250" height="170">
					
					</td>
				</tr>
				<tr>
					<th>상세이미지</th>
					<td> <img id="contentImg1" width="150" height="120"> </td>
					<td> <img id="contentImg2" width="150" height="120"> </td>
					<td> <img id="contentImg3" width="150" height="120"> </td>
				</tr>
			</table>
			
			<div id="file-area">
				<!-- onchange: 변화가 일어났을 때 발생하는 이벤트 -->
				<!--
					선언적 함수를 내부에 작성할 때 해당 이벤트가 발생한 시점에 요소 객체를 전달하는 방법
					함수(this)
				-->
				<input type="file" id="file1" name="file1" onchange="loadImg(this, 1)" required> <!-- 대표이미지 필수  -->
				<input type="file" id="file2" name="file2" onchange="loadImg(this, 2)">
				<input type="file" id="file3" name="file3" onchange="loadImg(this, 3)">
				<input type="file" id="file4" name="file4" onchange="loadImg(this, 4)">
			</div>
					
			<div align="center">
				<button type="submit">글작성</button>
			</div>
		</form>
		
		<script>
			$(function(){
				$("#file-area").hide();		//file input 숨기기

				//대표이미지를 클릭하면 input file 요소 1번이 클릭되게 하는 함수
				$("#titleImg").click(function(){
					$("#file1").click();
				});
				$("#contentImg1").click(function(){
					$("#file2").click();
				});
				$("#contentImg2").click(function(){
					$("#file3").click();
				});
				$("#contentImg3").click(function(){
					$("#file4").click();
				});
			});


			//이미지를 읽어줄 함수
			function loadImg(inputFile, num){
				console.log(inputFile.files);
				//inputFile.files: 파일업로드 정보를 배열의 형태로 반환해주는 속성
				//파일을 선택하면 files 속성의 length가 1이 반환됨
				if(inputFile.files.length == 1){	//파일이 등록되면
					//해당 파일을 읽어줄 FileReader라고 하는 자바스크립트 객체를 이용한다.
					var reader = new FileReader();

					//파일을 읽어줄 메소드: reader.readAsDataURL(파일) - 반환타입 void
					//해당 파일을 읽어서 고유한 url을 부여해주는 메소드
					//반환받은 url을 미리보기 화면에 넣어주면 된다.
					reader.readAsDataURL(inputFile.files[0])


					//해당 reader 객체가 읽혀진 시점에 img src 속성에 부여된 고유 url을 넣어주기
					reader.onload = function(e/*이벤트 정보*/){
						console.log(e)					
						//이벤트 객체에서 reader가 부여한 고유 url 정보
						//event.target.result
						console.log(e.target.result);


						switch(num){
							case 1: $("#titleImg").attr("src", e.target.result); break;
							case 2: $("#contentImg1").attr("src", e.target.result); break;
							case 3: $("#contentImg2").attr("src", e.target.result); break;
							case 4: $("#contentImg3").attr("src", e.target.result); break;
						}


					}

				}else{	//length가 1이 아니면
					
					switch(num){
						case 1: $("#titleImg").attr("src", null); break;
						case 2: $("#contentImg1").attr("src", null); break;
						case 3: $("#contentImg2").attr("src", null); break;
						case 4: $("#contentImg3").attr("src", null); break;
					}

				}
			}
		
			// $("input[type=file]").change(function(){
			// 	console.log("======");
			// 	console.log($(this).prop("files"));
			// });
		
		
		</script>


		<br><br>
	</div>
</body>
</html>