package servlet;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.NotificationDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.services.impl.BugService;
import ro.msg.edu.jbugs.services.impl.CommentService;
import ro.msg.edu.jbugs.services.impl.NotificationService;
import ro.msg.edu.jbugs.services.impl.UserService;
import ro.msg.edu.jbugs.timer.TimerBean;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; UserLogin Name.
 * @since 19.1.2
 */

@WebServlet("/TSL")
public class TestServlet extends HttpServlet {

    @EJB
    UserService userService;

    @EJB
    BugService bugService;

    @EJB
    CommentService commentService;

    @EJB
    NotificationService notificationService;

    @EJB
    TimerBean timmerBean;


    @Override
    public void init() throws ServletException {
        System.out.println("Init done");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        //try{
        UserDto userDto = new UserDto(1, 1, "Fnts", "Lnts", "et@msggroup.com", "+40712345678", "pt", "unt", true);
//                addUserDefault();
//        try {
        // UserDto userdtoReturned = addUser(userDto);
//        } catch (BusinessException e) {
//            e.printStackTrace();
//        }
//            userService.login(userdtoReturned);
                //userService.deleteUser(userdtoReturned);
//                addNotification(2);
//                out.println("<h1> done <h1>");
//            } catch (BusinessException e) {
//                System.out.println(e.getMessage()+e.getErrorCode());
//            }





        out.println("<h1>" + "" + "</h1> <br>");
    }




    private UserDto addUser(UserDto userDto) throws IOException, BusinessException {
        userService.addUser(userDto);
        return userDto;
    }

    private UserDto findUser(Integer id) throws BusinessException {
        UserDto userDto1 = userService.findUser(id);
        return userDto1;
    }

    private List<UserDto> getAllUser() {
        List<UserDto> userDtoList = userService.getAllUser();
        return userDtoList;
    }

    private BugDto addBug(BugDto bugDto) throws BusinessException {
        bugService.addBug(bugDto);
        return bugDto;
    }

    private BugDto findBug(Integer id) {
        return bugService.findBug(id);
    }

    private List<BugDto> getAllBugCretedBy(Integer id) throws BusinessException {
        return userService.getAllCreatedBugs(id);
    }


    private void addNotification(Integer userId) throws BusinessException {
        UserDto userDto = userService.findUser(userId);
        NotificationDto notificationDto = new NotificationDto(0,new Timestamp(Calendar.getInstance().getTimeInMillis()),"Hello","myType","noFound",userDto);
        try {
            notificationService.addNotification(notificationDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printRaport(PrintWriter out) throws IOException, BusinessException {
        System.out.println("Hello");
        List<UserDto> userDtoList = getAllUser();
        List<BugDto> createdByUser;
        for (UserDto userDto : userDtoList) {
            out.println("UserLogin: " + userDto.getUsername() + " Created :<br>");
            createdByUser = getAllBugCretedBy(userDto.getId());
            for (BugDto bugDto : createdByUser) {
                out.println(bugDto.getId() + ", " + bugDto.getTitle() + ", " + bugDto.getCreated().getId() + ", " + bugDto.getFixedVersion() + "<br>");
            }
            out.println("------------------------------------<br>");
        }

    }

    private void defaultAddAll() throws IOException, BusinessException {
        //persist.xml restart line on
        addUserDefault();
        addBugsDefault();
        addCommentDefault();
    }

    private void addUserDefault() throws IOException, BusinessException {
        UserDto userDto1 = new UserDto(0, 0, "fn1", "ln1", "email1", "0123456", "pswd1", "username1", true);
        UserDto userDto2 = new UserDto(0, 0, "fn2", "ln2", "email2", "0123456", "pswd2", "username2", true);
        userService.addUser(userDto1);
        userService.addUser(userDto2);
    }

    private void addBugsDefault() throws BusinessException {
        UserDto userDto = userService.findUser(1);
        UserDto userDto2 = userService.findUser(2);
        BugDto bugDto = new BugDto(0, "title1", "desc1", "v1", Timestamp.valueOf("1/1/2012"), "active", "v14", "LOW", userDto, userDto2);
        addBug(bugDto);
        userDto = userService.findUser(1);
        userDto2 = userService.findUser(1);
        bugDto = new BugDto(0, "title1", "desc1", "v1", Timestamp.valueOf("1/1/2017"), "active", "v14", "LOW", userDto, userDto2);
        addBug(bugDto);
        userDto = userService.findUser(2);
        userDto2 = userService.findUser(1);
        bugDto = new BugDto(0, "title1", "desc1", "v1", Timestamp.valueOf("1/1/2016"), "active", "v14", "LOW", userDto, userDto2);
        addBug(bugDto);
        userDto = userService.findUser(2);
        userDto2 = userService.findUser(2);
        bugDto = new BugDto(0, "title1", "desc1", "v1", Timestamp.valueOf("1/1/2002"), "active", "v14", "LOW", userDto, userDto2);
        addBug(bugDto);
    }

    private void addCommentDefault() throws BusinessException {
        UserDto user1 = findUser(1);
        UserDto user2 = findUser(2);
        BugDto bug1 = findBug(1);
        BugDto bug2 = findBug(2);

        commentService.addComment(user1, user2, bug1, bug2);
    }

    private Integer deleteOldComments() {
        return commentService.deleteOldComment();
    }
}
