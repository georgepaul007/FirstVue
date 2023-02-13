package com.example.User.controller;

import com.example.User.dto.LoginDTO;
import com.example.User.dto.StatusDTO;
import com.example.User.dto.UserDTO;
import com.example.User.entities.User;
import com.example.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafkaUser")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/addUserDetails")
    public ResponseEntity<User> addUserDetails(@RequestBody UserDTO userDTO){
        User user=userService.addUserDetails(userDTO);
        System.out.println("Hello~");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/getUserDetails/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable("userId") String userId){
        User user=userService.getUserDetails(userId);
        System.out.println("Hello~");
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/getUserDetailsByName/{userName}")
    public ResponseEntity<User> getUserDetailsByName(@PathVariable("userName") String userName){
        User user=userService.getUserDetailsByName(userName);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUserById(userId);
        return new ResponseEntity<>("Data deleted!",HttpStatus.OK);
    }

    @PostMapping("/getLoginStatus")
    public ResponseEntity<StatusDTO> loginStatus(@RequestBody LoginDTO loginDTO){
        StatusDTO loginStatus=userService.loginStatus(loginDTO);
        return new ResponseEntity<>(loginStatus,HttpStatus.OK);
    }
}
