package ua.nure.nosqlpractice.user;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final IUserDAO userDAO;

    @GetMapping
    public List<User> findAllUsers() {
        return userDAO.getAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable String id){
        return userDAO.getById(new ObjectId(id)).orElse(null);
    }

    @GetMapping("/name/{name}")
    public User findUserByLastName(@PathVariable String name){
        return userDAO.getByLastName(name).orElse(null);
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public String createUser(@RequestBody User user){
        userDAO.create(user);
        return user.toString();
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user){
        userDAO.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> httpStatus(@PathVariable String id){
        userDAO.delete(new User(new ObjectId(id), null, null, null, null, (short) 0, null));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
