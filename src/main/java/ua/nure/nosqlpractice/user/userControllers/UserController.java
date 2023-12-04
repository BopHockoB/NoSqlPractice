package ua.nure.nosqlpractice.user.userControllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.observers.Observer;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;
import ua.nure.nosqlpractice.user.userDao.UserMySQLDAO;


import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private String message = "There's no message yet";
    private final IUserDAO userDAO;
    //private Observer observer;

    public UserController(@Qualifier("userMySQLDAO") IUserDAO userDAO) {

        this.userDAO = userDAO;
        ((UserMySQLDAO) userDAO).registerObserver(
                o -> message = o.toString()
        );


    }

    // Display all users
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userDAO.getAll();
        model.addAttribute("users", users);
        model.addAttribute("message", message);

        return "user/list";
    }

    // Display form for adding a new user
    @GetMapping("/new")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/new";
    }

    // Save a new user
    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") User user) {
        user.setUserId(new ObjectId());
        userDAO.create(user);
        return "redirect:/users";
    }

    // Display form for updating a user by ID
    @GetMapping("/edit/{userId}")
    public String showUpdateForm(@PathVariable("userId") ObjectId userId, Model model) {
        User user = userDAO.getById(userId).orElse(null);
        model.addAttribute("user", user);
        return "user/edit";
    }

    // Update a user
    @PostMapping("/edit/{userId}")
    public String updateUser(@PathVariable("userId") ObjectId userId, @ModelAttribute("user") User user) {
        user.setUserId(userId);
        userDAO.update(user);
        return "redirect:/users";
    }

    // Delete a user by ID
    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") ObjectId userId) {
        userDAO.delete(userId);
        return "redirect:/users";
    }
}
