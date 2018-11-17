package com.example.controller;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Notice;
import com.example.entity.Selfie;
import com.example.entity.User;
import com.example.entity.UserFriend;
import com.example.entity.UserFriendId;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.FindFriendResponse;
import com.example.repository.NoticeRepository;
import com.example.repository.UserFriendRepository;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
	
	@Autowired
	NoticeRepository noticerepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserFriendRepository userfriendrepository;
	
	
	//新增好友 一個人加，對方確認，雙方互為好友
	@PostMapping(value = "/add/{username}")
	public String addFriend(@PathVariable String username , @CurrentUser UserPrincipal currentUser){
	//	if (userfriendrepository.existsById(username))
	//	if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	//		return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
	//	}
		String current = currentUser.getUsername();
		User userdata = new User(current);
		User friend = new User(username);
//		userfriendrepository.findByUser(userdata).map(thedata ->{
//			if(thedata.getFriend().getUsername().equals(current)&& thedata.getUser().getUsername().equals(username)) {
//				return thedata;
//			} 
			if(username.equals(currentUser.getUsername()) ) {
				System.out.println("can not add yourself");
				return null;
			}
			else {
				Notice notice = new Notice();
				System.out.println("currentUser = "+current);
				System.out.println("findUser = "+username);
				return userRepository.findByUsername(username).map(frienddata ->{
				UserFriend findfriend = new UserFriend();
				findfriend.setFriend(friend);
				findfriend.setUser(userdata);
				findfriend.setConfirmed(false);
				notice.setMessage(current+" want to be your friend!");
				notice.setRead(false);
//				notice.setSender(userdata);
				notice.setReceive(friend);
				noticerepository.save(notice);
				userfriendrepository.save(findfriend);
				
			    return "["+current+"]"+" add "+"["+frienddata.getUsername()+"]"+" to friend list";
				}).orElseThrow(()->new ResourceNotFoundException("Fail!!!!!!", username, currentUser));	
				}
			
			
//		});
//		return null;
		
		
		
}
//刪除好友 一個人刪除，雙方名單都會除名
	@Transactional
	@RequestMapping(value = "/delete/{username}", method = RequestMethod.DELETE)
	public void deletefriend(@PathVariable String username, @CurrentUser UserPrincipal currentUser) {
		System.out.println("DELETE currentUser = "+ currentUser.getUsername());
		System.out.println("DELETE username = "+ username);
		User user = new User(currentUser.getUsername());
		User friend = new User(username);
		
		if (username.equals(currentUser.getUsername())) {
			System.out.println("Don't Delete Yourself");
		}else {
		UserFriendId userfriendId = new UserFriendId();
		UserFriendId frienduserId = new UserFriendId();
		userfriendId.setUser(user);
		userfriendId.setFriend(friend);
		frienduserId.setUser(friend);
		frienduserId.setFriend(user);
		userfriendrepository.deleteById(userfriendId);
		userfriendrepository.deleteById(frienduserId);	
		}
	}

//			else {
//		userRepository.findByUsername(username).map(users ->{
//			System.out.println("User List:");
//			System.out.println("User name:"+ users.getUsername());
//			System.out.println("User email:"+ users.getEmail());
			 // System.out.println("Enter");
//			System.out.println("FindaUser = "+users.getUsername());
//			if(users.getUsername().equals(username)) {
//				System.out.println("DELETE SUCCESSFULLY!");
//				return userfriendrepository.deleteByUser(users);
//				
//				
//			}
//				else {
//				System.out.println("DELETE Failed!");
//				  return null;
//					}).orElseThrow(()->new ResourceNotFoundException("Faill!!!!!!", currentUser.getUsername(), null));
//			}
//			return null;
//			    
//		
//		}).orElseThrow(()->new ResourceNotFoundException("Faill!!!!!!", currentUser.getUsername(), null));
	
	
	@GetMapping("/find/{friendname}")
	public FindFriendResponse findfriend(@PathVariable("friendname")String fname) {
		return userRepository.findByUsername(fname).map(friend ->{
			Selfie selfie = friend.getSelfie();
			String uri = selfie.getSelfieUri();
			String friendname = friend.getUsername();
			FindFriendResponse findfriendres = new FindFriendResponse(friendname, uri);
			
			return findfriendres;
		}).orElseThrow(() -> new ResourceNotFoundException("User" + fname + "not found", null, null));
		
	}


}
