package com.sap.cc.users;

import com.sap.cc.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserStorage userStorage;

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public ResponseEntity<User> signUpUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder)
            throws URISyntaxException {
        User createdUser = userStorage.saveUser(user);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/users" + "/{id}")
                .buildAndExpand(createdUser.getId());
        URI locationHeaderUri = new URI(uriComponents.getPath());
        return ResponseEntity.created(locationHeaderUri).body(createdUser);
    }

    @GetMapping("/pretty/{id}")
    public ResponseEntity<String> printPrettyPage(@PathVariable("id") Long id) {

        if (id < 1) {
            throw new IllegalArgumentException("Id must not be less than 1");
        }

        Optional<User> user = userStorage.retrieveUserById(id);

        if (user.isPresent()) {

            String prettyPage = getPrettyPage(user.get());

            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(prettyPage);
        }

        throw new NotFoundException();
    }

    public String getPrettyPage(User user) {
        return user.getName() + System.lineSeparator() + user.getPhoneNumber();
    }
}
