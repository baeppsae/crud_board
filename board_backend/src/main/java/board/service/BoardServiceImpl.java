package board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.common.FileUtils;
import board.dto.BoardDto;
import board.dto.BoardFileDto;
import board.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BoardDto> selectBoardList() {
		return boardMapper.selectBoardList();
	}

	@Override
	public void insertBoard(BoardDto boardDto, MultipartHttpServletRequest request) throws Exception {
		String username = "hong"; // SecurityContextHolder.getContext().getAuthentication().getName();
		boardDto.setCreatorId(username);
		boardMapper.insertBoard(boardDto);
		
		List<BoardFileDto> fileInfoList = fileUtils.parseFileInfo(boardDto.getBoardIdx(), request);
		if (!CollectionUtils.isEmpty(fileInfoList)) {
			boardMapper.insertBoardFileList(fileInfoList);
		}
	}

	@Override
	public BoardDto selectBoardDetail(int boardIdx) {
		boardMapper.updateHitCount(boardIdx);
		
		BoardDto boardDto = boardMapper.selectBoardDetail(boardIdx);
		if (boardDto != null) {
			List<BoardFileDto> boardFileInfo = boardMapper.selectBoardFileList(boardIdx);
			boardDto.setFileInfoList(boardFileInfo);
		}
		
		return boardDto;
	}

	@Override
	public void updateBoard(BoardDto boardDto) {
		// TODO. 로그인한 사용자 아이디로 변경
		boardDto.setUpdatorId("go");
		boardMapper.updateBoard(boardDto);		
	}

	@Override
	public void deleteBoard(int boardIdx) {
		BoardDto boardDto = new BoardDto();
		boardDto.setBoardIdx(boardIdx);
		// TODO. 로그인한 사용자 아이디로 변경
		boardDto.setUpdatorId("go");
		boardMapper.deleteBoard(boardDto);		
	}

	@Override
	public BoardFileDto selectBoardFileInfo(int idx, int boardIdx) {
		return boardMapper.selectBoardFileInfo(idx, boardIdx);
	}

	@Override
	public void insertBoardWithFile(BoardDto boardDto, MultipartFile[] files) throws Exception {
		// TODO. 로그인한 사용자의 ID로 변경
		boardDto.setCreatorId("hong");
		boardMapper.insertBoard(boardDto);
		
		// 첨부 파일을 저장하고 첨부 파일 정보를 반환 
		// TODO 내일 이어서 구현 ... 
		List<BoardFileDto> fileInfoList = fileUtils.parseFileInfo(boardDto.getBoardIdx(), files);
		
		// 첨부 파일 정보를 저장
		if (!CollectionUtils.isEmpty(fileInfoList)) {
			boardMapper.insertBoardFileList(fileInfoList);
		}
	}
}
