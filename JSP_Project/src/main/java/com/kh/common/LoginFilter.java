package com.kh.common;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.vo.Member;

/**
 * Servlet Filter implementation class LoginFilter
 */
//view controller 사이에서 요청의 흐름을 가로챔
//필터가 필요한 매핑 주소가 여러개라면 {, , ,}로 나열하기

//만약 admin에 관련된 처리를 하는 매핑 주소가 (xxx.ad)
//
@WebFilter({"/myPage.me", "/update.me", "/delete.me"})
//@WebFilter("*.bo")	// 뒤에 .bo가 붙은 모든 url 필터링
public class LoginFilter extends HttpFilter implements Filter {
       
	//Filter에 고유한 id 부여하기(없어도 됨)
	private static final long serialVersionUID = 1L;

	
	/**
     * @see HttpFilter#HttpFilter()
     */
    public LoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
    //해당 필터가 종료되는 시점
	public void destroy() {
		// TODO Auto-generated method stub
	}

	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		ServletRequest request: 세션을 얻어낼 수 없음! HttpServletRequest 클래스보다 더 상위 클래스임. 다운 캐스팅 해야함.
		
		//HttpServletRequest로 다운캐스팅하기
		HttpSession session = ((HttpServletRequest/*다운캐스팅을 해서 getSession()으로 세션 가져오기*/)request).getSession();
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser != null) {	//로그인이 되어있다면
			//요청을 그대로 유지함
			chain.doFilter(request, response);	//chain: 요청의 흐름을 그대로 유지시켜주는 객체
			
		}else {	//로그인이 되어있지 않다면
			//흐름을 바꿔주는 작업처리
			session.setAttribute("alertMsg", "로그인이 필요한 서비스입니다. 로그인 후 이용해 주세요");
			
			//request, response 다운캐스팅하여 sendRedirect
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath());
			
		}
		
	}
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	//해당 필터가 생성된 시점
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
