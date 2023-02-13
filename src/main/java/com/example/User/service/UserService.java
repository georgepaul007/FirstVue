package com.example.User.service;

import com.example.User.dto.LoginDTO;
import com.example.User.dto.StatusDTO;
import com.example.User.entities.User;
import com.example.User.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    UserRepository userRepository;

    @CachePut(value = "userCache")
    public User addUserDetails(Object userDTO){
        kafkaTemplate.send("userTopic",userDTO);
        System.out.println("Data sent to kafka");

        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        userRepository.save(user);
        System.out.println("Data sent to Mongo");
        return user;
    }

    @Cacheable(value = "userCache")
    public User getUserDetails(String userId){
        Optional<User> user=userRepository.findById(userId);
        User user1=new User();
        if(user.isPresent()){
            user1=user.get();
        }
        return user1;
    }

    public User getUserDetailsByName(String userName){
        Optional<User> user=userRepository.findByUserName(userName);
        User user1=new User();
        if(user.isPresent()){
            user1=user.get();
        }
        return user1;
    }

    @CacheEvict(value = "userCache")
    public void deleteUserById(String userId){
        userRepository.deleteById(userId);
        System.out.println("Successfully Deleted!");
    }

    public StatusDTO loginStatus(LoginDTO loginDTO){
        Optional<User> user=userRepository.findByUserName(loginDTO.getUserName());
        StatusDTO statusDTO=new StatusDTO();
        if(user.isPresent()) {
            User user1 = user.get();
            if(user1.getPassword().equals(loginDTO.getPassword())){
                statusDTO.setIsvalid(true);
                statusDTO.setUserId(user1.userId);
                return statusDTO;
            }
        }
        statusDTO.setIsvalid(false);
        return statusDTO;
    }
}
