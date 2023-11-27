package com.example.pj01account.service;

import com.example.pj01account.domain.Account;
import com.example.pj01account.domain.AccountUser;
import com.example.pj01account.dto.AccountDto;
import com.example.pj01account.exception.AccountException;
import com.example.pj01account.repository.AccountRepository;
import com.example.pj01account.repository.AccountUserRepository;
import com.example.pj01account.type.AccountStatus;
import com.example.pj01account.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.pj01account.type.AccountStatus.IN_USE;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     *
     * @param userId
     * @param initialBalance
     * 사용자가 있는지 조회
     * 계좌의 번호를 생성하고
     * 계좌를 저장, 그 정보를 넘긴다.
     */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
                .orElse("1000000000");

        return AccountDto.fromEntity(
                accountRepository.save(Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build()
        ));
    }

    @Transactional
    public Account getAccount(Long id){
        return accountRepository.findById(id).get();
    }

}
