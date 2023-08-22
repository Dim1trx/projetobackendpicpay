package com.picpaysimplificado.controllers;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception{
        Transaction newTransaction = this.transactionService.createTransaction(transactionDTO);

        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactions =  this.transactionService.getAllTransactions();

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
