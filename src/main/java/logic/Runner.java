package logic;

import java.io.*;
import java.util.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.validation.*;

public class Runner {

    static String exit = "q";

    /**
     * сам себе лаунчер :)
     *
     * @param args
     */
    public static void main(String args[]) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "SpringConfig.xml");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        String isExit = "";
        do {
            // Когда для автоматического поиска бины помечены как компоненты, в
            // методе getBean в качестве имени выступает имя класса с маленькой
            // буквы, по соглашению
            TextProcessor obj = (TextProcessor) context.getBean("textProcessor");

            validate(obj, validator);

            obj.run();

            System.out.println("Enter \"" + exit + "\" to exit, or enter any other to reload properties and re-process file...");

            isExit = bufferedReader.readLine();
            context.refresh();
        } while (!isExit.equals(exit));

        bufferedReader.close();
        context.close();
        factory.close();
    }

    // http://habrahabr.ru/post/68318/
    public static void validate(Object object, Validator validator) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(object);
        if(constraintViolations.size()!=0){
            System.err.println("[Hibernate Validator] Validation erors:");
            for (ConstraintViolation<Object> cv : constraintViolations)
                System.err.println(String.format(
                        "property: [%s], value: [%s], message: [%s]",
                        cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));

            throw new ValidationException();
        }
    }
}
