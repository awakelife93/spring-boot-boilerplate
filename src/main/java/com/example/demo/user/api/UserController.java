package com.example.demo.user.api;

import com.example.demo.common.response.ErrorResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final GetUserService getUserService;
  private final ChangeUserService changeUserService;

  @Operation(
    operationId = "getUserById",
    summary = "Get User",
    description = "Get User By User Id API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          schema = @Schema(implementation = GetUserResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User Not Found",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @GetMapping("/{userId}")
  public ResponseEntity<GetUserResponse> getUserById(
    @PathVariable("userId") Long userId
  ) {
    final GetUserResponse getUserResponse = getUserService.getUserById(userId);
    return ResponseEntity.ok(getUserResponse);
  }

  @Operation(
    operationId = "getUserList",
    summary = "Get User List",
    description = "Get User List API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          array = @ArraySchema(
            schema = @Schema(implementation = GetUserResponse.class)
          )
        )
      ),
    }
  )
  @GetMapping
  public ResponseEntity<List<GetUserResponse>> getUserList(Pageable pageable) {
    final List<GetUserResponse> listUserResponses = getUserService.getUserList(
      pageable
    );
    return ResponseEntity.ok(listUserResponses);
  }

  @Operation(
    operationId = "createUser",
    summary = "Create User",
    description = "Create User API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Created",
        content = @Content(
          schema = @Schema(implementation = CreateUserResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Field Valid Error",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "409",
        description = "Already User Exist",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @PostMapping("/register")
  public ResponseEntity<CreateUserResponse> createUser(
    @RequestBody @Valid CreateUserRequest createUserRequest
  ) {
    final CreateUserResponse createUserResponse = changeUserService.createUser(
      createUserRequest
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
  }

  @Operation(
    operationId = "updateUser",
    summary = "Update User",
    description = "Update User API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          schema = @Schema(implementation = UpdateUserResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Request Body Valid Error",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User Not Found",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @PatchMapping("/{userId}")
  public ResponseEntity<UpdateUserResponse> updateUser(
    @RequestBody @Valid UpdateUserRequest updateUserRequest,
    @PathVariable("userId") Long userId
  ) {
    final UpdateUserResponse updateUserResponse = changeUserService.updateUser(
      userId,
      updateUserRequest
    );
    return ResponseEntity.ok(updateUserResponse);
  }

  @Operation(
    operationId = "updateMe",
    summary = "Update Me",
    description = "Update Me API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          schema = @Schema(implementation = UpdateMeResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Request Body Valid Error",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User Not Found",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @PatchMapping
  public ResponseEntity<UpdateMeResponse> updateMe(
    @RequestBody @Valid UpdateUserRequest updateUserRequest,
    @CurrentUser SecurityUserItem securityUserItem
  ) {
    final UpdateMeResponse updateMeResponse = changeUserService.updateMe(
      securityUserItem.getUserId(),
      updateUserRequest
    );
    return ResponseEntity.ok(updateMeResponse);
  }

  @Operation(
    operationId = "deleteUser",
    summary = "Delete User",
    description = "Delete User API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
    changeUserService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
