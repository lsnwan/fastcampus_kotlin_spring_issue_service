package com.fastcampus.userservice.controller

import com.fastcampus.userservice.model.AuthToken
import com.fastcampus.userservice.model.SignInRequest
import com.fastcampus.userservice.model.SignInResponse
import com.fastcampus.userservice.model.SignUpRequest
import com.fastcampus.userservice.service.UserService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController (
  private val userService: UserService,
) {

  private val logger = KotlinLogging.logger {  }

  @PostMapping("/signup")
  suspend fun signup(@RequestBody request : SignUpRequest) {
    logger.info { "회원가입 컨트롤러" }
    userService.signUp(request)
  }

  @PostMapping("/signin")
  suspend fun signin(@RequestBody signInRequest: SignInRequest): SignInResponse {
    logger.info { "로그인 컨트롤러" }
    return userService.signIn(signInRequest)
  }

  @DeleteMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  suspend fun logout(@AuthToken token: String) {
    userService.logout(token)
  }
}