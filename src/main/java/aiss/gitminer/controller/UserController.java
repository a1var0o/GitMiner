package aiss.gitminer.controller;

import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "User", description = "User management API")
@RestController
@RequestMapping("/gitminer/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "Retrieve all users",
            description = "Get all users from the database",
            tags = {"users", "get"}
    )
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")})
    })
    @QueryMapping
    public List<User> users() {
        return userRepository.findAll();
    }

    @Operation(summary = "Retrieve a user",
            description = "Get a user by its Id",
            tags = {"users", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    @QueryMapping
    public User userById(@PathVariable @Argument String id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }
}
