package com.example.demo.user.repository.impl;

import com.example.demo.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

  private final JPAQueryFactory jpaQueryFactory;
}
