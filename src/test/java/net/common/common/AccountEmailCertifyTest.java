package net.common.common;

import net.mwav.common.module.MailLib;
import net.mwav.common.module.SecurityLib;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.mail.Message;
import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/mwav-mvc.xml"})
@WebAppConfiguration
public class AccountEmailCertifyTest {

    @Spy @Autowired
    ServletContext servletContext;

    @InjectMocks
    AccountEmailCertify controller;

    MockMvc mockMvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        assertThat(controller).isNotNull();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * <pre>
     *     이메일 테스트를 위해 이메일 발송를 Mock으로 대체해야하지만
     *     MailLib는 Bill Pugh Singleton 패턴으로 이루어져 있기 때문에
     *     inner class, private, final을 무시하는 reflection 기술로
     *     runtime에 MailLib.MailLibSingleton.instance의 값을 mock으로 수정함
     * </pre>
     * @throws Exception
     */
    private void makeMockMailLib() throws Exception {
        //구현된 mock객체를 미리 선언
        MailLib mailLib = spy(MailLib.getInstance());

        //MailLib 하위에 있는 MailLibSingleton 클래스를 찾음
        Class<?> declaredClass = MailLib.class.getDeclaredClasses()[0];

        //MailLib의 하위의 private static class MailLibSingleton 클래스의 instance 필드를 찾음
        final Field field = declaredClass.getDeclaredField("instance");

        //private 객체에 액세스하기 위해 SecurityManager 허용 요청
        field.setAccessible(true);

        //final 객체를 수정하기 위해 리플렉션 객체를 수정함
        final Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        //MailLib.MailLibSingleton.instance의 값을 mock으로 수정
        field.set(MailLib.class, mock(MailLib.class));

        //mailLib.send 호출 시 무시한다.
        doNothing().when(mailLib).send(any(Message.class));
    }

    @Test
    public void certify_이메일_발송_및_이메일_확인_페이지로_포워딩() throws Exception {
        //given & when
        makeMockMailLib();

        //then
        mockMvc.perform(get("/accounts/email/certify")
                        .param("email", "tony950620@naver.com")
                        .param("id", "memberId")
                        .param("account", "promoter"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void authority_이메일_인증_후_인증완료_체크() throws Exception {

        mockMvc.perform(get("/accounts/email/authority/9INuwYNJSrEqzo84tHVhu4smeDaffpAi+pjnsEKyzd8Gpc~P6CCqOiZRKkhH7RPQG27Mx+lkn9~2U+BxJrJTsIHtB+mXCVT5jTc55iwH9nU="))
                .andDo(print());
    }
}