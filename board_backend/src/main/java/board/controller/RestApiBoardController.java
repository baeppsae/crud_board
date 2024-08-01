package board.controller;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import board.dto.BoardDto;
import board.dto.BoardFileDto;
import board.dto.BoardListResponse;
import board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:3000", allowedHeaders="GET")
public class RestApiBoardController {
	
	@Autowired
	private BoardService boardService;
	
	// @CrossOrigin(origins="http://localhost:3000", allowedHeaders="GET")
	@Operation(summary = "게시판 목록 조회", description = "등록된 게시물 목록을 조회해서 반환합니다.")
	@GetMapping("/board")
	public List<BoardListResponse> openBoardList(HttpServletRequest request) throws Exception {
		List<BoardDto> boardList = boardService.selectBoardList();

		List<BoardListResponse> results = new ArrayList<>();
		/*
		for (BoardDto dto : boardList) {
			BoardListResponse res = new ModelMapper().map(dto, BoardListResponse.class);
			results.add(res);
		}
		*/
		boardList.forEach(dto -> {
			results.add(new ModelMapper().map(dto, BoardListResponse.class));
		});
		
		return results;
	}
	
	@Operation(summary = "게시판 등록", description = "게시물 제목과 내용을 저장합니다.")
	@Parameter(name = "boardDto", description = "게시물 정보를 담고 있는 객체", required = true)
	@PostMapping("/board/write")
	/*
	public void insertBoard(@RequestBody BoardDto boardDto, MultipartHttpServletRequest request) throws Exception {
		boardService.insertBoard(boardDto, request);
	}
	*/
	public void insertBoard(
			@RequestPart(value="data", required=true) BoardDto boardDto, 
			@RequestPart(value="files", required=false) MultipartFile[] files) throws Exception {
		boardService.insertBoardWithFile(boardDto, files);
	}
	
	@GetMapping("/board/{boardIdx}")
	public ResponseEntity<Object> openBoardDetail(
			@PathVariable("boardIdx") int boardIdx) throws Exception {
		BoardDto boardDto = boardService.selectBoardDetail(boardIdx);
		if (boardDto == null) {
			Map<String, String> result = new HashMap<>();
			result.put("code", HttpStatus.NOT_FOUND.toString());
			result.put("message", "일치하는 게시물이 존재하지 않습니다.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(boardDto);
		}
	}
	
	@PutMapping("/board/{boardIdx}")
	public void updateBoard(@PathVariable("boardIdx") int boardIdx, @RequestBody BoardDto boardDto) throws Exception {
		boardDto.setBoardIdx(boardIdx);
		boardService.updateBoard(boardDto);
	}
	
	@DeleteMapping("/board/{boardIdx}")
	public void deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception {
		boardService.deleteBoard(boardIdx);
	}

	@GetMapping("/board/file/{boardIdx}/{idx}")
	public void downloadBoardFile(@PathVariable("idx") int idx, @PathVariable("boardIdx") int boardIdx, HttpServletResponse response) throws Exception {
		BoardFileDto boardFileDto = boardService.selectBoardFileInfo(idx, boardIdx);
		if (ObjectUtils.isEmpty(boardFileDto)) {
			return;
		}
		
		Path path = Paths.get(boardFileDto.getStoredFilePath());
		byte[] file = Files.readAllBytes(path); 
		
		response.setContentType("application/octet-stream");
		response.setContentLength(file.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(boardFileDto.getOriginalFileName(), "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(file);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	@GetMapping("/board/hello")
	public List<String> hello() {
		List<String> msg = new ArrayList<>();
		msg.add("hello, 홍길동");
		msg.add("hello, 고길동");
		return msg;
	}
}
