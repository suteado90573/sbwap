package com.sb.sbwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sb.sblib.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final AccountService accountService;

	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}

	@PostMapping("/login")
	public String postLogin(String username, String password) {

		// 認証成功ならばトップ画面、認証失敗ならログイン画面に遷移する
		if (accountService.auth(username, password)) {
			return "top";
		}
		return "login";
	}
}
