package com.jiangyun.aicvweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jiangyun.aicvweb.entity.Member;

@Mapper
public interface MemberMapper {

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	int save(Member user);
	
	/**
	 * 绑定微信
	 * @param user
	 * @return
	 */
	int bindWechat(Member user);
	
	/**
	 * 启用禁用
	 * @param id
	 * @param disabled
	 * @return
	 */
	int disabled(@Param("id")Long id, @Param("disabled") Boolean disabled);
	
	/**
	 * 查询用户列表
	 * @param user
	 * @return
	 */
	List<Member> selectMemberList(Member user);
	
	/**
	 * 查询用户详情
	 * @param id
	 * @return
	 */
	Member selectMemberById(Long id);
	
	/**
	 * 根据手机号查询用户详情
	 * @param id
	 * @return
	 */
	Member selectMemberByPhonenumber(String phonenumber);
	
	/**
	 * 根据openid查询用户详情
	 * @param id
	 * @return
	 */
	Member selectMemberByOpenid(String openid);
	
	Member selectMemberByInvitationCode(String invittionCode);
	
	/**
	 * 修改手机号
	 * @param id
	 * @param phonenumber
	 * @return
	 */
	int changePhonenumber(@Param("id") Long id, @Param("phonenumber") String phonenumber);
	
	/**
	 * 修改密码
	 * @param id
	 * @param password
	 * @return
	 */
	int changePassword(@Param("id") Long id, @Param("password") String password);
}
