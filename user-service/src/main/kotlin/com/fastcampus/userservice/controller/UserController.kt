package com.fastcampus.userservice.controller

import com.fastcampus.userservice.model.*
import com.fastcampus.userservice.service.UserService
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KotlinLogging
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.File

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

  @GetMapping("/me")
  suspend fun get(
    @AuthToken token: String,
  ) : MeResponse {
    return MeResponse(userService.getByToken(token))
  }

  @GetMapping("/{userId}/username")
  suspend fun getUsername(@PathVariable userId: Long) : Map<String, String> {
    return mapOf("reporter" to userService.get(userId).username)
  }

  @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  suspend fun edit(
    @PathVariable id: Long,
    @ModelAttribute request: UserEditRequest,
    @AuthToken token: String,
    @RequestPart("profileUrl") filePart: FilePart
  ) {

    val orgFilename = filePart.filename()
    var filename: String? = null
    if (orgFilename.isNotEmpty()) {
      val ext = orgFilename.substring(orgFilename.lastIndexOf(".") + 1)
      filename = "${id}.${ext}"
      val file = File(ClassPathResource("/images/").file, filename)
      filePart.transferTo(file).awaitSingleOrNull()
    }
    userService.edit(token, request.username, filename)

  }


}