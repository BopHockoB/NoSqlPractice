package ua.nure.nosqlpractice.user.userControllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;
import ua.nure.nosqlpractice.user.userDao.UserDAOProxy;
import ua.nure.nosqlpractice.user.userMemento.UserCaretaker;


import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserController {

    private String message = "There's no message yet";
    private final IUserDAO userDAO;

    private final UserCaretaker caretaker;

    public UserController(@Qualifier("userDAOProxy") IUserDAO userDAO, UserCaretaker caretaker) {

        this.userDAO = userDAO;
        this.caretaker = caretaker;
        ((UserDAOProxy) userDAO).registerObserver(
                o -> message = o.toString()
        );


    }


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
        try {
            userDAO.create(user);
            return "redirect:/users";
        } catch (SecurityException e) {
            return "redirect:/users?restricted";
        }

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

        try {
            user.setUserId(userId);
            caretaker.addMemento(userDAO.getById(userId).orElse(null).saveState());
            userDAO.update(user);
            return "redirect:/users";
        } catch (SecurityException e) {
            return "redirect:/users?restricted";
        }
    }

    // Delete a user by ID
    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") ObjectId userId) {
        try {
            userDAO.delete(userId);
            return "redirect:/users";
        } catch (SecurityException e) {
            return "redirect:/users?restricted";
        }
    }

    @PostMapping("/previous")
    public String restorePreviousState() {
        List<User> users = userDAO.getAll();
        if (users.isEmpty())
            return "redirect:/users?error";

        try {
            User restoredUser = caretaker.previous().getState();

            int index = IntStream.range(0, users.size())
                    .filter(i -> restoredUser.getUserId().equals(users.get(i).getUserId()))
                    .findFirst()
                    .orElse(-1);


            if (index != -1) {
               userDAO.update(restoredUser);
                return "redirect:/users";
            } else {
                return "redirect:/users?error";
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return "redirect:/users?error";
    }



}
