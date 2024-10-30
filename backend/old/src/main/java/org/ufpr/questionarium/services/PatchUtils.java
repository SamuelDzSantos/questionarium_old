package org.ufpr.questionarium.services;

import java.lang.reflect.Field;

import org.springframework.stereotype.Service;
import org.ufpr.questionarium.models.User;

@Service
public class PatchUtils {

    public void userPatch(User existingUser, User incompleteUser) {
        Class<?> userClass = User.class;
        Field[] fields = userClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(incompleteUser);
                if (value != null)
                    field.set(existingUser, value);
            } catch (Exception e) {
                throw new RuntimeException();
            } finally {
                field.setAccessible(false);
            }
        }
    }

}
