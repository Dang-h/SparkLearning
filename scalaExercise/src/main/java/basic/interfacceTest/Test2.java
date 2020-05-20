package basic.interfacceTest;

public class Test2 {
	public static void main(String[] args) {
		User dangHao = new User(1111L, "Hao", "Dang");
		User liuTom = new User(1112L, "Tom", "Liu");
		Account accountDangHao = new Account(1111L, 1234, dangHao);
		Account accountLiuTom = new Account(1235L, 1235, liuTom);

		Account[] accounts = {accountDangHao, accountLiuTom};
		AccountServiceImpl service = new AccountServiceImpl(accounts);
		System.out.println(service.findAccountByOwnerId(1111L).toString());
		System.out.println(service.countAccountsWithBalanceGreaterThan(121));
	}
}

interface AccountService {
	/**
	 * It finds an account by owner id
	 *
	 * @param id owner unique identifier
	 * @return account or null
	 */
	Account findAccountByOwnerId(long id);

	/**
	 * It count the number of account with balance > the given value
	 *
	 * @param value
	 * @return the number of accounts
	 */
	long countAccountsWithBalanceGreaterThan(long value);
}

// Declare and implement your AccountServiceImpl here
class AccountServiceImpl implements AccountService {
	Account[] accounts;

	public AccountServiceImpl(Account[] accounts) {
		this.accounts = accounts;
	}

	@Override
	public Account findAccountByOwnerId(long id) {
		Account result = null;
		for (Account account : accounts) {
			if (account.getOwner().getId() == id) {
				result = account;
				break;
			}
		}

		if (result != null) {
			return result;
		} else {
			return null;
		}
	}

	@Override
	public long countAccountsWithBalanceGreaterThan(long value) {
		long count = 0L;
		for (Account account : accounts) {
			if (account.getBalance() > value) {
				count++;
			}
		}
		return count;
	}
}


class Account {

	private long id;
	private long balance;
	private User owner;

	public Account(long id, long balance, User owner) {
		this.id = id;
		this.balance = balance;
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public long getBalance() {
		return balance;
	}

	public User getOwner() {
		return owner;
	}
}

class User {

	private long id;
	private String firstName;
	private String lastName;

	public User(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
