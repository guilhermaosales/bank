package com.br.bank.service

import com.br.bank.model.Account
import com.br.bank.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class AccountServiceImpl(private val repository: AccountRepository) : AccountService {
    override fun create(account: Account): Account {
        Assert.hasLength(account.name, "[name] cannot be blank")
        Assert.isTrue(account.name.length >=5, "[name] needs have at least 5 characters")

        Assert.hasLength(account.document, "[document] cannot be blank")
        Assert.isTrue(account.document.length == 11, "[document] needs have at least 11 characters")

        return repository.save(account)
    }

    override fun getAll(): List<Account> {
        return repository.findAll()
    }

    override fun getById(id: Long): Optional<Account> {
        return repository.findById(id)
    }

    override fun update(id: Long, account: Account): Optional<Account> {
        val optional = getById(id)
        if (optional.isEmpty) Optional.empty<Account>()

        return optional.map {
            val accountToUpdate = it.copy(
                name = account.name,
                document = account.document,
                phone = account.phone
            )
            create(accountToUpdate)
        }
    }

    override fun delete(id: Long) {
        repository.findById(id).map {
            repository.delete(it)
        }.orElseThrow { throw RuntimeException ("Id not found $id") }

    }
}