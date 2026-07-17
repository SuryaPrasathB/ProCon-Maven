package com.tasnetwork.spring.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.exception.ResourceNotFoundException;






@RestController
@RequestMapping("/api")
public class RestAppController {

//	@Autowired
	//CommentRepository commentRepository;
	
	@GetMapping(value = "/")
    public String home() {
		
		System.out.println("RestAppController: home : Entry");
		ApplicationLauncher.logger.info("RestAppController: home path hit");
        return "Hello TAS Network!";
    }
/*	
	@PostMapping(value="/setupData",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			//,produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)  
	public ResponseEntity<Object> setupDbData(@RequestBody Comment inputCommentData)  {  
		System.out.println("RestAppController: setupDbData : Entry");
		Comment responseData = new Comment("BadBoy", "Dont Play with Me", 105);
		if(inputCommentData.getPostName().equals("createDataSetup")){
			
			System.out.println("RestAppController: setupDbData : creating data setup");
			
			responseData = new Comment("Good", "You have valid credential", 100);
			Comment commentData = new Comment("test2", "Nice Post", 201);
			Comment updatedComment = commentRepository.save(commentData);	
			commentData = new Comment("test1", "Nice Post", 100);
			updatedComment = commentRepository.save(commentData);
			
			commentData = new Comment("test1", "comment1", 50);
			updatedComment = commentRepository.save(commentData);
			
			commentData = new Comment("test1", "comment2", 600);
			updatedComment = commentRepository.save(commentData);
			
			commentData = new Comment("test3", "comment3", 300 );
			updatedComment = commentRepository.save(commentData);
			
			commentData = new Comment("test1", "comment3", 400);
			updatedComment = commentRepository.save(commentData);
			
			commentData = new Comment("test2", "comment3", 375);
			updatedComment = commentRepository.save(commentData);
			
			commentData = new Comment("testGold2", "comment3", 375);
			updatedComment = commentRepository.save(commentData);
			
			//return ResponseEntity<Comment>(responseData,HttpStatus.CREATED).build();
			return ResponseEntity.ok(responseData);
		}else{
			System.out.println("RestAppController: setupDbData: invalid setup criteria received");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
		}
	}  
	
	
	@DeleteMapping(value = "/deleteDataSetup",
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
		//,produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
		) 
    public ResponseEntity<?> deleteDataSetup(@RequestBody Comment inputCommentData) {

        var isRemoved = postService.delete(id);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
		
		System.out.println("RestAppController: deleteDataSetup : Entry");
		Comment responseData = new Comment("Naughty Boy", "Dont be crazy", 106);
		if(inputCommentData.getPostName().equals("delDataSetup")){
			
			System.out.println("RestAppController: deleteDataSetup : creating data setup");
			responseData = new Comment("Success", "You are good to go", 107);
			 
			commentRepository.deleteAll();	
			return ResponseEntity.ok(responseData);
		}else{
			System.out.println("RestAppController: deleteDataSetup: invalid setup criteria received");
			return new ResponseEntity<>(responseData, HttpStatus.FORBIDDEN);
		}
    }
	
	
	@GetMapping("/comments")
	public List<Comment> getAllComments() {
		
		
		List<Comment> dataList = commentRepository.findAll();
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAllComments : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		//Collections.sort(dataList,(e1,e2) -> e1.getAmount()>e2.getAmount()?e2.getAmount():e2.getAmount());
		List<Comment> sortedDataList = (List<Comment> ) dataList.stream()
										.sorted(Comparator.comparingInt(Comment::getAmount).reversed())
										.collect(Collectors.toList());
		sortedDataList.stream().forEach(e-> System.out.println("RestAppController: sorted getAllComments : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}
	
	@GetMapping("/amountOrderedByPostName")
	//http://127.0.0.1:8083/api/amountOrderedByPostName?postName=test2
	public List<Comment> getAmountOrderedByPostName(@RequestParam String postName) {
		
		System.out.println("RestAppController: postName : " + postName);
		List<Comment> dataList = commentRepository.findByPostNameOrderByAmountAsc(postName);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedByPostName : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}
	
	@GetMapping("/amountOrderedBy")
	public List<Comment> getAmountOrderedBy(){//@RequestParam String postName) {
		
		List<Comment> dataList = commentRepository.findByOrderByAmountDesc();
		
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedBy : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		return dataList;
	}
	
	@GetMapping("/amountOrderedBy2")
	public List<Comment> getAmountOrderedBy2(){//@RequestParam String postName) {
		
		//CommentService commentSrv = new CommentService();
		
		//List<Comment> dataList = commentSrv.getAmountOrderedBy2();
		
		List<Comment> dataList = commentRepository.findAll(Sort.by(Sort.Direction.ASC, "amount"));
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedBy2 : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}
	
	@GetMapping("/amountOrderedBy3")
	public List<Comment> getAmountOrderedBy3(){//@RequestParam String postName) {
		
		//CommentService commentSrv = new CommentService();
		
		//List<Comment> dataList = commentSrv.getAmountOrderedBy2();
		
		//Pageable pageSortQuery = PageRequest.of(page, size, sort)
		
		List<Comment> dataList = commentRepository.findAll(Sort.by("amount").descending().and(Sort.by("postName")));
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedBy3 : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}
	
	
	@GetMapping("/first2By")
	public List<Comment> getFirst2PostName(@RequestParam String postName){//@RequestParam String postName) {
		
	
		List<Comment> dataList = commentRepository.findFirst2ByPostName(postName,Sort.by("postName"));
		dataList.stream().forEach(e-> System.out.println("RestAppController: getFirst2PostName : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}

	@GetMapping("/top3By")
	public ResponseEntity<Object> getTop3ByPostName(@RequestParam String postName){//@RequestParam String postName) {
		
		Pageable pageSortQuery = PageRequest.of(1, 4, Sort.by("postName"));
		Page<Comment> dataList = commentRepository.findTop3ByPostName(postName,pageSortQuery);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getTop3ByPostName : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return ResponseEntity.ok(dataList.getContent());
	}
	
	@GetMapping("/byContaining")
	public ResponseEntity<Object> getByPostNameContaining(@RequestParam String postName){//@RequestParam String postName) {
		
		//Pageable pageSortQuery = PageRequest.of(1, 4, Sort.by("postName"));
		Streamable<Comment> dataList = commentRepository.findByPostNameContaining(postName);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getByPostNameContaining : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return ResponseEntity.ok(dataList.get());
	}
	
	
	@GetMapping("spFilteredPost")
	public ResponseEntity<Object> getSpQueryFilteredPostComment(@RequestParam String postName){//@RequestParam String postName) {
		
		//Pageable pageSortQuery = PageRequest.of(1, 4, Sort.by("postName"));
		List<Comment> dataList = commentRepository.getQueryFilteredPostComment(postName);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getSpQueryFilteredPostComment : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return ResponseEntity.ok(dataList);
	}
	
	
	@GetMapping("spProcFilteredPost")
	public ResponseEntity<Object> getSpProcFilteredPostComment(@RequestParam String postName){//@RequestParam String postName) {
		
		//Pageable pageSortQuery = PageRequest.of(1, 4, Sort.by("postName"));
		List<Comment> dataList = commentRepository.getPostCommentFilter(postName);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getSpProcFilteredPostComment : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return ResponseEntity.ok(dataList);
	}

	
	@GetMapping("/amountSortedByPage")
	public ResponseEntity<Object> getAmountOrderedBy4(){//@RequestParam String postName) {
		
		Pageable pagableSortedByAmountDesc = 
				  PageRequest.of(0, 2, Sort.by("amount").descending());
		
		Page<Comment> pageDataList = null;//commentRepository.findAll(pagableSortedByAmountDesc);
		
		List<Comment> resultContentList = new ArrayList<Comment>();
		//pageDataList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedBy3 : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		while(true){
			
			pageDataList = commentRepository.findAll(pagableSortedByAmountDesc);
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getTotalPages(): " + pageDataList.getTotalPages());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getTotalElements(): " + pageDataList.getTotalElements());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getNumber(): " + pageDataList.getNumber());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getSize(): " + pageDataList.getSize());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getNumberOfElements(): " + pageDataList.getNumberOfElements());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getContent(): " + pageDataList.getContent());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.nextPageable(): " + pageDataList.nextPageable());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.hasNext(): " + pageDataList.hasNext());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.nextOrLastPageable(): " + pageDataList.nextOrLastPageable());
			System.out.println("RestAppController: getAmountOrderedBy4 : pageDataList.getPageable(): " + pageDataList.getPageable());
			
			List<Comment> currentPageContentList = pageDataList.getContent();
			
			currentPageContentList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedBy4 : currentPageContentList: id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
			resultContentList.addAll(currentPageContentList);
			if(!pageDataList.hasNext()){
				break;
			}
			
			pagableSortedByAmountDesc = pageDataList.nextPageable();
		
		//pageDataList = commentRepository.findAll(pagableSortedByAmountDesc);
		
		}
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.getTotalPages(): " + pageDataList.getTotalPages());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.getTotalElements(): " + pageDataList.getTotalElements());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.getNumber(): " + pageDataList.getNumber());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.getSize(): " + pageDataList.getSize());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.getNumberOfElements(): " + pageDataList.getNumberOfElements());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.getContent(): " + pageDataList.getContent());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.nextPageable(): " + pageDataList.nextPageable());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.hasNext(): " + pageDataList.hasNext());
		System.out.println("RestAppController: getAmountOrderedBy3-2 : pageDataList.nextOrLastPageable(): " + pageDataList.nextOrLastPageable());
		
		currentPageContentList = pageDataList.getContent();
		
		currentPageContentList.stream().forEach(e-> System.out.println("RestAppController: getAmountOrderedBy3 : currentPageContentList: id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		
		return ResponseEntity.ok(resultContentList);//.build();
	}
	
	@GetMapping("/amountLessThan")
	public List<Comment> getAmountLessThan(@RequestParam int amount) {
		
		
		List<Comment> dataList = commentRepository.findByAmountLessThanEqual(amount);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAmountLessThan : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}
	
	@GetMapping("/amountGreaterThan")
	public List<Comment> getAmountGreaterThan(@RequestParam(defaultValue = "100") int amount) {
		
		
		List<Comment> dataList = commentRepository.findByAmountGreaterThanOrderByAmountAsc(amount);
		dataList.stream().forEach(e-> System.out.println("RestAppController: getAmountGreaterThan : id: " + e.getId() + ", name: " + e.getPostName()+ ", amount: " + e.getAmount()));
		
		return dataList;
	}
	

	@PostMapping("/comments")
	public Comment createComment(@RequestBody Comment comment) {
		
		
		System.out.println("RestAppController: createComment: getPostName:" + comment.getPostName());
		System.out.println("RestAppController: createComment: getAmount:" + comment.getAmount());
		System.out.println("RestAppController: createComment: getUserComment:" + comment.getUserComment());
		return commentRepository.save(comment);
	}

	@GetMapping("/comments/{id}")
	public Comment getCommentById(@PathVariable(value = "id") Long commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
	}
	
	//@GetMapping("/name/{postname}")
	@GetMapping("/name")
	public ResponseEntity<List<Comment>> getCommentByPostName(@RequestParam String postName) {
	//public ResponseEntity<List<Comment>> getCommentByPostName(@PathVariable(value = "postName") String inputPostName) {
		System.out.println("RestAppController: getCommentByPostName : Entry");
		
		System.out.println("RestAppController: getCommentByPostName : postName: " + postName);
		CommentService commentSrv = new CommentService();
		//commentRepository.
		//return commentSrv.findByPostName(inputPostName)
		//	.orElseThrow(() -> new ResourceNotFoundException("Comment", "post_name", inputPostName));
		//return commentRepository.findBypostName(inputPostName)
		//		.orElseThrow(() -> new ResourceNotFoundException("Comment", "post_name", inputPostName));
		return new ResponseEntity<List<Comment>>(commentRepository.findByPostName(postName), HttpStatus.OK);
	}
	
	@GetMapping("/nameOrderBy")
	public ResponseEntity<List<Comment>> getPostNameByOrderId(@RequestParam String postName) {
		System.out.println("RestAppController: getPostNameByOrderId : Entry");
		System.out.println("RestAppController: getPostNameByOrderId : postName: " + postName);
		CommentService commentSrv = new CommentService();
		return new ResponseEntity<List<Comment>>(commentRepository.findByPostNameOrderById(postName), HttpStatus.OK);
	}
	
	@GetMapping("/countByPostName")
	public Long getCountByPostName(@RequestParam String postName) {
		System.out.println("RestAppController: getCountByPostName : Entry");
		System.out.println("RestAppController: getCountByPostName : postName: " + postName);
		//CommentService commentSrv = new CommentService();
		//return new ResponseEntity<List<Comment>>(commentRepository.countByPostName(postName), HttpStatus.OK);
		return commentRepository.countByPostName(postName)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "postName", postName));
	}
	
	
	@GetMapping("/filterNotInPostName")
	public ResponseEntity<List<Comment>> getFilterNotInPostName(){//@RequestParam String postName) {
		System.out.println("RestAppController: getFilterNotInPostName : Entry");
		//System.out.println("RestAppController: getDistinctPostName : postName: " + postName);
		List<String> postName = new ArrayList<String>(Arrays.asList("test2","test1"));
		return new ResponseEntity<List<Comment>>(commentRepository.findByPostNameNotIn(postName), HttpStatus.OK);
		
		//return commentRepository.findDistinctPostName()
		//		.orElseThrow(() -> new ResourceNotFoundException("Comment", "Distinct", "value"));
	}
	
	@GetMapping("/filterInPostName")
	public ResponseEntity<List<Comment>> getFilterInPostName(){//@RequestParam String postName) {
		System.out.println("RestAppController: getFilterInPostName : Entry");
		//System.out.println("RestAppController: getDistinctPostName : postName: " + postName);
		List<String> postName = new ArrayList<String>(Arrays.asList("test2","test1"));
		return new ResponseEntity<List<Comment>>(commentRepository.findByPostNameIn(postName), HttpStatus.OK);
		
		//return commentRepository.findDistinctPostName()
		//		.orElseThrow(() -> new ResourceNotFoundException("Comment", "Distinct", "value"));
	}
	
	@GetMapping("/distinctPostName")
	public ResponseEntity<List<String>> getDistinctPostName(){//@RequestParam String postName) {
		System.out.println("RestAppController: getDistinctPostName : Entry");
		//System.out.println("RestAppController: getDistinctPostName : postName: " + postName);
		List<String> postName = new ArrayList<String>(Arrays.asList("test2","test1"));
		return new ResponseEntity<List<String>>(commentRepository.getDistinctPostName(), HttpStatus.OK);
		
		//return commentRepository.findDistinctPostName()
		//		.orElseThrow(() -> new ResourceNotFoundException("Comment", "Distinct", "value"));
	}
	
	@GetMapping("/customData/{postName}")
	public ResponseEntity<List<Comment>> getCustomQueryData(@PathVariable String postName, @RequestParam String userComment,@RequestParam int amount) {
		System.out.println("RestAppController: getCustomQueryData : Entry");
		System.out.println("RestAppController: getCustomQueryData : postName: " + postName);
		System.out.println("RestAppController: getCustomQueryData : userComment: " + userComment);
		System.out.println("RestAppController: getCustomQueryData : amount: " + amount);
		//List<String> postName = new ArrayList<String>(Arrays.asList("test2","test1"));
		return new ResponseEntity<List<Comment>>(commentRepository.findCustomData(postName,userComment,amount), HttpStatus.OK);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{postName}")
				.buildAndExpand(postName).toUri();

		return ResponseEntity.created(location).build();
		
		//return commentRepository.findDistinctPostName()
		//		.orElseThrow(() -> new ResourceNotFoundException("Comment", "Distinct", "value"));
	}
	
	@GetMapping("/cmt")
	public ResponseEntity<List<Comment>> getCommentByCommentData(@RequestParam String userComment) {
	//public ResponseEntity<List<Comment>> getCommentByPostName(@PathVariable(value = "postName") String inputPostName) {
		System.out.println("RestAppController: getCommentByCommentData : Entry");
		
		System.out.println("RestAppController: getCommentByCommentData : comment: " + userComment);
		CommentService commentSrv = new CommentService();
		//commentRepository.
		//return commentSrv.findByPostName(inputPostName)
		//	.orElseThrow(() -> new ResourceNotFoundException("Comment", "post_name", inputPostName));
		//return commentRepository.findBypostName(inputPostName)
		//		.orElseThrow(() -> new ResourceNotFoundException("Comment", "post_name", inputPostName));
		return new ResponseEntity<List<Comment>>(commentRepository.findByUserComment(userComment), HttpStatus.OK);
	}
	
	@GetMapping("/both")
	public ResponseEntity<List<Comment>> getCommentByNameCommentData(@RequestParam String postName,@RequestParam String userComment) {
	//public ResponseEntity<List<Comment>> getCommentByPostName(@PathVariable(value = "postName") String inputPostName) {
		System.out.println("RestAppController: getCommentByNameCommentData : Entry");
		
		System.out.println("RestAppController: getCommentByNameCommentData : userComment: " + userComment);
		return new ResponseEntity<List<Comment>>(commentRepository.findByPostNameAndUserComment(postName,userComment), HttpStatus.OK);
	}
	
	@GetMapping("/Or")
	public ResponseEntity<List<Comment>> getCommentNameOrCommentData(@RequestParam String postName,@RequestParam String userComment) {
	//public ResponseEntity<List<Comment>> getCommentByPostName(@PathVariable(value = "postName") String inputPostName) {
		System.out.println("RestAppController: getCommentNameOrCommentData : Entry");
		
		System.out.println("RestAppController: getCommentNameOrCommentData : userComment: " + userComment);
		return new ResponseEntity<List<Comment>>(commentRepository.findByPostNameOrUserComment(postName,userComment), HttpStatus.OK);
	}

	@PutMapping("/comments/{id}")
	public Comment updateComment(@PathVariable(value = "id") Long commentId, @RequestBody Comment commentDetails) {

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		comment.setPostName(commentDetails.getPostName());
		comment.setUserComment(commentDetails.getUserComment());

		Comment updatedComment = commentRepository.save(comment);
		return updatedComment;
	}

	@DeleteMapping("/comments/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "id") Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		commentRepository.delete(comment);

		return ResponseEntity.ok().build();
	}
	*/
	
}
