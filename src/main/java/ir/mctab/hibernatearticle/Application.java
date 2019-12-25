package ir.mctab.hibernatearticle;

import ir.mctab.hibernatearticle.config.hibernate.HibernateUtil;
import ir.mctab.hibernatearticle.entities.Article;
import ir.mctab.hibernatearticle.entities.Category;
import ir.mctab.hibernatearticle.entities.User;
import ir.mctab.hibernatearticle.features.usermanagement.impl.LoginUseCaseImpl;
import ir.mctab.hibernatearticle.features.usermanagement.impl.LogoutUseCaseImpl;
import ir.mctab.hibernatearticle.features.usermanagement.usecases.LoginUseCase;
import ir.mctab.hibernatearticle.features.usermanagement.usecases.LogoutUseCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Application {
    public LoginUseCase loginUseCase = null;
    public static User user = null;

    public static void main(String[] args) {
        menu();
    }

    protected void loginUser() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        String userName = getUserInput("Username:");
        String userPassword = getUserInput("Password:");

        loginUseCase = new LoginUseCaseImpl();
        user = loginUseCase.login(userName, userPassword);
        if (user != null) {
            System.out.println("Login successful. Welcome " + user.getUsername() + "!");
            innerChoice();
        } else {
            System.out.println("Login failed! Try again.");
        }
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

    protected void changePassword() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start
        User user1 = session.load(User.class, user.getId());
        String newPassword = getUserInput("Enter you new password:");
        user1.setPassword(newPassword);
        session.update(user1);
        session.getTransaction().commit();
        session.close();
        System.out.println("Your password updated successfully!");
    }

    protected void showArticles() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        Query query = session.createSQLQuery("select id, title, brief from article ");
        List<Article> articleList = query.list();
        if (articleList.size() == 0) System.out.println("There isn't any article to show!");
        else {
            //todo brief and select to show complete
            articleList.forEach(System.out::println);
            String articleId = getUserInput("Select an article id:");
            Article article = session.load(Article.class, Long.parseLong(articleId));
            System.out.println(article);
        }
        session.close();
    }

    protected Article article(Article article) {
        String title = getUserInput("Title:");
        String brief = getUserInput("Brief:");
        String content = getUserInput("Content:");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // Show categories
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start
        Query query = session.createQuery("from Category ");
        List<Category> categoryList = query.list();
        if (categoryList.size() == 0) System.out.println("There isn't any category to show!");
        else categoryList.forEach(System.out::println);

        String categoryId = getUserInput("Select a category by it's id (or 0 to create a new category):");
        Category category = null;
        if (categoryId.equals("0")) {
            String categoryTitle = getUserInput("Category title:");
            String categoryDescription = getUserInput("Category description:");
            category = new Category(null, categoryTitle, categoryDescription);
            Long newCategoryId = (Long) session.save(category);
            session.getTransaction().commit();
            System.out.println("New category added successfuly!");
        } else {
            category = session.load(Category.class, Long.parseLong(categoryId));
        }

        if (article != null) {
            article.setTitle(title);
            article.setBrief(brief);
            article.setContent(content);
            article.setCategory(category);
            article.setLastUpdateDate(new Date());
            Boolean isPublished = Boolean.parseBoolean(getUserInput("Do you want to publish? (true or false):"));
            if (isPublished) {
                article.setIsPublished(true);
                article.setPublishDate(new Date());
            }
            return article;
        } else {
            Article articleNew = new Article(null, title, brief, content, new Date(), null, null, false, category, user);
            session.close();
            return articleNew;
        }
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
                    System.out.println("Wrong select!");
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

    public static void loginedScreen() {
        System.out.println("1. Show my articles");
        System.out.println("2. Edit my articles");
        System.out.println("3. Add new article");
        System.out.println("4. Change my password");
        System.out.println("5. Publish an article");
        System.out.println("0. Logout");
    }

    private static void innerChoice() {
        int selectedMenu;
        do {
            loginedScreen();
            selectedMenu = Integer.parseInt(getUserInput("Select an option to continue; 0-4"));

            switch (selectedMenu) {
                case 1:
                    new Application().showMyArticles();
                    break;
                case 2:
                    new Application().editArticle();
                    break;
                case 3:
                    new Application().addNewArticle();
                    break;
                case 4:
                    new Application().changePassword();
                    break;
                case 5:
                    new Application().publishArticle();
                    break;
                case 0:
                    LogoutUseCase logoutUseCase = new LogoutUseCaseImpl();
                    logoutUseCase.logout();
                    user = null;
                    System.out.println("Logout successful."); //back to previous menu
                    break;
                default:
                    System.out.println("Wrong select!");
                    break;
            }
        } while (selectedMenu != 0);
    }

    protected void publishArticle() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        Long articleId = Long.parseLong(getUserInput("Enter article id to publish:"));
        Article article = session.load(Article.class, articleId);
        article.setIsPublished(true);
        article.setPublishDate(new Date());
        session.update(article);
        session.getTransaction().commit();
        session.close();
        System.out.println("Your article published successfully!");
    }

    private static String getUserInput(String systemOut) throws NumberFormatException {
        if (systemOut != null && !systemOut.equals("")) System.out.println(systemOut);
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    protected void showMyArticles() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        Query query = session.createQuery("from Article where user = ?1");
        query.setParameter(1, user);
        List<Article> articleList = query.list();
        if (articleList.size() == 0) System.out.println("There isn't any article to show!");
        else articleList.forEach(System.out::println);

        session.close();
    }

    protected void addNewArticle() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start

        Article article = article(null);
        Long id = (Long) session.save(article);
        session.getTransaction().commit();
        session.close();

        System.out.println("New article added successfully.");
    }

    protected void editArticle() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Long articleId = Long.parseLong(getUserInput("Enter article id to edit:"));
        Article article = session.load(Article.class, articleId);
        article = article(article);
        //article.setId(articleId);
        session.update(article);
        session.getTransaction().commit();
        session.close();
        System.out.println("Your article updated successfully!");
    }
}