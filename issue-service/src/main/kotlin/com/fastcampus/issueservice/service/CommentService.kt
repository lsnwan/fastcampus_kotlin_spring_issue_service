package com.fastcampus.issueservice.service

import com.fastcampus.issueservice.domain.Comment
import com.fastcampus.issueservice.exception.NotFoundException
import com.fastcampus.issueservice.model.CommentRequest
import com.fastcampus.issueservice.model.CommentResponse
import com.fastcampus.issueservice.model.toResponse
import com.fastcampus.issueservice.repository.CommentRepository
import com.fastcampus.issueservice.repository.IssueRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService (
  private val commentRepository: CommentRepository,
  private val issueRepository: IssueRepository
) {

  @Transactional
  fun create(issueId: Long, userId: Long, username: String, request: CommentRequest) : CommentResponse {
    val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")
    val comment = Comment(
      issue = issue,
      userId = userId,
      username = username,
      body = request.body
    )

    issue.comments.add(comment)
    return commentRepository.save(comment).toResponse()
  }


}