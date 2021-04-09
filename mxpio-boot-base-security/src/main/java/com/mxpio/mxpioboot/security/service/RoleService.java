package com.mxpio.mxpioboot.security.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mxpio.mxpioboot.security.entity.Role;

public interface RoleService {

	void load(Pageable page);

	void save(List<Role> roles);

}