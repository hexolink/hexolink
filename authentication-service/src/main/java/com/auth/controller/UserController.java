package com.auth.controller;

import com.auth.entity.Avatar;
import com.auth.entity.User;
import com.auth.service.avatar.AvatarService;
import com.auth.service.userservice.UserService;
import com.auth.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.security.Principal;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final UserService userService;
    private final AvatarService avatarService;

    @Autowired
    public UserController(UserService userService, AvatarService avatarService) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @GetMapping("/me")
    public Principal getServiceUser(Principal principal) {
        return principal;
    }

    @PutMapping("/update")
    public void updateUser(Principal principal, @Valid @RequestBody UserTo updatedFields) {
        UsernamePasswordAuthenticationToken principal1 = (UsernamePasswordAuthenticationToken) principal;
        User currentUserFromHeader = (User) principal1.getPrincipal();
        if (currentUserFromHeader.getEmail().equals(updatedFields.getEmail())) {
            userService.update(currentUserFromHeader, updatedFields);
        }
    }

    @GetMapping(value = "/avatar")
    public void getAvatar(Principal principal, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken principal1 = (UsernamePasswordAuthenticationToken) principal;
        User currentUserFromHeader = (User) principal1.getPrincipal();
        Avatar avatar = avatarService.get(currentUserFromHeader.getId());
//        response.setHeader("Content-Disposition", "inline;filename=\"" + attachment.getDocId() + "." + attachment.getAttachmentExtension().toLowerCase() + "\"");
        response.setContentType("image/png");
        response.setHeader("Accept-Ranges", "bytes");
        try (OutputStream os = response.getOutputStream()) {
            os.write(avatar.getAvatar());
            os.flush();
        } catch (Exception ignored) {
        }
    }
}
