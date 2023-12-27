package ua.nure.nosqlpractice.user.userDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.dataTransfer.IDataTransfer;

@Component
@RequiredArgsConstructor
public class UserDataTransfer implements IDataTransfer {

    private final UserMongoDAO userMongoDAO;
    private final UserMySQLDAO userMySQLDAO;

    @Override
    public void transferDataToMongo(){
        userMySQLDAO.getAll().forEach(userMongoDAO::create);
    }

    @Override
    public void transferDataToMySQL(){
        userMongoDAO.getAll().forEach(userMySQLDAO::create);
    }
}
