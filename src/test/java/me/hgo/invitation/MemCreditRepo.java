package me.hgo.invitation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemCreditRepo implements CreditRepo {

    HashMap<CharSequence, List<Transaction>> map = new HashMap<>();

    @Override
    public List<Transaction> gainsOf(CharSequence id) {
        return map.get(id);
    }

    @Override
    public Transaction createNewTransaction(Transaction transaction) {
        List<Transaction> transactions = map.get(transaction.getFromId());
        if (transactions == null) {
            transactions = new ArrayList<Transaction>();
        }
        transactions.add(transaction);
        return transaction;
    }
}
