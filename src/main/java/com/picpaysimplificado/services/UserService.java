package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaciton(User sender, BigDecimal amount) throws Exception{
        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar transação");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Usuário não saldo suficiente para a transação");

        }


    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);

        return newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }
}
