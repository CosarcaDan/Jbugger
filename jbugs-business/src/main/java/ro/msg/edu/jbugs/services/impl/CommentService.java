package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.repo.CommentRepo;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.interceptor.CallDurationInterceptor;
import ro.msg.edu.jbugs.entity.Comment;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.Date;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
@Interceptors(CallDurationInterceptor.class)
public class CommentService {
    @EJB
    CommentRepo commentRepo;

    public Integer deleteOldComment(){
        Date date = new Date();
        date.setYear(date.getYear()-1);
        Integer result = commentRepo.deleteOldComments(date);
        return result;
    }

    public void addComment(UserDto user1, UserDto user2, BugDto bug1, BugDto bug2){
//        User user11 = UserDtoMapping.userDtoToUser(user1);
//        User user12 = UserDtoMapping.userDtoToUser(user2);
//        Bug bug11 = BugDtoMapping.bugDtoToBug(bug1,user11,user12);
//        Bug bug12 = BugDtoMapping.bugDtoToBug(bug1,user12,user12);
//        Comment comment1 = new Comment("com1",new Date("1/1/2012"),user11,bug11);
//        Comment comment2 = new Comment("com2",new Date("1/1/2014"),user12,bug12);
//        Comment comment3 = new Comment("com2",new Date("1/1/2017"),user11,bug12);
//        Comment comment4 = new Comment("com3",new Date("1/1/2002"),user12,bug11);
//        commentRepo.addComment(comment1);
//        commentRepo.addComment(comment2);
//        commentRepo.addComment(comment3);
//        commentRepo.addComment(comment4);
    }
}
