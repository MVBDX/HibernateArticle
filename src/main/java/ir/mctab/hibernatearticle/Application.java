package ir.mctab.hibernatearticle;

import ir.mctab.hibernatearticle.config.hibernate.HibernateUtil;
import ir.mctab.hibernatearticle.entities.Article;
import ir.mctab.hibernatearticle.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        menu();
    }

    protected void loginUser(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        String userName = getUserInput("Username:");
        String userPassword = getUserInput("Password:");

        //TODO
        // find by id
        //User user0 = session.
        //System.out.println(user0);
    }

    protected void registerUser() {
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
            Session session = sessionFactory.openSession();
            session.beginTransaction(); // transaction start

            String userName = getUserInput("Username:");
            //String userPassword = getUserInput("Password:");
            String nationalCode = getUserInput("National code:");
            String birthday = getUserInput("Birthday:");

            User user = new User(null, userName, nationalCode, nationalCode, new SimpleDateFormat("dd/MM/yyyy").parse(birthday));
            Long id = (Long) session.save(user);
            session.getTransaction().commit();
            session.close();

            System.out.println("Register successful. now you can login.");
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    protected void showArticles() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        Query query = session.createQuery("from Article ");
        List<Article> articleList = query.list();
        if (articleList.size() == 0) System.out.println("There is no articles!");
        else articleList.forEach(System.out::println);
        //System.out.println(articleList.stream().filter(user1 -> user1.getId() > 2).count());
        session.close();

        //remove
//        User user1 = session.load(User.class, 2L);
//        session.remove(user1);
//
        // update
//        User user2 = session.load(User.class, 3L);
//        user2.setUsername("UpdatedUser!");
//        session.update(user2);

        // transaction commit
        //session.getTransaction().commit();
    }

    public static void menu() {
        int selectedMenu;
        do {
            startScreen();
            selectedMenu = Integer.parseInt(getUserInput("Select an option to continue; 0-3"));

            switch (selectedMenu) {
                case 1:
                    new Application().loginUser();
                    break;
                case 2:
                    new Application().registerUser();
                    break;
                case 3:
                    new Application().showArticles();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    //getUserInput("Select an option to continue, 0-2");
                    //innerChoice1();
                    break;
            }
        } while (selectedMenu != 0);
    }

    public static void startScreen() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. View articles");
        System.out.println("0. Exit");
    }

    private static String getUserInput(String systemOut) throws NumberFormatException {
        if (systemOut != null && !systemOut.equals("")) System.out.println(systemOut);
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

}