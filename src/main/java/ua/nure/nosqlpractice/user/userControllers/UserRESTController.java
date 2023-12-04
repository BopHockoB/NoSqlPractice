package ua.nure.nosqlpractice.user.userControllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;

import java.util.List;

@RestController
@RequestMapping("/user-rest")
public class UserRESTController {

    private final IUserDAO userDAO;

    @Autowired
    public UserRESTController(@Qualifier("userMongoDAO") IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

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
        userDAO.delete(new ObjectId(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
