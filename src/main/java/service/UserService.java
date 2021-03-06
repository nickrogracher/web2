package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());
    /* Singletone */
    long checkForId;
    private static class UserServiceHolder{
        private final static UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceHolder.instance;
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        boolean check = false;
        if (!isExistsThisUser(user)){
            User userWithId = new User(maxId.incrementAndGet(), user.getEmail(), user.getPassword());
            dataBase.put(userWithId.getId(), userWithId);
            check = true;
        }
        return check;
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        boolean check = false;
        for (Map.Entry<Long, User> entry : dataBase.entrySet()){
            if (user.getEmail().equals(entry.getValue().getEmail()) && user.getPassword().equals(entry.getValue().getPassword())) {
                checkForId = entry.getValue().getId();
                check = true;
            }
        }
        return check;
    }

    public List<User> getAllAuth() {
        return new ArrayList<>(authMap.values());
    }

    public boolean authUser(User user) {
        boolean check = false;
        if (isExistsThisUser(user) && !isUserAuthById(checkForId)){
            User userWithId = new User(checkForId, user.getEmail(), user.getPassword());
            authMap.put(checkForId, userWithId);
            check = true;
        }
        return check;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        boolean check = false;
        for (Map.Entry<Long, User> entry : authMap.entrySet()){
            if (id.equals(entry.getKey())) {
                check = true;
                break;
            }
        }
        return check;
    }

}
