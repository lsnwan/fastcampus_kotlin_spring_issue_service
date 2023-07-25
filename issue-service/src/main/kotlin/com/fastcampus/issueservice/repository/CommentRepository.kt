package com.fastcampus.issueservice.repository

import com.fastcampus.issueservice.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}