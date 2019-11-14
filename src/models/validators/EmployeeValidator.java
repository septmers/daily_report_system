package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DBUtil;

public class EmployeeValidator {
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag){
        List<String> errors = new ArrayList<String>();

        String code_error = validateCode(e.getCode(), code_duplicate_check_flag);
        if(!code_error.equals("")){
            errors.add(code_error);
        }

        String name_error = validateName(e.getName());
        if(!name_error.equals("")){
            errors.add(name_error);
        }

        String password_error = validatePassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")){
            errors.add(password_error);
        }

        return errors;
    }

    private static String validateCode(String code, Boolean code_duplicate_check_flag){
        //社員番号の必須入力チェック（新規登録時のみ実行）
        if(code == null || code.equals("")){
            return "社員番号を入力してください。";
        }

        //すでに登録されている社員番号との重複チェック（新規登録時または変更時のみ実行）
        if(code_duplicate_check_flag){
            EntityManager em = DBUtil.createEntityManager();
            long enployees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class)
                                            .setParameter("code", code)
                                                .getSingleResult();
            em.close();
            if(enployees_count > 0){
                return "入力された社員番号はすでに存在しています。";
            }
        }

        return "";
    }

    //社員名の必須入力チェック
    private static String validateName(String name){
        //必須入力チェック
        if(name == null || name.equals("")){
            return "氏名を入力してください。";
        }
        return "";
    }

    //パスワードの必須入力チェック
    private static String validatePassword(String password, Boolean password_check_flag){
        //パスワードを変更する場合のみ実行
        if(password_check_flag && (password == null || password.equals(""))){
            return "パスワードを入力してください。";
        }
        return "";
    }
}
