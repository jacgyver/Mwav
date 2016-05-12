/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mwav.member.contrloller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.mwav.member.auth.google.SignInUtils;

import org.apache.maven.model.Model;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;


@Controller
public class SignController {
	private final ProviderSignInUtils providerSignInUtils;

	/*@Autowired
	MemberService memberService;*/

	@Inject
	public SignController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository) {
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public void signin() {
	}

	@RequestMapping("/google/expired")
	public void simulateExpiredToken() { // 만료된 토큰 관리
		throw new ExpiredAuthorizationException("google");
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm(WebRequest req, Model model, HttpServletRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(req);
		String member_email = connection.fetchUserProfile().getEmail();

		if (connection != null) {
			String getUserName = connection.fetchUserProfile().getUsername();
			SignInUtils.signin(member_email); // 인증
			//providerSignInUtils.doPostSignUp(getUserName, request); // userConnection Row 생성
			
			// Member Row 생성
			/*boolean checkCreateMember = memberService.selectEmailMember(member_email);
			System.out.println("checkCreateMember = "+checkCreateMember);
			if (checkCreateMember) {
				Member member = new Member(memberService.selectCountMember(), connection.getDisplayName(), member_email,
						connection.getImageUrl(), getUserName);
				memberService.insertMember(member);
				
				request.getSession().setAttribute("member_email", member_email);
			}*/

		}
		
		return "/memberDefault.do";
	}

}
