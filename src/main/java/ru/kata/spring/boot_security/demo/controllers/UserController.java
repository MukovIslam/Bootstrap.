package ru.kata.spring.boot_security.demo.controllers;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.securityService.UserSecurityService;
import ru.kata.spring.boot_security.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {


    private final IUserService userService;
    private final IDaoRoleService iDaoRoleService;
    @Autowired
    public UserController(IUserService userService, UserSecurityService securityService, IDaoRoleService iDaoRoleService) {
        this.userService = userService;
        this.iDaoRoleService = iDaoRoleService;
    }

    @GetMapping("admin")
    public String index(Model model, Principal principal) {
        User user = new User();

        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        model.addAttribute("AllPeople", userService.getAllUsers());
        model.addAttribute("person", user);
        model.addAttribute("allroles" ,(iDaoRoleService.getAllRole()));
        return "people/admin";
    }

    @GetMapping("user/{id}")
    public String show(@PathVariable("id") int id, Model model,Principal principal) {
        model.addAttribute("idPeople", userService.show(id));
        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));

        return "people/userPage";
    }

    @GetMapping("admin/new")
    public String newPerson(Model model) {
        User user = new User();
        model.addAttribute("allroles" ,(iDaoRoleService.getAllRole()));
        model.addAttribute("person", user);
        return "people/new";
    }

    @GetMapping("admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        userService.updateUser(userService.show(id));
        return "redirect:/admin";
    }

    @PostMapping("admin/{id}/del")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("admin/create")
    public String create(@ModelAttribute("person") User person) {
        List<Role> rol = new ArrayList<>();
        for (Role role : person.getRoles()){
                rol.add(iDaoRoleService.getRole(role.getName()));
            System.out.println(role.getName());
        }
        person.setRoles(rol);
        userService.addUser(person);
        return "redirect:/admin";
    }

    @PostMapping("admin/{id}")
    public String update(@ModelAttribute("person") User user) {
        List<Role> rol = new ArrayList<>();
        for (Role role : user.getRoles()){
            rol.add(iDaoRoleService.getRole(role.getName()));
            System.out.println(role.getName());
        }
        user.setRoles(rol);
        userService.updateUser(user);
        return "redirect:/admin";
       }

       @PostMapping("admin/{id}/setRolAdmin")
    public String setRolAdmin(@PathVariable("id") int id) {

           User user = userService.show(id);
           if (!user.getRoles().contains(iDaoRoleService.getRole("ADMIN"))) {
               user.getRoles().add(iDaoRoleService.getRole("ADMIN"));
               userService.updateUser(user);
           }

           return "redirect:/admin";
    }
    @PostMapping("admin/{id}/setRolUser")
    public String setRolUser(@PathVariable("id") int id, HttpServletRequest request) {
        User user = userService.show(id);
        if (!user.getRoles().contains(iDaoRoleService.getRole("USER"))) {
            user.getRoles().add(iDaoRoleService.getRole("USER"));
            userService.updateUser(user);
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
