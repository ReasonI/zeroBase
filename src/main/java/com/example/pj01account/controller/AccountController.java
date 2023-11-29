package com.example.pj01account.controller;

import com.example.pj01account.domain.Account;
import com.example.pj01account.dto.AccountDto;
import com.example.pj01account.dto.AccountInfo;
import com.example.pj01account.dto.CreateAccount;
import com.example.pj01account.dto.DeleteAccount;
import com.example.pj01account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/account")
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
    ) {

        return CreateAccount.Response.from(
                accountService.createAccount(
                request.getUserId(),
                request.getInitialBalance()
                )
        );
    }

    @DeleteMapping("/account")
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {

        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()
                )
        );
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountsByUserId(
            @RequestParam("user_id") Long userId
    ){
        return accountService.getAccountByUserId(userId)
                .stream().map(accountDto ->
                        AccountInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }
}
