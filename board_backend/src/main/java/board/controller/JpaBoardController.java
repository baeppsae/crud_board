package board.controller;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.entity.BoardEntity;
import board.entity.BoardFileEntity;
import board.service.JpaBoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/jpa")
public class JpaBoardController {
	
	@Autowired
	private JpaBoardService jpaBoardService;
	
	@GetMapping("/board")
	public ModelAndView openBoardList(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/board/jpaBoardList");
		
		List<BoardEntity> list = jpaBoardService.selectBoardList();
		mv.addObject("list", list);
		
		return mv;
	}
	
	@GetMapping("/board/write")
	public String openBoardWrite() throws Exception {
		return "/board/jpaBoardWrite";
	}
	
	@PostMapping("/board/write")
	public String insertBoard(BoardEntity boardEntity, MultipartHttpServletRequest request) throws Exception {
		jpaBoardService.insertBoard(boardEntity, request);
		return "redirect:/jpa/board";	
	}
	
	@GetMapping("/board/{boardIdx}")
	public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception {
		ModelAndView mv = new ModelAndView("/board/jpaBoardDetail");
		
		BoardEntity boardEntity = jpaBoardService.selectBoardDetail(boardIdx);
		mv.addObject("board", boardEntity);
		
		return mv;
	}
	
	@PutMapping("/board/{boardIdx}")
	public String updateBoard(@PathVariable("boardIdx") int boardIdx, BoardEntity boardEntity) throws Exception {
		jpaBoardService.updateBoard(boardEntity);
		return "redirect:/jpa/board";
	}
	
	@DeleteMapping("/board/{boardIdx}")
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception {
		jpaBoardService.deleteBoard(boardIdx);
		return "redirect:/jpa/board";
	}

	@GetMapping("/board/file/{boardIdx}/{idx}")
	public void downloadBoardFile(@PathVariable("idx") int idx, @PathVariable("boardIdx") int boardIdx, HttpServletResponse response) throws Exception {
		BoardFileEntity boardFileEntity = jpaBoardService.selectBoardFileInfo(idx, boardIdx);
		if (ObjectUtils.isEmpty(boardFileEntity)) {
			return;
		}
		
		Path path = Paths.get(boardFileEntity.getStoredFilePath());
		byte[] file = Files.readAllBytes(path); 
		
		response.setContentType("application/octet-stream");
		response.setContentLength(file.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(boardFileEntity.getOriginalFileName(), "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(file);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
