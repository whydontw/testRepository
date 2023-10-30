<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    .outer {
        background: linear-gradient( -100deg, red, blue, green, yellow );
        color:black;
        text-shadow: -1px 0px yellow, 0px 1px yellow, 1px 0px yellow, 0px -1px yellow;
        width:100%;
        margin:auto; /* 가운데 자동 정렬 */
        margin-top:50px; /* 위로부터 50px 아래로 여백 만들기 */
    }
    #mypage-form table {margin:auto;}
    #mypage-form input {margin:5px;}
    #deleteForm{color:black; text-shadow: -1px 0px magenta, 0px 1px magenta, 1px 0px magenta, 0px -1px magenta;}
</style>
</head>
<body>
    <%@ include file="../common/menubar.jsp" %> 
	<!-- ../ : 현재 폴더로부터 한번 빠져나감 (즉, 현재 폴더로부터 한단계 상위폴더로 이동)  -->
	
	<%
		//menubar에서 꺼내놓았던 loginUser(Member) 정보 사용하기
		String userId = loginUser.getUserId();
		String userName = loginUser.getUserName();
		
		//필수 요소가 아닌 항목에 null값이 들어가면 보여줄 때 ""로 보여주기
		//삼항 연산자 사용하기
		String phone = (loginUser.getPhone() == null? "" : loginUser.getPhone());
		String email = (loginUser.getEmail() == null? "" : loginUser.getEmail());
		String address = (loginUser.getAddress() == null? "" : loginUser.getAddress());
		String interest = (loginUser.getInterest() == null? "" : loginUser.getInterest());
		
	%>
	
	<div class="outer">
        <br>
        <h2 align="center">슬기 고쳐먹기</h2>
        
        <form id="mypage-form" action="<%= contextPath %>/update.me" method="post"> 
        <!-- menubar.jsp 를 위에서 include 했기 때문에 contextPath 변수를 가져다 쓸 수 있다. -->

            <!-- 아이디, 비밀번호, 이름, 전화번호, 이메일, 주소, 취미 -->
            <table>
                <!-- (tr>td*3)*8 -->
                <tr>
                    <td>* 아이디</td>
                    <!-- 읽기전용 속성: readonly -->
                    <!-- value 속성에 불러온 값을 넣어준다 -->
                    <td><input type="text" name="userId" maxlength="12" value="<%=userId %>" readonly></td>
                    <td></td>
                </tr>
                <tr>
                    <td>* 이름</td>
                    <td><input type="text" name="userName" maxlength="6" value="<%= userName %>" required></td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;&nbsp;전화번호</td>
                    <td><input type="text" name="phone" placeholder="- 포함해서 입력" value="<%= phone %>"><td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;&nbsp;이메일</td>
                    <td><input type="email" name="email" value="<%= email %>"></td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;&nbsp;주소</td>
                    <td><input type="text" name="address" value="<%= address %>"></td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;&nbsp;관심분야</td>
                    <td colspan="2">
                        <!-- (input[type=checkbox name=interest id= value=]+label)*6 -->


                        <!-- 동적으로 데이터를 가지고 와서 사용자에게 checked 화면 출력하여 보여줘야 한다 -->
                        <!-- javascript 혹은 jquery 함수 사용하여 보여주기 -->
                        <input type="checkbox" name="interest" id="sports" value="운동">
                        <label for="sports">운동</label>

                        <input type="checkbox" name="interest" id="hiking" value="등산">
                        <label for="hiking">등산</label>

                        <input type="checkbox" name="interest" id="fishing" value="낚시">
                        <label for="fishing">낚시</label>

                        <br>

                        <input type="checkbox" name="interest" id="cooking" value="요리">
                        <label for="cooking">요리</label>

                        <input type="checkbox" name="interest" id="game" value="게임">
                        <label for="game">게임</label>

                        <input type="checkbox" name="interest" id="movie" value="영화">
                        <label for="movie">영화</label>
                    </td>
                </tr>
            </table>
            
            <br><br>

            <div align="center">
                <button type="submit" class="btn btn-warning">슬기정보진짜로고치기</button>
                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#updatePwd">비밀번호 변경</button>
                <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#deleteForm">회원탈퇴</button>
            </div>

            <br><br>
        </form>
        
        
        <!-- 비밀번호 변경용 모달영역 -->
        <!-- The Modal -->
		<div class="modal" id="updatePwd"  style="background: linear-gradient( -100deg, pink, megenta, black, skyblue );">
		  <div class="modal-dialog">
		    <div class="modal-content">
		
		      <!-- Modal Header -->
		      <div class="modal-header">
		        <h4 class="modal-title">슬기비번고치기</h4>
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		      </div>
		
		      <!-- Modal body -->
		      <div class="modal-body" align="center">
              <form action="<%= contextPath %>/updatePwd.me" method="post">
                <!-- 현재 비밀번호, 변경할 비밀번호, 변경할 비밀번호 확인하기 -->

	                <table>
	                  <tr>
	                    <td>현재 비밀번호</td>
	                    <td>
	                      <input type="password" name="userPwd" id="" required>
	                    </td>
	                  </tr>
	                  <tr>
	                    <td>변경할 비밀번호</td>
	                    <td>
	                      <input type="password" name="updatePwd" id="" required>
	                    </td>
	                  </tr>
	                  <tr>
	                    <td>비밀번호 확인</td>
	                    <td>
	                      <input type="password" name="checkPwd" id="" required>
	                    </td>
	                  </tr>
	                </table>
	              	<button type="submit" class="btn btn-secondary" onclick="return pwdCheck();">비번고치기</button>
              </form>

              <br>
		      </div>
		      <!-- Modal footer -->
		      <div class="modal-footer">
		        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
		      </div>
		
		    </div>
		  </div>
		</div>
		
		
		<script type="text/javascript">
		
			function pwdCheck(){
				//변경할 비밀번호와 비밀번호 확인 값이 같은지 확인 후
				//다르면 기존 버튼에 걸려있는 기본이벤트인 submit 작업을 막아주는 처리를 할 함수
				
				var cPwd = $("input[name=updatePwd]");
				var chkPwd = $("input[name=checkPwd]");
				
				
				console.log(cPwd.val());
				console.log(chkPwd.val());
				
				
				//두 요소 객체의 value값이 같지 않다면
				if(cPwd.val() != chkPwd.val()){
					alert("변경할 비밀번호와 비밀번호 확인이 같지 않습니다.");
					cPwd.select();
					
					//기본이벤트 막기
					return false;
				}
			}
		</script>
        
        
    <!-- 회원탈퇴 모달영역 -->
    <!-- The Modal -->
		<div class="modal" id="deleteForm">
		  <div class="modal-dialog">
		    <div class="modal-content">
		
		      <!-- Modal Header -->
		      <div class="modal-header">
		        <h4 class="modal-title">슬기패밀리 탈퇴</h4>
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		      </div>
		
		      <!-- Modal body -->
		      <div class="modal-body" align="center">
		        <b>지금 탈퇴하면 다시는 슬기 패밀리에 낄 수 없습니다. <br>
		        	정말로 탈퇴하시겠습니까?
		        </b>
		        
		        <form action="<%= contextPath %>/delete.me" method="post">
              <!-- 데이터 숨겨서 전송하기 -->
              <!-- 식별자이므로 userNo도 되고, userId도 된다. -->
              <!-- userNo가 primary key라서 index 기능을 하기 때문에 userNo를 사용하는게 더 좋다. -->
              <input type="hidden" name="userNo" value="<%= loginUser.getUserNo() %>">
              <table>
                <tr>
                  <td>비밀번호</td>
                  <td>
                    <input type="password" name="userPwd" required>
                  </td>
                </tr>
              </table>
		        	<br><br>
              <button type="submit" class="btn btn-danger">슬기패밀리 탈퇴</button>
		        
		        </form>
				        
		        
		      </div>
		
		      <!-- Modal footer -->
		      <div class="modal-footer">
		        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
		      </div>
		
		    </div>
		  </div>
		</div>
        
        
     <script type="text/javascript">
       	$(document).ready(function(){
       		
       		var interest = "<%= interest %>";			//'운동,게임,영화' or ""
       		//console.log(interest);
       		
       		//각 요소를 순차적으로 접근하며 function 실행할 수 있는 문법 : each
       		$("input[name=interest]").each(function(){
       			
	       			//interest에 있는 각 요소의 value값과 일치하는 값이 있다면 해당 요소를 checked 해주기
	       			
	       			//indexof() / search() : 일치하는 인덱스를 반환값이 없으면 -1 반환
	       			//includes(): 일치하는 값이 있으면 true / 없으면 false
	       			
	       			var value = $(this).val();
	       			
	       			//현재 value 값이 로그인 정보 중에 interest에 포함되었는지 확인
	       			if(interest.includes(value)){
	       				//값이 있다면 현재 요소에 checked 걸기
	       				$(this).attr("checked", true);
	       			}
	       			
       		});
       	});
   </script>
        
    </div>
</body>
</html>