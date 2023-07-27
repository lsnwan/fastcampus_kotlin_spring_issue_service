package com.fastcampus.userservice.service

import com.fastcampus.userservice.config.JWTProperties
import com.fastcampus.userservice.domain.entity.User
import com.fastcampus.userservice.domain.repository.UserRepository
import com.fastcampus.userservice.exception.PasswordNotMatchedException
import com.fastcampus.userservice.exception.UserExistsException
import com.fastcampus.userservice.exception.UserNotFoundException
import com.fastcampus.userservice.model.SignInResponse
import com.fastcampus.userservice.model.SignUpRequest
import com.fastcampus.userservice.utils.BCryptUtils
import com.fastcampus.userservice.utils.JWTClaim
import com.fastcampus.userservice.utils.JWTUtils
import org.springframework.stereotype.Service

@Service
class UserService (
  private val userRepository: UserRepository,
  private val jwtProperties: JWTProperties,
) {

  suspend fun signUp(signUpRequest: SignUpRequest) {
    with(signUpRequest) {
      userRepository.findByEmail(email)?.let {
        throw UserExistsException()
      }
      val user = User(
        email = email,
        password = BCryptUtils.hash(password),
        username = username,
      )
      userRepository.save(user)
    }
  }

  suspend fun signIn(signInRequest: SignUpRequest): SignInResponse {
    return with (userRepository.findByEmail(signInRequest.email) ?: throw UserNotFoundException()) {
      val verified = BCryptUtils.verify(signInRequest.password, password)
      if (!verified) {
        throw PasswordNotMatchedException()
      }

      val jwtClaim = JWTClaim(
        userId = id!!,
        email = email,
        profileUrl = profileUrl,
        username = username
      )

      val newToken = JWTUtils.createToken(jwtClaim, jwtProperties)

      SignInResponse(
        email = email,
        username = username,
        token = newToken
      )

    }
  }

}
