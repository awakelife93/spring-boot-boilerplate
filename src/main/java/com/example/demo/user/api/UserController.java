package com.example.demo.user.api;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.annotation.CurrentUser;
import com.example.demo.user.application.ChangeUserService;
import com.example.demo.user.application.GetUserService;
import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.CreateUserResponse;
import com.example.demo.user.dto.serve.response.GetUserResponse;
import com.example.demo.user.dto.serve.response.UpdateMeResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final GetUserService getUserService;
  private final ChangeUserService changeUserService;

  @GetMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<GetUserResponse> getUserById(
    @PathVariable("userId") Long userId
  ) {
    final GetUserResponse userResponse = getUserService.getUserById(userId);
    return ResponseEntity.ok(userResponse);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<GetUserResponse>> getUserList(Pageable pageable) {
    final List<GetUserResponse> usersResponse = getUserService.getUserList(
      pageable
    );
    return ResponseEntity.ok(usersResponse);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<CreateUserResponse> createUser(
    @RequestBody @Valid CreateUserRequest dto
  ) {
    final CreateUserResponse createUserResponse = changeUserService.createUser(
      dto
    );
    return ResponseEntity.ok(createUserResponse);
  }

  @PatchMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<UpdateUserResponse> updateUser(
    @RequestBody @Valid UpdateUserRequest dto,
    @PathVariable("userId") Long userId
  ) {
    final UpdateUserResponse usersResponse = changeUserService.updateUser(
      userId,
      dto
    );
    return ResponseEntity.ok(usersResponse);
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<UpdateMeResponse> updateMe(
    @RequestBody @Valid UpdateUserRequest dto,
    @CurrentUser SecurityUserItem securityUserItem
  ) {
    final UpdateMeResponse usersResponse = changeUserService.updateMe(
      securityUserItem.getUserId(),
      dto
    );
    return ResponseEntity.ok(usersResponse);
  }

  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("userId") Long userId) {
    changeUserService.deleteUser(userId);
  }
}
