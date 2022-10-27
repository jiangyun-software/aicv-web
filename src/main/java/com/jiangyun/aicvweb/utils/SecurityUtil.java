package com.jiangyun.aicvweb.utils;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jiangyun.aicvweb.entity.LoginMember;
import com.jiangyun.aicvweb.entity.Member;

public class SecurityUtil {
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public static LoginMember getLoginMember() {
		return SpringUtil.getBean(RedisUtil.class).get("login_token:" + ServletUtil.getHeader("authorization"), LoginMember.class);
	}

	/**
	 * 获取当前用户信息
	 * @return
	 */
	public static Member getMemberInfo() {
		LoginMember loginMember = getLoginMember();
		return loginMember == null ? null : loginMember.getMember();
	}
	
	public static Long getMemberId() {
		return getMemberInfo().getId();
	}
	
	public static String getPhonenumber() {
		return getMemberInfo().getPhonenumber();
	}
	
	/**
	 * 密码加密
	 * @param password
	 * @return
	 */
	public static String encodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);
		return passwordEncoder.encode(password);
	}
	
	/**
	 * 验证密码
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 */
	public static boolean validPassword(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	/**
     * 检查密码强度
     *
     * @param pwd
     * @return
     */
    public static boolean checkPasswordStrong(String pwd) {
        // 1.至少8字符
        if (pwd.length() < 8) {
            ExceptionUtil.warningUser("密码不能少于8个字符");
        }
        // 2.不能超过18个字符
        if (pwd.length() > 18) {
            ExceptionUtil.warningUser("密码不能超过18个字符");
        }
        // 3.必须满足大写字母、小写字母、数字和特殊符号中的三种
        int a = Pattern.matches("^.*(?=.*\\d).*$", pwd) ? 1 : 0;
        int b = Pattern.matches("^.*(?=.*[A-Z]).*$", pwd) ? 1 : 0;
        int c = Pattern.matches("^.*(?=.*[a-z]).*$", pwd) ? 1 : 0;
        int d = Pattern.matches("^.*(?=.*[~!@#$%^&*()_+`=,.<>?-]).*$", pwd) ? 1 : 0;
        if (a + b + c + d < 3) {
//            ExceptionUtil.warningUser("密码必须包含大写字母、小写字母、数字和特殊符号中的三种字符");
        }
        // 4.不能超过4个连续的数字或字母
        int count = 0;
        byte[] pwdChar = pwd.toUpperCase().getBytes();
        for (int i = 1; i < pwdChar.length; i++) {
            if (pwdChar[i - i] == pwdChar[i]) {
                count++;
            } else {
                count = 0;
            }
            if (count == 3) {
//                ExceptionUtil.warningUser("密码不能包含连续的字符");
            }
        }

        return true;
    }
    
    public static String invitationCode(Long id) {
    	String s = "0123456789abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        while (id >= 62) {
            Long a = id % 62;
            id /= 62;
            sb.append(s.charAt(a.intValue()));
        }
        sb.append(s.charAt(id.intValue()));
        return sb.reverse().toString();
	}
}
