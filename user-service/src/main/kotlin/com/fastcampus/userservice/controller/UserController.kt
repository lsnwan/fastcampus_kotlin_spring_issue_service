package com.fastcampus.userservice.controller

import com.fastcampus.userservice.model.SignInResponse
import com.fastcampus.userservice.model.SignUpRequest
import com.fastcampus.userservice.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController (
  private val userService: UserService,
) {

  @PostMapping("/signup")
  suspend fun signup(@RequestBody request : SignUpRequest) {
    userService.signUp(request)
  }

  @PostMapping("/signin")
  suspend fun signin(@RequestBody signInRequest: SignUpRequest): SignInResponse {
    return userService.signIn(signInRequest)
  }

}