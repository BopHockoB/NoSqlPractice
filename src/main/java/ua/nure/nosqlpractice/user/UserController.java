package ua.nure.nosqlpractice.user;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
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

    @PostMapping("/create")
    public HttpStatus createUser(@RequestBody User user){
        userDAO.create(user);
        return HttpStatus.CREATED;
    }

    @PutMapping("/update")
    public HttpStatus updateUser(@RequestBody User user){
        userDAO.update(user);
        return HttpStatus.OK;
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus httpStatus(@PathVariable String id){
        userDAO.delete(new User(new ObjectId(id), null, null, null, null, null, null));
        return HttpStatus.OK;
    }
}
