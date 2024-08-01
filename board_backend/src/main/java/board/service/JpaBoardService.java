package board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.entity.BoardEntity;
import board.entity.BoardFileEntity;

public interface JpaBoardService {
	List<BoardEntity> selectBoardList();
	void insertBoard(BoardEntity boardEntity, MultipartHttpServletRequest request) throws Exception;
	void updateBoard(BoardEntity boardEntity) throws Exception;
	void deleteBoard(int boardIdx);
	BoardFileEntity selectBoardFileInfo(int idx, int boardIdx);
	BoardEntity selectBoardDetail(int boardIdx) throws Exception;
}
