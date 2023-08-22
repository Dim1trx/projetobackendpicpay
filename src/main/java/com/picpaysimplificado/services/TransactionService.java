package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service

public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {

        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());
        BigDecimal value = transactionDTO.value();
        userService.validateTransaciton(sender, value);


        boolean isAuthorized = this.authorizeTransaction(sender, value);
        if (!isAuthorized){
            throw  new Exception("Transação não autorizada");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setUser(sender);
        transaction.setReceiver(receiver);
        transaction.setTimeStamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(value));
        receiver.setBalance(receiver.getBalance().add(value));

        this.transactionRepository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return transaction;

    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizeResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

        if(authorizeResponse.getStatusCode() == HttpStatus.OK){

            String message = (String) authorizeResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;

    }

    public List<Transaction> getAllTransactions(){
        return this.transactionRepository.findAll();
    }
}
