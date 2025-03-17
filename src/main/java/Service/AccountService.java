package Service;

import Model.Account;
import DAO.AccountDAO;



public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }
        else if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        else if (accountDAO.usernameAlreadyExists(account.getUsername())){
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    public Account loginAccount(Account account){
        return accountDAO.logIntoAccount(account);
    }
}
